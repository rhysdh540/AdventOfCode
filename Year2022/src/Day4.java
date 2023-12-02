import java.util.List;
import java.util.ArrayList;

public class Day4 implements Day.IntDay {
    public int run1Int() throws Exception {
        List<String> input = Main.getInput(4);
        ArrayList<Range[]> ranges = new ArrayList<Range[]>();
        for(String line : input) {
            String[] split = line.split(",");
            ranges.add(new Range[] {new Range(split[0]), new Range(split[1])});
        }
        int count = 0;
        for(Range[] range : ranges)
            if(range[0].contains(range[1]) || range[1].contains(range[0])) count++;
        return count;
    }
    public int run2Int() throws Exception {
        List<String> input = Main.getInput(4);
        ArrayList<Range[]> ranges = new ArrayList<Range[]>();
        for(String line : input) {
            String[] split = line.split(",");
            ranges.add(new Range[] {new Range(split[0]), new Range(split[1])});
        }
        int count = 0;
        for(Range[] range : ranges)
            if(range[0].overlaps(range[1])) count++;
        return count;
    }

    static class Range {
        int min;
        int max;
        public Range(String in){
            min = Integer.parseInt(in.split("-")[0]);
            max = Integer.parseInt(in.split("-")[1]);
        }
        public boolean contains(int num) {
            return num >= min && num <= max;
        }
        public boolean contains(Range range) {
            return contains(range.min) && contains(range.max);
        }
        public boolean overlaps(Range range) {
            return contains(range.min) || contains(range.max) || range.contains(this.min) || range.contains(this.max);
        }
    }
}
