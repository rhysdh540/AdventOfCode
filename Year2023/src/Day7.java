import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2023/day/7">Day 7</a>
 */
public class Day7 extends Day.IntDay {
    @Override
    public int run1Int(List<String> input) {
		return run(this::getCardValue, false);
    }

    @Override
    public int run2Int(List<String> input) {
		return run(card -> card == 'J' ? 1 : getCardValue(card), true);
    }

	private int run(Function<Character, Integer> cardValueGetter, boolean jokers) {
		List<Hand> hands = new ArrayList<>();
		for(String s : getInput()) {
			String[] parts = s.split(" ");
			int bid = Utils.fastParseInt(parts[1]);
			int firstCard = cardValueGetter.apply(parts[0].charAt(0));
			HandType type = getHandType(parts[0], jokers);
			hands.add(new Hand(parts[0], type, bid));
		}

		hands.sort((a, b) -> {
			if(a.type != b.type) {
				return a.type.compareTo(b.type);
			}
			for(int i = 0; i < a.cards.length(); i++) {
				int aVal = cardValueGetter.apply(a.cards.charAt(i));
				int bVal = cardValueGetter.apply(b.cards.charAt(i));
				if(aVal != bVal) {
					return bVal - aVal;
				}
			}
			return 0;
		});

		Collections.reverse(hands);
		int sum = 0;
		for(int i = 0; i < hands.size(); i++) {
			sum += hands.get(i).bid * (i + 1);
		}
		return sum;
	}

	private int getCardValue(char card) {
		return switch(card) {
			case 'A' -> 14;
			case 'K' -> 13;
			case 'Q' -> 12;
			case 'J' -> 11;
			case 'T' -> 10;
			default -> Utils.fastParseInt(card);
		};
	}

	private HandType getHandType(String hand, boolean jokers) {
	    if (hand.contains("J") && jokers) {
	        String handWithoutJokers = hand.replace("J", "");

			Entry<Character, Long> best = null;

			Comparator<Entry<Character, Long>> comparator = Entry.comparingByValue();

			Map<Character, Long> map = new HashMap<>();
			for(Character c : handWithoutJokers.toCharArray()) {
				map.merge(c, 1L, Long::sum);
			}

			for(Entry<Character, Long> entry : map.entrySet()) {
				if(best == null || comparator.compare(entry, best) > 0) {
					best = entry;
				}
			}
			char mostFrequentCard = Optional.ofNullable(best)
	                .map(Entry::getKey)
	                .orElse((char) 0);
	        hand = hand.replace("J", "" + mostFrequentCard);
	    }

		int numUnique = 0;
		IntHashSet uniqueValues = new IntHashSet();
		for(char character : hand.toCharArray()) {
			if(uniqueValues.add(character)) {
				numUnique++;
			}
		}
		return switch(numUnique) {
			case 1 -> HandType.FIVE_OF_A_KIND;
			case 2 -> {
				int numFirst = 0;
				for(char c : hand.toCharArray()) {
					if(c == hand.charAt(0)) {
						numFirst++;
					}
				}
				yield numFirst == 4 || numFirst == 1
						? HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
			}
			case 3 -> {
				Map<Character, Integer> map = new HashMap<>();
				for(char character : hand.toCharArray()) {
					map.merge(character, 1, Integer::sum);
				}
				yield map.containsValue(3)
						? HandType.THREE_OF_A_KIND : HandType.TWO_PAIR;
			}
			case 4 -> HandType.ONE_PAIR;
			default -> HandType.HIGH_CARD;
		};
	}

	record Hand(String cards, HandType type, int bid) {}

	enum HandType {
		FIVE_OF_A_KIND,
		FOUR_OF_A_KIND,
		FULL_HOUSE,
		THREE_OF_A_KIND,
		TWO_PAIR,
		ONE_PAIR,
		HIGH_CARD
	}
}
