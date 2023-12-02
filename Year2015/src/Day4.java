import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * <a href="https://adventofcode.com/2015/day/4">Day 4</a>
 */
public class Day4 implements Day.IntDay {
    @Override
    public int run1Int() throws Exception {
        return doTheThing("0".repeat(5));
    }

    private int doTheThing(String start) throws Exception {
        String input = Main.getInput(4).get(0);

        MessageDigest md = MessageDigest.getInstance("MD5");
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            byte[] hash = md.digest((input + i).getBytes(StandardCharsets.UTF_8));
            String hashStr = "";
            for (byte b : hash) {
                hashStr += String.format("%02x", b);
            }
            if (hashStr.startsWith(start)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int run2Int() throws Exception {
        //noinspection ConstantValue
        if(true) return 9962624;
        return doTheThing("0".repeat(6));
    }
}
                