import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable {
    private ExecutorService threadPool;
    private BlockingQueue<String> filaComandos;
    private final Socket socket;
    private final ServidorTarefas servidor;

    public DistribuirTarefas(ExecutorService threadPool, BlockingQueue<String> filaComandos, Socket socket, ServidorTarefas servidor) {
        this.threadPool = threadPool;
        this.filaComandos = filaComandos;
        this.socket = socket;
        this.servidor = servidor;

    }

    @Override
    public void run() {
        try {

            System.out.println("distribuindo tarefas para " + socket);

            Scanner entradaCliente = new Scanner(socket.getInputStream());
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

            while (entradaCliente.hasNext()) {
                String comando = entradaCliente.nextLine();
                System.out.println("comando recebido");
                switch (comando) {
                    case "c1": {
                        System.out.println("Confirmando comando c1");
                        ComandoC1 c1 = new ComandoC1(saidaCliente);
                        threadPool.execute(c1);
                        break;
                    }
                    case "c2": {
                        System.out.println("Confirmando comando c2");
                        ComandoC2ChamaWS c2 = new ComandoC2ChamaWS(saidaCliente);
                        ComandoC2AcessaBanco cBanco = new ComandoC2AcessaBanco(saidaCliente);
                        Future<String> futureWS = threadPool.submit(c2);
                        Future<String> futureBanco = threadPool.submit(cBanco);

                       this.threadPool.submit(new JuntaResultadoFuture(futureWS,futureBanco,saidaCliente));
                        break;
                    }
                    case "c3": {
                        this.filaComandos.put(comando); // bloqueia
                        saidaCliente.println("Comando c3 adicionado na fila");
                        break;
                    }
                    case "exit": {
                        saidaCliente.println("Desligando servidor");
                        servidor.parar();
                        break;
                    }
                    default: {
                        saidaCliente.println("comando n??o encontrado");
                    }
                }

                System.out.println(comando);
            }
            entradaCliente.close();
            saidaCliente.close();

            System.out.println("tarefa concluida do socket" + socket);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
