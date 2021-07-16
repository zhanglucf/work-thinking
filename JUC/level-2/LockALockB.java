import java.util.concurrent.TimeUnit;

/**

    死锁案例演示

 */

class AB implements Runnable {

    private String lockA;
    private String lockB;

    public AB(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {

        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + " 得到了锁" + lockA);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 企图得到锁" + lockB);
            synchronized (lockB) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class LockALockB {
    public static void main(String[] args) {
        new Thread(new AB("lockA","lockB")).start();
        new Thread(new AB("lockB","lockA")).start();
    }
}
