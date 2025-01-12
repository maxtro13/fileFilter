package src;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.util.*;

public class Main {

    @Parameter(names = "-o", description = "Аргумент, задающий конечный путь для создаваемых файлов")
    private String outputPath = "";

    @Parameter(names = "-p", description = "Префикс создаваемых файлов")
    private String prefix = "";

    @Parameter(names = "-a", description = "Отключить перезапись файлов")
    private boolean append = false;

    @Parameter(names = "-s", description = "Вывод короткой статистики")
    private boolean shortStats;

    @Parameter(names = "-f", description = "Вывод полной статистики")
    private boolean fullStats;
//todo разобраться почему добавляется точка в названию файлов, протестировать разные параметры в cmd
    @Parameter(description = "Входящие файлы")
    private List<String> files = new ArrayList<>();

    public static void main(String[] args) {

        Main main = new Main();
        JCommander jCommander = null;
        try {
            jCommander = JCommander.newBuilder()
                    .addObject(main)
                    .build();
            jCommander.parse(args);
            main.run();
        } catch (ParameterException exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
            jCommander.usage();
        }
    }

    public void run() {
        FileFilter fileFilter = new FileFilter();
        fileFilter.processFile(files.toArray(new String[0]), outputPath, prefix, append, shortStats, fullStats);
    }

    private boolean isFiles() {
        if (!files.isEmpty()) {
            for (String fileName : files) {
                File file = new File(fileName);
                return fileName.endsWith(".txt") && file.isFile() && file.exists();
            }
        } else {
            throw new ParameterException("Такого параметра не существует");
        }
        return false;
    }
}
