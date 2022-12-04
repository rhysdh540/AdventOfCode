package four;

public class Range {
    int min;
    int max;
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }
    public Range(String in){
        String[] split = in.split("-");
        this.min = Integer.parseInt(split[0]);
        this.max = Integer.parseInt(split[1]);
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
