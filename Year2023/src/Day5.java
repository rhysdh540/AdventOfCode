import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * <a href="https://adventofcode.com/2023/day/5">Day 5</a>
 */
public class Day5 implements Day<Long> {
    @Override
    public Long run1() throws Exception {
        List<List<String>> input = getParsedInput();
        List<Long> seeds = getSeeds(input);
        List<Map> maps = getMaps(input);

        long min = Long.MAX_VALUE;
        for(long seed : seeds) {
            long value = seed;
            for(Map map : maps) {
                value = map.getValue(value);
            }
            if(min > value)
                min = value;
        }

        return min;
    }
    
    @Override
    public Long run2() throws Exception {
        List<List<String>> input = getParsedInput();
        List<Map> maps = getMaps(input);
        int[] counter = {0};
        Collection<List<Long>> ranges = getSeeds(input).stream()
                .collect(Collectors.groupingBy(it -> counter[0]++ / 2)).values();

        Collections.reverse(maps);

        //get the largest value from the last map
        PrimitiveIterator.OfLong itr = maps.get(0).getMaxRange();

        // send them all backwards to find their original values
        while(itr.hasNext()) {
            long next = itr.nextLong();
            long changed = next;

            for(Map map : maps) {
                changed = map.getKey(changed);
            }

            // if any of the original values are in the ranges, we found the answer
            for(List<Long> range : ranges) {
                if(range.get(0) <= changed && changed < range.get(0) + range.get(1)) {
                    return next;
                }
            }
        }
        throw new AssertionError("No solution found");
    }

    private List<List<String>> getParsedInput() throws Exception {
        List<List<String>> parsedInput = new ArrayList<>();
        List<String> current = new ArrayList<>();
        for(String line : Main.getInput(5)) {
            if(line.isEmpty()) {
                parsedInput.add(current);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }
        if(!current.isEmpty())
            parsedInput.add(current);
        return parsedInput;
    }

    private List<Long> getSeeds(List<List<String>> input) {
        return Arrays.stream(input.get(0).get(0).substring(7).split(" "))
                .map(Long::parseLong)
                .toList();
    }

    private List<Map> getMaps(List<List<String>> input) {
        return input.stream().skip(1).map(Map::create).collect(Collectors.toList());
    }
}

record Map(List<Entry> entries) {
    static Map create(List<String> lines) {
        List<Entry> entries = lines.stream().skip(1).map(Entry::create).collect(Collectors.toList());
        return new Map(entries);
    }

    long getValue(long key) {
        for(Entry entry : entries) {
            long sourceLow = entry.source;
            long sourceHigh = entry.source + entry.range - 1;
            if(sourceLow <= key && key <= sourceHigh) {
                long distance = key - sourceLow;
                return entry.destination + distance;
            }
        }

        return key;
    }

    long getKey(long value) {
        for(Entry entry : entries) {
            long destinationLow = entry.destination;
            long destinationHigh = destinationLow + entry.range - 1;
            if(destinationLow <= value && value <= destinationHigh) {
                long distance = value - destinationLow;
                return entry.source + distance;
            }
        }

        return value;
    }

    PrimitiveIterator.OfLong getMaxRange() {
        long highestDestination = entries.stream()
                .map(entry -> entry.destination + entry.range).
                max(Long::compareTo)
                .orElseThrow();
        return LongStream.range(0, highestDestination).iterator();
    }

    record Entry(long destination, long source, long range) {
        static Entry create(String line) {
            String[] parts = line.split(" ");
            long destination = Long.parseLong(parts[0]),
                      source = Long.parseLong(parts[1]),
                       range = Long.parseLong(parts[2]);
            return new Entry(destination, source, range);
        }
    }
}
