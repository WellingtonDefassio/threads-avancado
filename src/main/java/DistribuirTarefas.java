import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {
    private final Socket socket;
    private final ServidorTarefas servidor;

    public DistribuirTarefas(Socket socket, ServidorTarefas servidor) {
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
                        saidaCliente.println("Confirmação comando c1");
                        break;
                    }
                    case "c2": {
                        saidaCliente.println("Confirmação comando c2");
                        break;
                    }
                    case "exit": {
                        saidaCliente.println("Desligando servidor");
                        servidor.parar();
                        break;
                    }
                    default: {
                        saidaCliente.println("comando não encontrado");
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
