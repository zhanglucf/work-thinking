import java.util.concurrent.TimeUnit;

public class T06 implements Runnable {
    private volatile int count = 100;
    public /*synchronized*/ void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " count = " + count);
        count--;
    }
    public static void main(String[] args) {
        T06 t = new T06();
        for(int i=0; i<100; i++) {
            new Thread(t, "THREAD" + i).start();
        }
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.count);
    }
}
