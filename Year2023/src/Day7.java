import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
			sum += hands.get(i).bid *(i + 1);
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
		if(hand.contains("J") && jokers) {
			hand = replaceJoker(hand);
		}

		return switch(countUniqueCards(hand)) {
			case 1 -> HandType.FIVE_OF_A_KIND;
			case 2 -> evaluateTwoUnique(hand);
			case 3 -> evaluateThreeUnique(hand);
			case 4 -> HandType.ONE_PAIR;
			default -> HandType.HIGH_CARD;
		};
	}

	private String replaceJoker(String hand) {
		Map<Character, Long> frequencyMap = countCardFrequency(hand.replace("J", ""));
		char mostFrequentCard = getMostFrequentCard(frequencyMap);
		return hand.replace("J", String.valueOf(mostFrequentCard));
	}

	private int countUniqueCards(String hand) {
		int numUnique = 0;
		boolean[] visited = new boolean[128]; // Assuming ASCII characters
		for(char character : hand.toCharArray()) {
			if(!visited[character]) {
				visited[character] = true;
				numUnique++;
			}
		}
		return numUnique;
	}

	private Map<Character, Long> countCardFrequency(String hand) {
		Map<Character, Long> map = new HashMap<>();
		for(char c : hand.toCharArray()) {
			map.merge(c, 1L, Long::sum);
		}
		return map;
	}

	private char getMostFrequentCard(Map<Character, Long> frequencyMap) {
		char mostFrequentCard = 0;
		long maxFrequency = 0;
		for(Entry<Character, Long> entry : frequencyMap.entrySet()) {
			if(entry.getValue() > maxFrequency) {
				mostFrequentCard = entry.getKey();
				maxFrequency = entry.getValue();
			}
		}
		return mostFrequentCard;
	}

	private HandType evaluateTwoUnique(String hand) {
		int numFirst = 0;
		for(char c : hand.toCharArray()) {
			if(c == hand.charAt(0)) {
				numFirst++;
			}
		}
		return(numFirst == 4 || numFirst == 1) ?
			HandType.FOUR_OF_A_KIND : HandType.FULL_HOUSE;
	}

	private HandType evaluateThreeUnique(String hand) {
		int[] freq = new int[128];
		for(char character : hand.toCharArray()) {
			freq[character]++;
		}

		int countOfThree = 0;
		int countOfTwo = 0;
		int countOfOne = 0;

		for(int i = 0; i < 128; i++) {
			if(freq[i] == 3) countOfThree++;
			else if(freq[i] == 2) countOfTwo++;
			else if(freq[i] == 1) countOfOne++;
		}

		if(countOfThree == 1 && (countOfTwo == 1 || countOfOne == 2)) {
			return HandType.THREE_OF_A_KIND;
		} else if(countOfThree == 1 && countOfOne == 1) {
			return HandType.FULL_HOUSE;
		} else {
			return HandType.TWO_PAIR;
		}
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
