import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
    启动线程的方式你能说出几种？
 */
public class T03 {

    public static void main(String[] args) {
        new Thread().start();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread(()->{}).start();

        new Thread(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        })).start();

        new Thread(new FutureTask<String>(()->{return "";})).start();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{});
        executorService.shutdown();

    }
}
