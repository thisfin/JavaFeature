package win.sourcecode.feature;

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
    }
}
