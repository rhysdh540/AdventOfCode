import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://adventofcode.com/2023/day/7">Day 7</a>
 */
public class Day7 extends Day.IntDay {
    @Override
    public int run1Int(List<String> input) {
		return run(this::getCardValue, this::getHandType);
    }

    @Override
    public int run2Int(List<String> input) {
		return run(this::getCardValue2, this::getHandType2);
    }

	private int run(Function<Character, Integer> cardValueGetter, Function<String, HandType> handTypeGetter) {
		List<Hand> hands = getInput().stream().map(line -> {
			String[] parts = line.split(" ");
			int bid = Integer.parseInt(parts[1]);
			int firstCard = cardValueGetter.apply(parts[0].charAt(0));
			HandType type = handTypeGetter.apply(parts[0]);
			return new Hand(parts[0], type, bid);
		}).sorted((a, b) -> {
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
		}).collect(Collectors.toList());
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
			default -> Integer.parseInt(card + "");
		};
	}

	private int getCardValue2(char card) {
		return card == 'J' ? 1 : getCardValue(card);
	}

	private HandType getHandType(String hand) {
		int numUnique = (int) hand.chars().distinct().count();
		if(numUnique == 1) {
			return HandType.FIVE_OF_A_KIND;
		} else if(numUnique == 2) {
			//test between 4-of-a-kind and full house
			int numFirst = (int) hand.chars().filter(c -> c == hand.charAt(0)).count();
			if(numFirst == 4 || numFirst == 1) {
				return HandType.FOUR_OF_A_KIND;
			} else {
				return HandType.FULL_HOUSE;
			}
		} else if(numUnique == 3) {
			//test between 3-of-a-kind and two pair
			if(hand.chars().boxed().collect(Collectors.toMap(
				c -> (char) c.intValue(),
				c -> 1,
				Integer::sum
			)).values().stream().anyMatch(c -> c == 3)) {
				return HandType.THREE_OF_A_KIND;
			} else {
				return HandType.TWO_PAIR;
			}
		} else if(numUnique == 4) {
			return HandType.ONE_PAIR;
		} else {
			return HandType.HIGH_CARD;
		}
	}

	private HandType getHandType2(String hand) {
	    //jokers "J" are wild and can be any card
	    if (hand.contains("J")) {
	        String handWithoutJokers = hand.replace("J", "");
	        char mostFrequentCard = handWithoutJokers.chars()
	                .mapToObj(c -> (char) c)
	                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
	                .entrySet().stream()
	                .max(Map.Entry.comparingByValue())
	                .map(Map.Entry::getKey)
	                .orElse((char) 0);
	        hand = hand.replace("J", String.valueOf(mostFrequentCard));
	    }

	    int numUnique = (int) hand.chars().distinct().count();
	    if(numUnique == 1) {
	        return HandType.FIVE_OF_A_KIND;
	    } else if(numUnique == 2) {
			String finalHand = hand;
			int numFirst = (int) hand.chars().filter(c -> c == finalHand.charAt(0)).count();
	        if(numFirst == 4 || numFirst == 1) {
	            return HandType.FOUR_OF_A_KIND;
	        } else {
	            return HandType.FULL_HOUSE;
	        }
	    } else if(numUnique == 3) {
	        if(hand.chars().boxed().collect(Collectors.toMap(
	            c -> (char) c.intValue(),
	            c -> 1,
	            Integer::sum
	        )).values().stream().anyMatch(c -> c == 3)) {
	            return HandType.THREE_OF_A_KIND;
	        } else {
	            return HandType.TWO_PAIR;
	        }
	    } else if(numUnique == 4) {
	        return HandType.ONE_PAIR;
	    } else {
	        return HandType.HIGH_CARD;
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
		HIGH_CARD;
	}
}
