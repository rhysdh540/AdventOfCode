package one;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class one {
    public static int run1(){
        Scanner sc = null;
        try {
            sc = new Scanner(new File("src/one/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        ArrayList<Integer> calories = new ArrayList<>();
        assert sc != null;
        calories.add(sc.nextInt());
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equals(""))
                calories.add(Integer.parseInt(sc.nextLine()));
            else
                calories.set(calories.size() - 1, calories.get(calories.size() - 1) + Integer.parseInt(line));
        }
        int max = 0;
        for (int i : calories) {
            if (i > max)
                max = i;
        }
        return max;
    }
    public static int run2() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("src/one/input.txt"));
        } catch (FileNotFoundException e) { System.out.println("File not found"); }
        ArrayList<Integer> calories = new ArrayList<>();
        assert sc != null;
        calories.add(sc.nextInt());
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equals(""))
                calories.add(Integer.parseInt(sc.nextLine()));
            else
                calories.set(calories.size() - 1, calories.get(calories.size() - 1) + Integer.parseInt(line));
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
