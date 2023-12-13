import aoc.Day.LongDay;
import util.Pair;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <a href="https://adventofcode.com/2023/day/8">Day 8</a>
 */
public class Day8 extends LongDay {
    @Override
    public long run1Long(List<String> input) {
		char[] insns = input.remove(0).toCharArray();
		List<Node> nodes = getParsedInput(input);

		Node pointer = null;
		for(Node node : nodes) {
			if(node.name.equals("AAA")) {
				pointer = node;
				break;
			}
		}

		assert pointer != null;

		long count = 0;
		for(int i = 0; ; i++, count++) {
			if(i == insns.length) {
				i = 0;
			}
			if(pointer.name.equals("ZZZ")) {
				return count;
			}
			pointer = findNextNode(pointer, insns[i]);
		}
    }

    @Override
    public long run2Long(List<String> input) {
		char[] insns = input.remove(0).toCharArray();
		List<Node> nodes = getParsedInput(input);

		List<Node> pointersList = new ArrayList<>();
		for(Node n : nodes) {
			if(n.name.charAt(2) == 'A') {
				pointersList.add(n);
			}
		}
		Node[] pointers = pointersList.toArray(new Node[0]);

		List<Long> periods = new ArrayList<>();
		for(Node node : pointers) {
			long count = 0;
			for(int i = 0; ; i++, count++) {
				if(i == insns.length) i = 0;
				if(node.name.charAt(2) == 'Z') {
					periods.add(count);
					break;
				}
				node = findNextNode(node, insns[i]);
			}
		}

		long lcm = periods.get(0);
		for(int i = 1; i < periods.size(); i++) {
			lcm = Utils.lcm(lcm, periods.get(i));
		}
		return lcm;
    }

	private Map<String, Node> nodeMap = new HashMap<>();

	private Node findNextNode(Node node, char insn) {
		String target = insn == 'L' ? node.next.getFirst() : node.next.getSecond();
		Node targetNode = nodeMap.get(target);
		if(targetNode == null) {
			throw new AssertionError();
		}
		return targetNode;
	}

	private final Pattern uselessStuff = Pattern.compile("[=(,)]");

	private List<Node> getParsedInput(List<String> input) {
		List<Node> list = new ArrayList<>();
		for(String s : input) {
			s = uselessStuff.matcher(s).replaceAll("");
			if(s.isEmpty()) continue;
			String[] parts = Utils.SPACES.split(s);
			Node node = new Node(parts[0], Pair.of(parts[1], parts[2]));
			list.add(node);
			nodeMap.put(node.name, node);
		}
		return list;
	}

	record Node(String name, Pair<String, String> next) {}
}
