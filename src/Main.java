import one.one;
import two.two;
import three.three;
import four.four;
import five.five;
import java.io.PrintStream;
public class Main {
    static PrintStream so = System.out;
    public static void main(String[] args) throws Exception {
        so.println("Day 1:\nPart 1: " + one.run1() + "\nPart 2: " + one.run2());
        so.println("\nDay 2:\nPart 1: " + two.run1() + "\nPart 2: " + two.run2());
        so.println("\nDay 3:\nPart 1: " + three.run1() + "\nPart 2: " + three.run2());
        so.println("\nDay 4:\nPart 1: " + four.run1() + "\nPart 2: " + four.run2());
        so.println("\nDay 5:\nPart 1: " + five.run1() + "\nPart 2: " + five.run2());
    }
}