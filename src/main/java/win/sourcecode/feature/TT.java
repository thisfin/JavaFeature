package win.sourcecode.feature;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Stream;

public class TT {
    public static void main(String[] args) {
        {
            String s1 = new String("123");
            String s2 = s1;
            s1 = new String("234");
            System.out.println(s2);
        }

        {
            String s1 = "123";
            String s2 = s1;
            s1 = "234";
            System.out.println(s2);
        }

        {
            Bean bean1 = new Bean("123");
            Bean bean2 = bean1;
            bean1 = new Bean("234");
            System.out.println(bean2.name);
        }

        Stream.of(1, 2, 3).filter(i -> !(i == 2)).forEach(i -> System.out.println(i));
    }

    @Data
    @AllArgsConstructor
    static class Bean {
        private String name;
    }
}
