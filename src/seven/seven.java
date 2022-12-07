package seven;

import Main.Helper;

import java.io.IOException;
import java.util.ArrayList;

public class seven {

    public static int run1() throws IOException {
        Folder rootFolder = readInput();
        int totalSize = 0;
        ArrayList<Folder> smallFolders = rootFolder.foldersSmallerThan(100000);
        for (Folder smallFolder : smallFolders) {
            totalSize += smallFolder.size();
        }
        return totalSize;
    }

    public static int run2() throws IOException {
        int unused = 30000000, total = 70000000;
        int maxSpace = total - unused;
        Folder rootFolder = readInput();
        //System.out.println("Used: " + rootFolder.size());
        int toDelete = rootFolder.size() - maxSpace;
        //System.out.println("To Delete: " + toDelete);

        ArrayList<Folder> options = rootFolder.foldersLargerThan(toDelete);
        Folder smallest = options.get(0);
        for (Folder option : options) {
            if (option.size() < smallest.size())
                smallest = option;
        }
        return smallest.size();
    }

    private static Folder readInput() throws IOException {
        ArrayList<String> inputData = Helper.readLines("src/seven/input.txt");

        Folder rootFolder = new Folder(null, "/");
        Folder currentFolder = rootFolder;

        for (String line : inputData) {
            String[] parts = line.split(" ");
            if (parts[0].equals("$")) {
                switch(parts[1]) {
                    case "cd":
                        if (parts[2].equals("/")) {
                            currentFolder = rootFolder;
                        } else if (parts[2].equals("..")) {
                            if (currentFolder.getParent() == null) {
                                throw new IOException("Attempted to traverse up from root");
                            }
                            currentFolder = currentFolder.getParent();
                        } else {
                            if (!currentFolder.getFolders().containsKey(parts[2])) {
                                currentFolder.getFolders().put(parts[2], new Folder(currentFolder, parts[2]));
                            }
                            currentFolder = currentFolder.getFolders().get(parts[2]);
                        }
                        break;
                    case "ls":
                        break;
                    default:
                        throw new IOException("Unexpected command");
                }
            } else if (parts[0].equals("dir")) {
                if (!currentFolder.getFolders().containsKey(parts[1]))
                    currentFolder.putFolder(parts[1], new Folder(currentFolder, parts[1]));
            } else currentFolder.putFileSize(parts[1], Integer.parseInt(parts[0]));
        }
        return rootFolder;
    }
}
