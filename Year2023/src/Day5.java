import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.LongStream;

/**
 * <a href="https://adventofcode.com/2023/day/5">Day 5</a>
 */
public class Day5 extends Day<Long> {
	@Override
	public Long run1(List<String> a) {
		List<List<String>> input = getParsedInput();

		long best = Integer.MAX_VALUE;
		for(Long l : getSeeds(input)) {
			for(Map map : getMaps(input)) {
				l = map.getValue(l);
			}
			if(l < best) {
				best = l;
			}
		}
		return best;
	}

	@Override
	public Long run2(List<String> a) {
		//noinspection ConstantConditions
		List<List<String>> input = getParsedInput();
		Map[] maps = getMaps(input);

		List<Long> seeds = getSeeds(input);
		int size = seeds.size();
		long[][] ranges = new long[size / 2][];
		for (int i = 0; i < size; i += 2) {
			long low = seeds.get(i);
			long high = seeds.get(i + 1);
			ranges[i / 2] = new long[]{low, high};
		}

		Utils.reverse(maps); // reverse the maps so we can go backwards

		// get the largest value from the last map
		PrimitiveIterator.OfLong itr = maps[0].getMaxRange();

		// send them all backwards to find their original values
		while(itr.hasNext()) {
			long next = itr.nextLong();
			long changed = next;

			for(Map map : maps) {
				changed = map.getKey(changed);
			}

			// if any of the original values are in the ranges, we found the answer
			for(long[] range : ranges) {
				long low = range[0];
				long high = low + range[1];
				if(low <= changed && changed < high) {
					return next;
				}
			}
		}
		throw new AssertionError("No solution found");
	}

	private List<List<String>> getParsedInput() {
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
		String[] seeds = Utils.SPACES.split(input.get(0).get(0).substring(7));
		List<Long> list = new ArrayList<>();
		for(String seed : seeds) {
			list.add(Utils.fastParseLong(seed));
		}
		return list;
	}

	private Map[] getMaps(List<List<String>> input) {
		input = new ArrayList<>(input);
		input.remove(0);
		Map[] maps = new Map[input.size()];
		for(int i = 0; i < input.size(); i++) {
			maps[i] = Map.create(input.get(i));
		}
		return maps;
	}

	record Map(Entry[] entries) {
		static Map create(List<String> lines) {
			Entry[] entries = new Entry[lines.size() - 1];
			for(int i = 1; i < lines.size(); i++) {
				entries[i - 1] = Entry.create(lines.get(i));
			}
			// sort so we can binary search later
			Arrays.sort(entries, Comparator.comparingLong(o -> o.destination)); // faster than Entry::destination for some reason
			return new Map(entries);
		}

		// runs a value through the map
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

		// runs a value backwards through the map to find the original value
		// uses binary search for the SPEED
		long getKey(long value) {
			int low = 0;
			int high = entries.length - 1;

			while(low <= high) {
				int mid = (low + high) >>> 1;
				Entry entry = entries[mid];
				long destinationLow = entry.destination;
				long destinationHigh = destinationLow + entry.range - 1;

				if(destinationHigh < value) {
					low = mid + 1;
				} else if(destinationLow > value) {
					high = mid - 1;
				} else {
					return entry.source + value - destinationLow;
				}
			}

			return value;
		}

		// finds the range with the highest destination
		PrimitiveIterator.OfLong getMaxRange() {
			long best = 0;
			for(Entry entry : entries) {
				long l = entry.destination + entry.range;
				if(l > best) {
					best = l;
				}
			}
			long highest = best;
			return LongStream.range(0, highest).iterator();
		}

		record Entry(long destination, long source, long range) {
			static Entry create(String line) {
				String[] parts = line.split(" ");
				long destination = Utils.fastParseLong(parts[0]),
						source = Utils.fastParseLong(parts[1]),
						range = Utils.fastParseLong(parts[2]);
				return new Entry(destination, source, range);
			}
		}
	}
}
