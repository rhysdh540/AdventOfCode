package four;

public class Range {
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
