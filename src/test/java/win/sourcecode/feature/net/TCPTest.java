package win.sourcecode.feature.net;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPTest {
    @Test
    public void client()
            throws IOException {
        String message = "Hello, World!";
        byte[] bytes = message.getBytes();
        try (Socket socket = new Socket("127.0.0.1", 7777); InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write(message.getBytes());

            int totalByteReceived = 0;
            int bytesRecive;

            while (totalByteReceived < bytes.length) {
                if ((bytesRecive = inputStream.read(bytes, totalByteReceived, bytes.length - totalByteReceived)) == -1) {
                    break;
                }
                totalByteReceived += bytesRecive;
            }
            System.out.println(new String(bytes));
        }
    }

    @Test
    public void server()
            throws IOException {
        final int bufferSize = 32;

        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            int receivedSize;
            byte[] bytes = new byte[bufferSize];

            for (; ; ) {
                Socket clientSocket = serverSocket.accept();

                SocketAddress socketAddress = clientSocket.getRemoteSocketAddress();
                System.out.println("Handling client at " + socketAddress);

                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                while ((receivedSize = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, receivedSize);
                }

                break;
            }
        }
    }
}
