import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day7 implements Day.IntDay {

    public int run1Int() throws Exception {
        Folder rootFolder = readInput();
        int totalSize = 0;
        ArrayList<Folder> smallFolders = rootFolder.foldersSmallerThan(100000);
        for (Folder smallFolder : smallFolders) {
            totalSize += smallFolder.size();
        }
        return totalSize;
    }

    public int run2Int() throws Exception {
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

    private static Folder readInput() throws Exception {
        List<String> inputData = Main.getInput(7);

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

    static class Folder {
        private final String name;
        private final Folder parent;
        private final HashMap<String, Integer> fileSizes = new HashMap<>();
        private final HashMap<String, Folder> folders = new HashMap<>();
        private int size = -1;

        public Folder(Folder parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public Folder getParent() {
            return parent;
        }
        public HashMap<String, Integer> getFileSizes() {
            return fileSizes;
        }
        public HashMap<String, Folder> getFolders() {
            return folders;
        }
        public void putFolder(String name, Folder folder) {
            folders.put(name, folder);
        }
        public void putFileSize(String name, int size) {
            fileSizes.put(name, size);
        }

        public int size() {
            if (this.size == -1) {
                size = 0;
                for (Folder subfolder : folders.values())
                    size += subfolder.size();
                for (Integer fileSize : fileSizes.values())
                    size += fileSize;
            } return this.size;
        }

        public ArrayList<Folder> foldersSmallerThan(int maxSize) {
            ArrayList<Folder> smallFolders = new ArrayList<>();
            for (Folder subfolder : folders.values()) {
                if (subfolder.size() <= maxSize)
                    smallFolders.add(subfolder);
                smallFolders.addAll(subfolder.foldersSmallerThan(maxSize));
            }
            return smallFolders;
        }

        public ArrayList<Folder> foldersLargerThan(int minSize) {
            ArrayList<Folder> largeFolders = new ArrayList<>();
            for (Folder subfolder : folders.values()) {
                if (subfolder.size() >= minSize)
                    largeFolders.add(subfolder);
                largeFolders.addAll(subfolder.foldersLargerThan(minSize));
            }
            return largeFolders;
        }

        public String path() {
            return (parent != null) ? parent.path() + name + "/" : "/";
        }
    }
}
