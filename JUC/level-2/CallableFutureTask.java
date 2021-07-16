import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author zzh
 * @date 2021年07月16日
 */
public class CallableFutureTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> futureTask01 = new FutureTask(()->{
                System.out.println("开始执行task01");
                TimeUnit.SECONDS.sleep(1);
                return 123;
        });

        FutureTask<Integer> futureTask02 = new FutureTask(()->{
            System.out.println("开始执行task02");
            TimeUnit.SECONDS.sleep(3);
            return 123;
        });

        FutureTask<Integer> futureTask03 = new FutureTask(()->{
            System.out.println("开始执行task03");
            TimeUnit.SECONDS.sleep(4);
            return 123;
        });

        new Thread(futureTask01).start();
        new Thread(futureTask02).start();
        new Thread(futureTask03).start();

        System.out.println(futureTask01.get());
        System.out.println(futureTask02.get());
        System.out.println(futureTask03.get());
    }
}
