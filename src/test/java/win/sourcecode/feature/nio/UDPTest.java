package win.sourcecode.feature.nio;

import lombok.Data;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UDPTest {
    @Test
    public void server()
            throws IOException {
        try (Selector selector = Selector.open(); DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.configureBlocking(false);
            datagramChannel.socket().bind(new InetSocketAddress(7777));
            datagramChannel.register(selector, SelectionKey.OP_READ, new ClientRecord());

            for (; ; ) {
                if (selector.select(3000) == 0) {
                    System.out.print(".");
                    continue;
                }

                for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                    SelectionKey selectionKey = iterator.next();

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

    private void handleRead(SelectionKey selectionKey)
            throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel)selectionKey.channel();
        ClientRecord clientRecord = (ClientRecord)selectionKey.attachment();

        clientRecord.getByteBuffer().clear();
        clientRecord.setSocketAddress(datagramChannel.receive(clientRecord.getByteBuffer()));
        if (clientRecord.getSocketAddress() != null) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void handleWrite(SelectionKey selectionKey)
            throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel)selectionKey.channel();
        ClientRecord clientRecord = (ClientRecord)selectionKey.attachment();

        clientRecord.getByteBuffer().flip();
        int bytesSent = datagramChannel.send(clientRecord.getByteBuffer(), clientRecord.getSocketAddress());
        if (bytesSent != 0) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    @Data
    private class ClientRecord {
        private SocketAddress socketAddress;
        private ByteBuffer byteBuffer = ByteBuffer.allocate(256);
    }
}
