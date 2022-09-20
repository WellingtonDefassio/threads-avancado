import java.io.PrintStream;

public class ComandoC2 implements Runnable{
    private PrintStream saidaCliente;

    public ComandoC2(PrintStream saidaCliente) {
        this.saidaCliente = saidaCliente;
    }

    @Override
    public void run() {
        System.out.println("Executando comando c2");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("excption comando c2");
//        saidaCliente.println("comando c2 concluido com sucesso");

    }
}
