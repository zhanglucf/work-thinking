# Netty核心技术

## I/O模型

> I/O模型是什么？就是用什么样的通道进行数据的发送和接收，很大程度上决定了程序通信的性能

## Java共支持3种网络编程模型/IO模式：BIO、NIO、AIO

>:one:BIO ： 同步并阻塞(传统阻塞型)  
>
>:two:NIO：  同步非阻塞
>
>:three:AIO(NIO.2)：异步非阻塞

## BIO

>服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销，可以通过线程池机制改善
>
>IO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，程序简单易理解

>```java
>public class BIOServer {
>
>    //telnet 127.0.0.1 6666
>    public static void main(String[] args) throws IOException {
>        ExecutorService executorService = Executors.newCachedThreadPool();
>        ServerSocket serverSocket = new ServerSocket(6666);
>        System.out.println("服务器启动了");
>                while (true){
>                    System.out.println(Thread.currentThread().getName() + "- 等待客户端建立连接----");
>                    Socket accept = serverSocket.accept();
>                    System.out.println("----与客户端建立连接----");
>                    System.out.println(Thread.currentThread().getName() + " 连接一个客户端");
>                    executorService.execute(()->handler(accept));
>                }
>    }
>
>    public static void handler(Socket socket){
>        System.out.println();
>
>        byte[] bytes = new byte[1024];
>        try {
>            InputStream inputStream = socket.getInputStream();
>            while (true){
>                System.out.println(Thread.currentThread().getName() + "- 等待客户端发送消息----");
>                int read = inputStream.read(bytes);
>                if (read != -1) {
>                    System.out.println(Thread.currentThread().getName() + " : " +new String(bytes,0,read));
>                }else {
>                    break;
>                }
>            }
>        } catch (IOException e) {
>            e.printStackTrace();
>        }finally {
>            System.out.println("关闭和客户端的连接");
>            try {
>                socket.close();
>            } catch (IOException e) {
>                e.printStackTrace();
>            }
>        }
>    }
>}
>
>```
>
><u>serverSocket.accept()   与inputStream.read(bytes)方法都是阻塞的</u>



## NIO

> NIO 全称 java non-blocking IO
>
> 从 JDK1.4 开始，Java 提供了一系列改进的输入/输出的新特性，被统称为 NIO(即 New IO)，是同步非阻塞的
>
> NIO 相关类都被放在 java.nio 包及子包下，并且对原 java.io 包中的很多类进行改写。

### NIO 有三大核心部分：

:one:Channel(通道)

:two:Buffer(缓冲区)

:three:Selector(选择器) 

![image-20210722174932138](../images/image-20210722174932138.png)

```
上图是三个Channel注册到了Selector上
```



> Buffer是一个内存块，底层是一个数组
>
> 每个Channel都对应一个buffer
>
> 一个Selector对应一个线程 
>
> 一个线程对应多个Channel
>
> 程序切换到哪个Channel是由事件决定的
>
> Selector可以根据不同的事件在不同的Channel切换
>
> 数据的读取写通过过Buffer,这个和BIO是不同的，但是NIO的Buffer是可以写，也可以读
>
> Selector可以感知通道的事件，它可以选择处理哪些事件。一个线程可以处理多个操作



### NIO 和 BIO 的比较 

>BIO 以流的方式处理数据,而 NIO 以块的方式处理数据,块 I/O 的效率比流 I/O 高很多
>BIO 是阻塞的，NIO 则是非阻塞的
>BIO基于字节流和字符流进行操作，而 NIO 基于 Channel(通道)和 Buffer(缓冲区)进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(选择器)用于监听多个通道的事件（比如：连接请求，数据到达等），因此使用单个线程就可以监听多个客户端通道



### IntBuffer

>```java
>    public static void main(String[] args) {
>        IntBuffer buffer = IntBuffer.allocate(5);
>        buffer.put(10);
>        buffer.put(11);
>        buffer.put(12);
>        //将buffer进行读写切换
>        Buffer flip = buffer.flip();
>        //remaining 美[rɪˈmeɪnɪŋ] 还需处理的
>        while (buffer.hasRemaining()) {
>            System.out.println(buffer.get());
>    }
>      
>    public final Buffer flip() {
>        limit = position;
>        position = 0;
>        mark = -1;
>        return this;
>    }
>```

![image-20210723090537893](../images/image-20210723090537893.png)

### 缓冲区(Buffer)

>缓冲区（Buffer）：缓冲区本质上是一个可以读写数据的内存块，可以理解成是一个容器对象(含数组)，该对象提供了一组方法，可以更轻松地使用内存块，，缓冲区对象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。Channel 提供从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由 Buffer

![image-20210723093921649](../images/image-20210723093921649.png)

### 缓冲区类及其子类

![image-20210723094744144](../images/image-20210723094744144.png)



> public abstract class Buffer {
>     //JDK1.4时，引入的api
>     **<font color='red'>public final int capacity( )</font>**//返回此缓冲区的容量
>     **<font color='red'>public final int position( )//返回此缓冲区的位置</font>**
>    <font color='red'> **public final Buffer position (int newPositio)//设置此缓冲区的位置**</font>
>    <font color='red'> **public final int limit( )//返回此缓冲区的限制**</font>
>     <font color='red'>**public final Buffer limit (int newLimit)//设置此缓冲区的限制**</font>
>     public final Buffer mark( )//在此缓冲区的位置设置标记
>     <font color='red'>**public final Buffer reset( )//将此缓冲区的位置重置为以前标记的位置**</font>
>     <font color='red'>**public final Buffer clear( )//清除此缓冲区, 即将各个标记恢复到初始状态，但是数据并没有真正擦除, 后面操作会覆盖**</font>
>    <font color='red'> **public final Buffer flip( )//反转此缓冲区**</font>
>     public final Buffer rewind( )//重绕此缓冲区
>    <font color='red'> **public final int remaining( )//返回当前位置与限制之间的元素数**</font>
>    <font color='red'> **public final boolean hasRemaining( )//告知在当前位置和限制之间是否有元素**</font>
>     public abstract boolean isReadOnly( );//告知此缓冲区是否为只读缓冲区
>
> ​    //JDK1.6时引入的api
> ​    public abstract boolean hasArray();//告知此缓冲区是否具有可访问的底层实现数组
> ​    public abstract Object array();//返回此缓冲区的底层实现数组
> ​    public abstract int arrayOffset();//返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
> ​    public abstract boolean isDirect();//告知此缓冲区是否为直接缓冲区
> }



>public abstract class ByteBuffer {
>//缓冲区创建相关api
><font color='red'>**public static ByteBuffer allocateDirect(int capacity)//创建直接缓冲区**</font>
><font color='red'>**public static ByteBuffer allocate(int capacity)//设置缓冲区的初始容量**</font>
>public static ByteBuffer wrap(byte[] array)//把一个数组放到缓冲区中使用
>//构造初始化位置offset和上界length的缓冲区
>public static ByteBuffer wrap(byte[] array,int offset, int length)
>//缓存区存取相关API
><font color='red'>**public abstract byte get( );//从当前位置position上get，get之后，position会自动+1**</font>
><font color='red'>**public abstract byte get (int index);//从绝对位置get**</font>
><font color='red'>**public abstract ByteBuffer put (byte b);//从当前位置上添加，put之后，position会自动+1**</font>
><font color='red'>**public abstract ByteBuffer put (int index, byte b);//从绝对位置上put**</font>
> }



### 通道（Channel）

>  NIO 中的通道(Channel)是双向的，可以读操作，也可以写操作。

![image-20210723104022512](../images/image-20210723104022512.png)





![image-20210723104624833](../images/image-20210723104624833.png)

> 1. FileChannel 用于文件的数据读写，DatagramChannel 用于 UDP 的数据读写，
>
> 2. ServerSocketChannel 和 SocketChannel 用于 TCP 的数据读写。
>
> 3. FileChannel主要用来对本地文件进行 IO 操作



#### FileChannel

> public int read(ByteBuffer dst) ，从通道读取数据并放到缓冲区中  <font color='green'> **通道  -> 缓冲区**</font>
> public int write(ByteBuffer src) ，把缓冲区的数据写到通道中          <font color='green'>**缓冲区->通道**</font>
> public long transferFrom(ReadableByteChannel src, long position, long count)，从目标通道中复制数据到当前通道
> public long transferTo(long position, long count, WritableByteChannel target)，把数据从当前通道复制给目标通道



> 使用前面学习后的ByteBuffer(缓冲) 和 FileChannel(通道)， 将 "hello,尚硅谷" 写入到file01.txt 中

```java
    @Test
    public void writeToFile() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("NIOFileChannel01.txt");
        FileChannel outChannel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("你好，NIO".getBytes());
        buffer.flip();
        outChannel.write(buffer);
        outputStream.close();
    }
```

> 读取文件，输出到控制台

```java
    @Test
    public void readFileToConsole() throws IOException {
        File file = new File("NIOFileChannel01.txt");
        FileInputStream inputStream = new FileInputStream(file);
        //获取channel
        FileChannel inputStreamChannel = inputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //创建缓冲区
        //ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
        //把数据读取到buffer
        inputStreamChannel.read(buffer);
        //读写反转
        buffer.flip();
        byte[] array = buffer.array();
//        System.out.println(new String(array));
        System.out.println(new String(array,0,buffer.limit()));

    }
```

> 用一个buffer完成文件copy

```java
    @Test
    public void readFileToFile() throws IOException {
        File fromFile = new File("D:\\LocalDataStore\\netty\\src\\main\\filetmp\\NIOFileChannel01.txt");
        File fileCopy = new File("D:\\LocalDataStore\\netty\\src\\main\\filetmp\\NIOFileChannel01-copy.txt");
        FileInputStream intputStream = new FileInputStream(fromFile);
        FileOutputStream outputStream = new FileOutputStream(fileCopy);
        FileChannel intputStreamChannel = intputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        //创建缓冲区
        //ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
        while (true){
            int read = intputStreamChannel.read(buffer);
            if (read == -1 || read == 0) {
                break;
            }else {
                buffer.flip();
                FileChannel outputStreamchannel = outputStream.getChannel();
                outputStreamchannel.write(buffer);
                buffer.clear();
            }
        }
        outputStream.close();
    }
```



> 把buffer 转成只读buffer   只读buffer写的时候抛出java.nio.ReadOnlyBufferException

```java
    @Test
    public void onlyRead() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }
        byteBuffer.flip();
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
        readOnlyBuffer.put((byte) 1);
    }
```



#### MappedByteBuffer

> 可以让文件只在堆外内存中修改，操作系统不需要copy一次  操作是系统级别，性能比较好

![image-20210723144328988](../images/image-20210723144328988.png)

```java
@Test
public void modifyNonHeap() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
    FileChannel channel = randomAccessFile.getChannel();
    MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
    mappedByteBuffer.put(0,(byte)'a');
    mappedByteBuffer.put(3,(byte)'C');
    randomAccessFile.close();
}
-- 123456789 -> a23C56789
```