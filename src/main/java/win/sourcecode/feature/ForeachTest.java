package win.sourcecode.feature;

import java.util.stream.IntStream;

public class ForeachTest {
    public static void main(String[] args) {
        long value1 = 0;
        long s = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++){

        }
        System.out.println(System.currentTimeMillis() - s);

        final long[] value2 = {0};
        IntStream is = IntStream.range(0, 10000000);
        s = System.currentTimeMillis();
        is.forEach(i -> {});
        System.out.println(System.currentTimeMillis() - s);
    }
}
