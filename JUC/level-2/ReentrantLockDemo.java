import org.junit.Test;

import java.util.PrimitiveIterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zzh
 * @date 2021年07月16日
 */
public class ReentrantLockDemo {
    private volatile int num = 0;
    ReentrantLock lock = new ReentrantLock();
    Condition conditionA = lock.newCondition();
    Condition conditionB = lock.newCondition();
    Condition conditionC = lock.newCondition();

    public void printA() {
        try {
            lock.lock();
            while (num != 0) {
                conditionA.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("A");
            }
            num= 1;
            conditionB.signal();
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB()  {
        try {
            lock.lock();
            while (num != 1) {
                conditionB.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("B");
            }
            num= 2;
            conditionC.signal();
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC()  {
        try {
            lock.lock();
            while (num != 2) {
                conditionC.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("C");
            }
            num= 0;
            conditionA.signal();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        new Thread(()->demo.printA()).start();
        new Thread(()->demo.printB()).start();
        new Thread(()->demo.printC()).start();
    }
}
