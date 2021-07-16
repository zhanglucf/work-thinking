import java.util.concurrent.locks.LockSupport;

/**
 * @author zzh
 * @date 2021年07月16日
 */
public class T04 {

    public static void main(String[] args) {
//testSleep();
testYield();
//        testJoin();
    }
    /*
    Sleep,意思就是睡眠，
    当前线程暂停一段时间让给别的线程去运行。Sleep是怎么复活的？由
    你的睡眠时间而定，等睡眠到规定的时间自动复活
    */
    static void testSleep() {
        new Thread(()->{
            for(int i=0; i<100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /*
    Yield,
    Running -> Ready，当前线让出CPU，接下来当前线程同样还会参与CPU执行的竞争，
    所以yield的意思是我让出一下CPU，后面你们能不能抢到那我不管
    */
    static void testYield() {
        new Thread(()->{
            for(int i=0; i<100; i++) {
                System.out.println(Thread.currentThread().getName()+ " A" + i);
                if(i%10 == 0) Thread.yield();
            }
        },"t1").start();
        new Thread(()->{
            for(int i=0; i<100; i++) {
                System.out.println(Thread.currentThread().getName()+ " B" + i);
                if(i%10 == 0) Thread.yield();
            }
        },"t2").start();
    }
    /*join
    t1和t2两个线程，在t1的某个点上调用了t2.join,它会跑到t2去运行，t1等待t2运行完毕继续t1运行（自己join自己没有意义）
    */
    static void testJoin() {
        Thread t1 = new Thread(()->{
            for(int i=0; i<10; i++) {
                System.out.println(Thread.currentThread().getName()+ " A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");
        Thread t2 = new Thread(()->{
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0; i<10; i++) {
                System.out.println(Thread.currentThread().getName()+ " A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2");
        t1.start();
        t2.start();
    }
}
