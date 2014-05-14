import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final String host = "localhost";
    static final int port = 7788;


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new UdpServerRunnable(host, port));
        executorService.submit(new UdpClientRunnable(host, port));
    }
}
