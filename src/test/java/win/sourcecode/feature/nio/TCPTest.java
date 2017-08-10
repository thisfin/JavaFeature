package win.sourcecode.feature.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TCPTest {
    @Test
    public void client()
            throws IOException {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);

            if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 7777))) {
                while (!socketChannel.finishConnect()) {
                    System.out.print(".");
                }
            }

            String message = "Hello, World!";
            byte[] bytes = message.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);
            ByteBuffer readBuffer = ByteBuffer.allocate(bytes.length);

            int totalByteReceived = 0;
            int bytesRecive = 0;

            while (totalByteReceived < bytes.length) {
                if (writeBuffer.hasRemaining()) {
                    socketChannel.write(writeBuffer);
                }

                if ((bytesRecive = socketChannel.read(readBuffer)) == -1) {
                    break;
                }
                totalByteReceived += bytesRecive;
                System.out.print(".");
            }

            System.out.println("Received: " + new String(readBuffer.array(), 0, totalByteReceived));
        }
    }

    @Test
    public void server()
            throws IOException {
        final int timeout = 3000;

        try (Selector selector = Selector.open()) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(7777));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            for (; ; ) {
                if (selector.select(timeout) == 0) {
                    System.out.print(".");
                    continue;
                }

                for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        handleAccept(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        handleRead(selectionKey);
                    }
                    if (selectionKey.isValid() && selectionKey.isWritable()) {
                        handleWrite(selectionKey);
                    }
                    iterator.remove();
                }
            }
        }
    }

    private void handleAccept(SelectionKey selectionKey)
            throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel)selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(256));
    }

    private void handleRead(SelectionKey selectionKey)
            throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
        long bytesRead = socketChannel.read(buffer);
        if (bytesRead == -1) {
            socketChannel.close();
        } else {
            selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    private void handleWrite(SelectionKey selectionKey)
            throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
        buffer.flip();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
        buffer.compact();
    }
}
