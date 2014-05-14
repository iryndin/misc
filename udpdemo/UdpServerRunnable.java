import java.io.IOException;
import java.net.*;

public class UdpServerRunnable implements Runnable {

    private final InetSocketAddress inetSocketAddress;
    private final static int BUFSIZE = 8192;

    public UdpServerRunnable(String hostname, int port) {
        this.inetSocketAddress = new InetSocketAddress(hostname, port);
    }

    @Override
    public void run() {
        try {
            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void runServer() throws IOException {
        System.out.println("Start udp server");
        try (DatagramSocket serverSocket = new DatagramSocket(inetSocketAddress)) {
            byte[] receiveData = new byte[BUFSIZE];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                byte[] data = receivePacket.getData();
                String sentence = new String(data);

                System.out.println("RECEIVED: " + sentence);

                // now send data back to sender

                InetAddress inetAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                String capitalizedSentence = sentence.toUpperCase();
                byte[] sendData = capitalizedSentence.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port);
                serverSocket.send(sendPacket);
            }
        }
    }
}
