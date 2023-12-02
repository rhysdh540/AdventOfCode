import java.util.ArrayList;
import java.util.List;

public class Day5 implements Day<String> {
    public String run1() throws Exception {
        List<String> input = Main.getInput(5);
        List<String> cratesIn = Main.getInput(5, "crates");
        Stack[] list = new Stack[9];
        String[] crates = cratesIn.toArray(String[]::new);
        for(int i = 7; i >= 0; i--){
            String line = crates[i].substring(0,crates[i].length()-1).replace("]     [", "] [_] [").replace("    [", "[_] [").replace("]    ", "] [_]").replace("]", "").replace("[", "").replace(" ", "");
            for(int j = 0; j < 9; j++){
                if(list[j] == null) list[j] = new Stack("");
                list[j].add(line.charAt(j));
            }
        } // now all the data from the crates is in the list
        for(String s : input){
            String line = s.replace("move ", "").replace("from ", "").replace("to ", "");
            String[] split = line.split(" ");
            for(int i = 0; i < Integer.parseInt(split[0]); i++)
                list[Integer.parseInt(split[1])-1].move(list[Integer.parseInt(split[2])-1]);
        } // this moves the crates around
        String result = "";
        for(int i = 0; i < 9; i++) // build the result
            result = result + list[i].get(list[i].getHeight()-1);
        return result;
    }
    public String run2() throws Exception {
        List<String> input = Main.getInput(5);
        List<String> cratesIn = Main.getInput(5, "crates");
        Stack[] list = new Stack[9];
        String[] crates = cratesIn.toArray(String[]::new);
        for(int i = 7; i >= 0; i--){
            String line = crates[i].substring(0,crates[i].length()-1).replace("]     [", "] [_] [").replace("    [", "[_] [").replace("]    ", "] [_]").replace("]", "").replace("[", "").replace(" ", "");
            for(int j = 0; j < 9; j++){
                if(list[j] == null) list[j] = new Stack("");
                list[j].add(line.charAt(j));
            }
        } // now all the data from the crates is in the list
        for(String s : input){
            String line = s.replace("move ", "").replace("from ", "").replace("to ", "");
            String[] split = line.split(" ");
            list[Integer.parseInt(split[1])-1].moveMultiple(list[Integer.parseInt(split[2])-1], Integer.parseInt(split[0]));
        } // crates are moved
        String result = "";
        for(int i = 0; i < 9; i++) // build the result
            result = result + list[i].get(list[i].getHeight()-1);
        return result;
    }

    static class Stack {
        private ArrayList<Character> data = new ArrayList<>(); // intellij this is not final thats the whole point of this class please ahhh

        public Stack(String s) {
            for (char c : s.toCharArray())
                data.add(c);
        }

        public void add(char c) {
            if(c == '_') return;
            data.add(c);
        }
        public void move(Stack to){
            to.add(data.remove(data.size()-1));
        }
        public void moveMultiple(Stack to, int amount){
            Stack mid = new Stack("");
            for(int i = 0; i < amount; i++)
                move(mid);
            for(int i = 0; i < amount; i++)
                mid.move(to);
        }
        public char get(int i){
            return data.get(i);
        }
        public String toString(){
            StringBuilder sb = new StringBuilder();
            for(char c : data) sb.append(c);
            return sb.toString();
        }
        public int getHeight(){
            return data.size();
        }
        public String toStringFancy(){
            StringBuilder sb = new StringBuilder();
            for(char c : data){
                sb.append("[").append(c).append("] ");
            }
            return sb.toString().trim();
        }
    }
}