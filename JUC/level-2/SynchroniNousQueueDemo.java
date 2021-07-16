import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zzh
 * @date 2021年07月16日
 */
public class SynchroniNousQueueDemo {


    @Test
    public void method() throws InterruptedException {
        BlockingQueue queue = new SynchronousQueue<>();

        Thread thread = new Thread(() -> {
            try {
                System.out.println("放入a");
                queue.put("a");
                System.out.println("放入b");
                queue.put("b");
                System.out.println("放入c");
                queue.put("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("取出 "+ queue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println("取出 "+ queue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println("取出 "+ queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
    }
}
