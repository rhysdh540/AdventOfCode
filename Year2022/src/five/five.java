package five;

import java.util.Scanner;
import java.io.File;

public class five {
    public static String run1() {
        Scanner crate = null;
        Scanner sc = null;
        try{
            sc = new Scanner(new File("src/five/input.txt"));
            crate = new Scanner(new File("src/five/crates.txt"));
        }catch (Exception ignored){}
        assert sc != null;
        assert crate != null;
        Stack[] list = new Stack[9];
        String[] crates = new String[8];
        for(int i = 0; crate.hasNextLine(); i++){
            crates[i] = crate.nextLine();
        }
        for(int i = 7; i >= 0; i--){
            String line = crates[i].substring(0,crates[i].length()-1).replace("]     [", "] [_] [").replace("    [", "[_] [").replace("]    ", "] [_]").replace("]", "").replace("[", "").replace(" ", "");
            for(int j = 0; j < 9; j++){
                if(list[j] == null) list[j] = new Stack("");
                list[j].add(line.charAt(j));
            }
        } // now all the data from the crates is in the list
        while(sc.hasNextLine()){
            String line = sc.nextLine().replace("move ", "").replace("from ", "").replace("to ", "");
            String[] split = line.split(" ");
            for(int i = 0; i < Integer.parseInt(split[0]); i++)
                list[Integer.parseInt(split[1])-1].move(list[Integer.parseInt(split[2])-1]);
        } // this moves the crates around
        String result = "";
        for(int i = 0; i < 9; i++) // build the result
            result = result + list[i].get(list[i].getHeight()-1);
        return result;
    }
    public static String run2() {
        Scanner crate = null;
        Scanner sc = null;
        try{
            sc = new Scanner(new File("src/five/input.txt"));
            crate = new Scanner(new File("src/five/crates.txt"));
        }catch (Exception ignored){}
        assert sc != null;
        assert crate != null;
        Stack[] list = new Stack[9];
        String[] crates = new String[8];
        for(int i = 0; crate.hasNextLine(); i++){
            crates[i] = crate.nextLine();
        }
        for(int i = 7; i >= 0; i--){
            String line = crates[i].substring(0,crates[i].length()-1).replace("]     [", "] [_] [").replace("    [", "[_] [").replace("]    ", "] [_]").replace("]", "").replace("[", "").replace(" ", "");
            for(int j = 0; j < 9; j++){
                if(list[j] == null) list[j] = new Stack("");
                list[j].add(line.charAt(j));
            }
        } // now all the data from the crates is in the list
        while(sc.hasNextLine()){
            String line = sc.nextLine().replace("move ", "").replace("from ", "").replace("to ", "");
            String[] split = line.split(" ");
            list[Integer.parseInt(split[1])-1].moveMultiple(list[Integer.parseInt(split[2])-1], Integer.parseInt(split[0]));
        } // crates are moved
        String result = "";
        for(int i = 0; i < 9; i++) // build the result
            result = result + list[i].get(list[i].getHeight()-1);
        return result;
    }
}
// FINALLY YES