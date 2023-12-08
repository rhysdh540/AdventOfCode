import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2023/day/8">Day 8</a>
 */
public class Day8 extends Day<Long> {
    @Override
    public Long run1(List<String> input) {
		String insns = input.remove(0);
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
			if(i == insns.length()) {
				i = 0;
			}
			if(pointer.name.equals("ZZZ")) {
				return count;
			}
			pointer = findNextNode(nodes, pointer, insns.charAt(i));
		}
    }

    @Override
    public Long run2(List<String> input) {
		String insns = input.remove(0);
		List<Node> nodes = getParsedInput(input);

		List<Node> pointers = new ArrayList<>();
		for(Node n1 : nodes) {
			if(n1.name.endsWith("A")) {
				pointers.add(n1);
			}
		}
		List<Long> periods = new ArrayList<>();
		for(Node node : pointers) {
			long count = 0;
			for(int i = 0; ; i++, count++) {
				if(i == insns.length()) i = 0;
				if(node.name.endsWith("Z")) {
					periods.add(count);
					break;
				}
				node = findNextNode(nodes, node, insns.charAt(i));
			}
		}

		long lcm = periods.get(0);
		for(int i = 1; i < periods.size(); i++) {
			lcm = lcm(lcm, periods.get(i));
		}
		return lcm;
    }

	private Node findNextNode(List<Node> nodes, Node node, char insn) {
		String target = insn == 'L' ? node.next.getFirst() : node.next.getSecond();
		for(Node n : nodes) {
			if(n.name.equals(target)) {
				return n;
			}
		}
		throw new AssertionError();
	}

	private final Pattern uselessStuff = Pattern.compile("[=(,)]");

	private List<Node> getParsedInput(List<String> input) {
		List<Node> list = new ArrayList<>();
		for(String s : input) {
			s = uselessStuff.matcher(s).replaceAll("");
			if(s.isEmpty()) continue;
			String[] parts = Utils.SPACES.split(s);
			list.add(new Node(parts[0], Pair.of(parts[1], parts[2])));
		}
		return list;
	}

	private long lcm(long a, long b) {
		return a * b / gcd(a, b);
	}

	private long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	record Node(String name, Pair<String, String> next) {}
}
