import aoc.Day.IntDay;
import util.Utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
* <a href="https://adventofcode.com/2023/day/15">Day 15</a>
*/
public class Day15 extends IntDay {
	@Override
	public int run1Int(List<String> a) {
		String[] input = Utils.split(a.get(0), ',');
		int sum = 0;
		for(String s : input) {
			sum += HASH(s);
		}
		return sum;
	}

	@Override
	public int run2Int(List<String> a) {
		String[] input = Utils.split(a.get(0), ',');

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer>[] boxes = new LinkedHashMap[256];
		Arrays.setAll(boxes, i -> new LinkedHashMap<>());

		for(String insn : input) {
			boolean remove = insn.contains("-");
			int index = remove ? insn.indexOf("-") : insn.indexOf("=");
			String key = insn.substring(0, index);

			LinkedHashMap<String, Integer> box = boxes[HASH(key)];
			if(insn.contains("-")) {
				box.remove(key);
			} else {
				box.put(key, Integer.parseInt(insn.substring(index + 1)));
			}
		}

		int sum = 0;
		for(int i = 0; i < 256; i++) {
			int box = i + 1;
			Iterator<Entry<String, Integer>> it = boxes[i].entrySet().iterator();
			int index = 1;
			while(it.hasNext()) {
				Entry<String, Integer> entry = it.next();
				sum += (box * index++ * entry.getValue());
			}
		}
		return sum;
	}

	private int HASH(String s) {
		int current = 0;
		for(char c : s.toCharArray()) {
			current += c;
			current *= 17;
			current %= 256;
		}
		return current;
	}
}
