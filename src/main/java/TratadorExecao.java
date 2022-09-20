public class TratadorExecao implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Deu erro na thread " + t.getName() + ", " + e.getMessage());
    }
}
