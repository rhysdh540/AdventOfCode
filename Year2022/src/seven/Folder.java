package seven;

import java.util.ArrayList;
import java.util.HashMap;

public class Folder {
    private final String name;
    private Folder parent;
    private HashMap<String, Integer> fileSizes = new HashMap<>();
    private HashMap<String, Folder> folders = new HashMap<>();
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