public class Day6 implements Day.IntDay {
    public int run1Int() throws Exception {
        String input = Main.getInput(6).get(0);
        for(int i = 4; i < input.length(); i++)
            if(isAllDifferent(input.substring(i-4, i)))
                return i;
        return -1;
    }
    public int run2Int() throws Exception {
        String input = Main.getInput(6).get(0);
        for(int i = 14; i < input.length(); i++)
            if(isAllDifferent14(input.substring(i-14, i)))
                return i;
        return -1;
    }
    public static boolean isAllDifferent(String s) throws Exception {
        if(s.length() != 4) throw new Exception("String must be length 4");
        for(char c : s.toCharArray())
            if(s.indexOf(c) != s.lastIndexOf(c))
                return false;
        return true;
    }
    public static boolean isAllDifferent14(String s) throws Exception {
        if(s.length() != 14) throw new Exception("String must be length 14");
        for(char c : s.toCharArray())
            if(s.indexOf(c) != s.lastIndexOf(c))
                return false;
        return true;
    }
}
