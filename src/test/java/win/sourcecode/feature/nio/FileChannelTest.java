package win.sourcecode.feature.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    /*
     * capacity 内存块大小
     * position 为当前位置
     * 写模式: 初始为 0, 每次写后都会移动. 最大值为 capacity - 1.
     * 读模式: 写模式切换为读模式时, 重置为 0, 每次读取后都会移动
     * limit
     * 写模式: 代表最多能写多少数据, 等于 capacity
     * 读模式: 代表最多能读取多少数据. 切换到读模式时, 会被设置成写模式下的 position 值
     *
     * mark 标记当前 position
     * reset 恢复 mark 的位置
     */
    @Test
    public void simple()
            throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile("/etc/hosts", "r")) { // jdk7 try resource
            FileChannel fileChannel = randomAccessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(48); // 缓冲区大小

            //            byte[] bytes = new byte[10]; // 另一种缓冲区声明
            //            byteBuffer = ByteBuffer.wrap(bytes);

            for (int size = fileChannel.read(byteBuffer); size != -1; size = fileChannel.read(byteBuffer)) { // 结尾为 -1
                System.out.println("read: " + size);

                byteBuffer.flip(); // 反转缓冲区 写模式->读模式
                while (byteBuffer.hasRemaining()) { // position < limit
                    System.out.println((char)byteBuffer.get());
                }

                byteBuffer.clear(); // 清空缓冲区. compact 只清除已经读取的, 未读数据复制至 buffer 起始处, position 为最后一个未读元素之后
            }

            randomAccessFile.close();
        }
    }

    @Test
    public void slice() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10); // 设置初始值
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte)i);
        }

        byteBuffer.position(3); // 取分片
        byteBuffer.limit(7);
        ByteBuffer slice = byteBuffer.slice();
        for (int i = 0; i < slice.capacity(); i++) { // 修改分片的值
            slice.put(i, (byte)(slice.get(i) * 10));
        }

        byteBuffer.position(0); // 主缓冲复原
        byteBuffer.limit(byteBuffer.capacity());
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }
    }

    @Test
    public void transfer()
            throws IOException {
        try (RandomAccessFile rFile = new RandomAccessFile("", "rw"); RandomAccessFile wFile = new RandomAccessFile("", "rw")) {
            FileChannel rFileChannel = rFile.getChannel();
            FileChannel wFileChannel = wFile.getChannel();

            long position = 0;
            long count = rFileChannel.size();
            wFileChannel.transferFrom(rFileChannel, position, count); // 2 选 1 即可
            //            rFileChannel.transferTo(position, count, wFileChannel);
        }
    }
}
