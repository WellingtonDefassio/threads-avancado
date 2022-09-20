import java.util.concurrent.ThreadFactory;

public class FabricaDeThreads implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(new TratadorExecao());
        return thread;
    }
}
