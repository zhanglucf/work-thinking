import java.util.concurrent.Callable;

/**

 创建线程的几种方式
    1. 继续Thread类
    2. 实现Runnable接口
    3. 实现Callable接口

 */
public class T02 {

    static class A extends Thread{
        @Override
        public void run() {
            System.out.println("A");
        }
    }

    static class B implements Runnable{

        @Override
        public void run() {
            System.out.println("B");
        }
    }

    static class C implements Callable<String>{

        @Override
        public String call() throws Exception {
            return "C";
        }
    }
}
