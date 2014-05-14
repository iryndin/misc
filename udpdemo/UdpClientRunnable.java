import java.io.IOException;
import java.net.*;

public class UdpClientRunnable implements Runnable {


    private final InetAddress destAddress;
    private final int port;

    public UdpClientRunnable(String hostname, int port) {
        try {
            this.destAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        this.port = port;
    }

    @Override
    public void run() {
        try {
            runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runClient() throws IOException {
        System.out.println("Start udp client");

        String sentence = "abcdef";

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            byte[] sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destAddress, port);
            clientSocket.send(sendPacket);
            System.out.println("client sent data");

            byte[] receiveData = new byte[1024 * 64];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
        }
        System.out.println("client closed");
    }
}
