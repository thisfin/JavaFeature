package win.sourcecode.feature.net;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTest {
    @Test
    public void client()
            throws IOException {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout(3000);

            String message = "Hello, World!";
            byte[] bytes = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("127.0.0.1"), 7777);
            DatagramPacket receivedPacket = new DatagramPacket(new byte[bytes.length], bytes.length);

            boolean receivedResponse = false;
            do {
                datagramSocket.send(sendPacket);

                datagramSocket.receive(receivedPacket);
                if (receivedPacket.getAddress().equals(InetAddress.getByName("127.0.0.1"))) {
                    receivedResponse = true;
                }
            } while (!receivedResponse);

            if (receivedResponse) {
                System.out.println("Received: " + new String(receivedPacket.getData()));
            }
        }
    }

    @Test
    public void server()
            throws IOException {
        final int echoMax = 255;

        try (DatagramSocket datagramSocket = new DatagramSocket(7777)) {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[echoMax], echoMax);

            for (; ; ) {
                datagramSocket.receive(datagramPacket);
                System.out.println("Handling client at " + datagramPacket.getAddress().getHostAddress() + " on port " + datagramPacket.getPort());
                datagramSocket.send(datagramPacket);
                datagramPacket.setLength(echoMax);
            }
        }
    }
}
