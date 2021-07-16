import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zzh
 * @date 2021年07月16日
 */
public class DemoQueue {

    @Test
    public void method() {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.add("a");
        blockingQueue.add("b");
        blockingQueue.add("c");
        System.out.println(blockingQueue.element());
        blockingQueue.remove();
        System.out.println(blockingQueue.element());
        blockingQueue.remove();
        System.out.println(blockingQueue.element());
        blockingQueue.remove();
        System.out.println(blockingQueue.element());
    }

    @Test
    public void method2() {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        blockingQueue.offer("a");
        blockingQueue.offer("b");
        blockingQueue.offer("c");
        //Retrieves, but does not remove, the head of this queue,
        System.out.println(blockingQueue.peek());
        blockingQueue.poll();
        System.out.println(blockingQueue.peek());
        blockingQueue.poll();
        System.out.println(blockingQueue.peek());
        blockingQueue.poll();
        System.out.println(blockingQueue.peek());
    }

    @Test
    public void method3() throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    String s = UUID.randomUUID().toString();
                    System.out.println(Thread.currentThread().getName()+ " 向queue中添加元素:" +s);
                    blockingQueue.put(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                    String take = blockingQueue.take();
                    System.out.println(Thread.currentThread().getName()+ " 从queue中取出元素:" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t3 = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                    String take = blockingQueue.take();
                    System.out.println(Thread.currentThread().getName()+ " 从queue中取出元素:" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
