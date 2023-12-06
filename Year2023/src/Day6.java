import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://adventofcode.com/2023/day/6">Day 6</a>
 */
public class Day6 implements Day<Long> {
    @Override
    public Long run1() throws Exception {
        List<String> input = Main.getInput(6);
        String[] timesStr = input.get(0).substring(5).trim().split(" +");
        long[] times = Arrays.stream(timesStr).mapToLong(Long::parseLong).toArray();
        String[] distStr = input.get(1).substring(9).trim().split(" +");
        long[] distances = Arrays.stream(distStr).mapToLong(Long::parseLong).toArray();

        return calculate(times, distances);
    }

    @Override
    public Long run2() throws Exception {
        List<String> input = Main.getInput(6);
        long time = Long.parseLong(input.get(0).substring(5).replace(" ", ""));
        long distance = Long.parseLong(input.get(1).substring(9).replace(" ", ""));
        return calculate(new long[] {time}, new long[] {distance});
    }

    private long calculate(long[] times, long[] distances) {
        long result = 1;
        for(int i = 0; i < times.length; i++) {
            long time = times[i];
            long distance = distances[i];
            int count = 0;
            for(int j = 1; j < time; j++) {
                if(j * (time - j) > distance) {
                    count++;
                }
            }
            result *= count;
        }
        return result;
    }
}
