import java.util.List;
import java.util.ArrayList;

public class Day1 implements Day<Integer, Integer> {
    public Integer run1() throws Exception {
        List<String> input = Main.getInput(1);
        ArrayList<Integer> calories = new ArrayList<>();
        boolean isFirst = true;
        for(String line : input) {
            if (line.isEmpty()) {
                isFirst = true;
                continue;
            }
            if(isFirst) {
                calories.add(Integer.parseInt(line));
                isFirst = false;
            } else {
                calories.set(calories.size() - 1, calories.get(calories.size() - 1) + Integer.parseInt(line));
            }
        }
        int max = 0;
        for (int i : calories) {
            if (i > max)
                max = i;
        }
        return max;
    }

    public Integer run2() throws Exception {
        List<String> input = Main.getInput(1);
        ArrayList<Integer> calories = new ArrayList<>();
        boolean isFirst = true;
        for(String line : input) {
            if (line.isEmpty()) {
                isFirst = true;
                continue;
            }
            if(isFirst) {
                calories.add(Integer.parseInt(line));
                isFirst = false;
            } else {
                calories.set(calories.size() - 1, calories.get(calories.size() - 1) + Integer.parseInt(line));
            }
        }
        int first = 0;
        for(int i = 0; i < calories.size(); i++) {
            if (calories.get(i) > first) {
                first = calories.get(i);
            }
        }
        int second = 0;
        for(int i = 0; i < calories.size(); i++) {
            if (calories.get(i) > second && calories.get(i) < first) {
                second = calories.get(i);
            }
        }
        int third = 0;
        for(int i = 0; i < calories.size(); i++) {
            if (calories.get(i) > third && calories.get(i) < second) {
                third = calories.get(i);
            }
        }
        return first + second + third;
    }
}
