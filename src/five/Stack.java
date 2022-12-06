package five;

import java.util.ArrayList;

public class Stack {
    private ArrayList<Character> data = new ArrayList<>();

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
            sb.append(" [").append(c).append("] ");
        }
        return sb.toString().trim();
    }
}
