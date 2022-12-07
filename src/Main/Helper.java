package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Helper {
    public static ArrayList<String> readLines(String path) throws IOException {
        Scanner scanner = new Scanner(new java.io.File(path));
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine())
            lines.add(scanner.nextLine());
        return lines;
    }
    public static int sumNums(String s) {
        int sum = 0;
        for(char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                sum *= 10;
                sum += Character.getNumericValue(c);
            }
        }
        return sum;
    }
}
