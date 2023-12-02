import java.util.List;

public class Day2 implements Day<Integer, Integer> {
    private static final int ROCK = 1;
    private static final int PAPER = 2;
    private static final int SCISSORS = 3;

    private static final int LOSE = 0;
    private static final int DRAW = 3;
    private static final int WIN = 6;

    public Integer run1() throws Exception {
        List<String> input = Main.getInput(2);
        int score = 0;
        for(String line : input) {
            score += getScore(line);
        }
        return score;
    }
    public Integer run2() throws Exception {
        List<String> input = Main.getInput(2);
        int score = 0;
        for(String line : input) {
            score += getScore2(line);
        }
        return score;
    }
    public static int getScore(String s) {
        int p1 = s.charAt(0) == 'A' ? ROCK : s.charAt(0) == 'B' ? PAPER : SCISSORS;
        int p2 = s.charAt(2) == 'X' ? ROCK : s.charAt(2) == 'Y' ? PAPER : SCISSORS;
        if (p1 == p2) return DRAW + p2;
        if (p1 == SCISSORS && p2 == ROCK) return WIN + p2;
        if (p1 == ROCK && p2 == PAPER) return WIN + p2;
        if (p1 == PAPER && p2 == SCISSORS) return WIN + p2;
        return p2;
    }
    public static int getScore2(String s) {
        int p1 = s.charAt(0) == 'A' ? ROCK : s.charAt(0) == 'B' ? PAPER : SCISSORS;
        int p2 = s.charAt(2) == 'X' ? LOSE : s.charAt(2) == 'Y' ? DRAW : WIN;
        switch(p2) {
            case LOSE -> {
                return switch(p1){
                    case ROCK -> SCISSORS;
                    case PAPER -> ROCK;
                    case SCISSORS -> PAPER;
                    default -> throw new IllegalStateException("Unexpected value: " + p1);
                };
            }
            case DRAW -> { return DRAW + p1; }
            case WIN -> {
                return switch(p1){
                    case ROCK -> WIN + PAPER;
                    case PAPER -> WIN + SCISSORS;
                    case SCISSORS -> WIN + ROCK;
                    default -> throw new IllegalStateException("Unexpected value: " + p1);
                };
            }
        }
        return -1;
    }
}