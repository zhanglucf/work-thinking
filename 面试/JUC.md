### Synchronized底层原理

### Synchronized锁升级过程

### ReentrantLock 与Synchronized

`CountDownLatch`   `CyclicBarrier`   `SemaPhore`  

`ArrayBlockingQueue`   `LinkedBlockingQueue`   `SynchronousQueue` 



### Executors返回的线程池对象的弊端如下：

      FixedThreadPool 和 SingleThreadPool：
    
      允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
    
      CachedThreadPool 和 ScheduledThreadPool :
    
      允许创建的线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。


### 异步任务 失败了怎么办？











MySQL根据配置文件会限制Server接受的数据包大小。

有时候大的插入和更新会受 max_allowed_packet 参数限制，导致写入或者更新失败。(比方说导入数据库,数据表)



![image-20210720134818132](../images/image-20210720134818132.png)











