import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

    private final ExecutorService threadPool;
    private final ServerSocket servidor;
    private AtomicBoolean estaRodando;

    public ServidorTarefas() throws IOException {
        System.out.println("-- iniciando servidor --");
        this.servidor = new ServerSocket(12345);
        this.threadPool = Executors.newFixedThreadPool(4, new FabricaDeThreads());
        this.estaRodando = new AtomicBoolean(true);
    }

    public void parar() throws IOException {
        System.out.println("ENTROU NA PARAR?");
        this.estaRodando.set(false);
        servidor.close();
        threadPool.shutdown();
    }

    public void rodar() throws IOException {
        while (this.estaRodando.get()) {
            try {
                Socket socket = servidor.accept();
                System.out.println("Aceitando novo cliente na porta " + socket.getPort());

                DistribuirTarefas distribuirTarefa = new DistribuirTarefas(threadPool, socket, this);
                threadPool.execute(distribuirTarefa);
            } catch (SocketException e) {
                System.out.println("socketExceptionm Extá rodando? " + this.estaRodando);
            }
        }

    }
    public static void main(String[] args) throws Exception {
        ServidorTarefas servidorTarefas = new ServidorTarefas();
        servidorTarefas.rodar();
        servidorTarefas.parar();
    }
}


