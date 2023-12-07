import java.util.List;
import java.util.ArrayList;

public class Day3 extends Day.IntDay {
    public int run1Int(List<String> input) {
        int sum = 0;
        ArrayList<String[]> lines = new ArrayList<String[]>();
        for(String line : input) {
            lines.add(new String[]{line.substring(0, line.length()/2), line.substring(line.length()/2)});
        }

        for(String[] group : lines){
            sum += priorityFind(findSame2(group[0], group[1]));
        }
        return sum;
    }
    public int run2Int(List<String> input) {
        int sum = 0;
        ArrayList<String[]> lines = new ArrayList<String[]>();
        for(int i = 0; i < input.size(); i += 3) {
            lines.add(new String[]{input.get(i), input.get(i + 1), input.get(i + 2)});
        }

        for(String[] group : lines){
            sum += priorityFind(findSame3(group[0], group[1], group[2]));
        }
        return sum;
    }
    public static char findSame2(String a, String b) {
        for(char c : a.toCharArray()) {
            if(b.contains(String.valueOf(c)))
                return c;
        }
        throw new RuntimeException("No same character found");
    }
    public static char findSame3(String a, String b, String c){
        for(char d : a.toCharArray()){
            if(b.contains(String.valueOf(d)) && c.contains(String.valueOf(d)))
                return d;
        }
        throw new RuntimeException("No same character found");
    }
    public static int priorityFind(char c) {
        return switch(c){
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            case 'i' -> 9;
            case 'j' -> 10;
            case 'k' -> 11;
            case 'l' -> 12;
            case 'm' -> 13;
            case 'n' -> 14;
            case 'o' -> 15;
            case 'p' -> 16;
            case 'q' -> 17;
            case 'r' -> 18;
            case 's' -> 19;
            case 't' -> 20;
            case 'u' -> 21;
            case 'v' -> 22;
            case 'w' -> 23;
            case 'x' -> 24;
            case 'y' -> 25;
            case 'z' -> 26;
            case 'A' -> 27;
            case 'B' -> 28;
            case 'C' -> 29;
            case 'D' -> 30;
            case 'E' -> 31;
            case 'F' -> 32;
            case 'G' -> 33;
            case 'H' -> 34;
            case 'I' -> 35;
            case 'J' -> 36;
            case 'K' -> 37;
            case 'L' -> 38;
            case 'M' -> 39;
            case 'N' -> 40;
            case 'O' -> 41;
            case 'P' -> 42;
            case 'Q' -> 43;
            case 'R' -> 44;
            case 'S' -> 45;
            case 'T' -> 46;
            case 'U' -> 47;
            case 'V' -> 48;
            case 'W' -> 49;
            case 'X' -> 50;
            case 'Y' -> 51;
            case 'Z' -> 52;
            default -> 2147483647;
        };
    }
}
