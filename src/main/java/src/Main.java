package src;

public class Main {
    public static void main(String[] args) {
        FileFilter fileFilter = new FileFilter();
        fileFilter.processFile(new String[]{
                "C:\\Users\\User\\Desktop\\1.txt",
                "C:\\Users\\User\\Desktop\\2.txt"}, true);
    }
}
