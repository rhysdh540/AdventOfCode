public class Helper {
    public static String[] readLines(String path) throws Exception {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path))).split("\r");
    }
}
