
public class T05 {

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(this.getState());
            for(int i=0; i<10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }
    }
    public static void main(String[] args) {
        Thread t = new MyThread();
//怎么样得到这个线程的状态呢？就是通过getState()这个方法
        System.out.println(t.getState());//他是一个new状态
        t.start();//到这start完了之后呢是Runnable的状态
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//t.join() 主线程会等待t线程任务执行结束，结束了线程t是一个Timenated状态
        System.out.println(t.getState());
    }
}
