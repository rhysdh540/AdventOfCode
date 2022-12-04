package four;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class four {
    public static int run1() {
        Scanner sc = null;
        try{
            sc = new Scanner(new File("src/four/input.txt"));
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
        ArrayList<Range[]> ranges = new ArrayList<Range[]>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] split = line.split(",");
            ranges.add(new Range[] {new Range(split[0]), new Range(split[1])});
        }
        int count = 0;
        for(Range[] range : ranges)
            if(range[0].contains(range[1]) || range[1].contains(range[0])) count++;
        return count;
    }
    public static int run2() {
        Scanner sc = null;
        try{
            sc = new Scanner(new File("src/four/input.txt"));
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
        ArrayList<Range[]> ranges = new ArrayList<Range[]>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] split = line.split(",");
            ranges.add(new Range[] {new Range(split[0]), new Range(split[1])});
        }
        int count = 0;
        for(Range[] range : ranges)
            if(range[0].overlaps(range[1])) count++;
        return count;
    }
}
