import java.io.PrintStream;
import java.util.concurrent.*;

public class JuntaResultadoFuture implements Callable<Void> {
    private final Future<String> futureWS;
    private final Future<String> futureBanco;
    private final PrintStream saidaCliente;

    public JuntaResultadoFuture(Future<String> futureWS, Future<String> futureBanco, PrintStream saidaCliente) {

        this.futureWS = futureWS;
        this.futureBanco = futureBanco;
        this.saidaCliente = saidaCliente;
    }

    @Override
    public Void call() {

        System.out.println("Aguardando resultado do future WS e Banco");
        try {
            String numeroMagico = this.futureWS.get(15, TimeUnit.SECONDS);

            String numeroMagico2 = this.futureBanco.get(15, TimeUnit.SECONDS);

            this.saidaCliente.println("Resultado comando C2 " + numeroMagico2 + " " + numeroMagico);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            this.saidaCliente.println("TIMEOUT EXEECUTION");
            this.futureWS.cancel(true);
            this.futureBanco.cancel(true);
        } catch (TimeoutException e) {
            this.saidaCliente.println("TIMEOUT EXEECUTION");
            this.futureWS.cancel(true);
            this.futureBanco.cancel(true);
            throw new RuntimeException(e);
        }

        System.out.println("fim da execução  JuntaResultado");

        return null;
    }
}
