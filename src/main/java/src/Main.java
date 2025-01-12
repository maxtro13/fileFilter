package src;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    @Parameter(names = "-o", description = "Аргумент, задающий конечный путь для создаваемых файлов", order = 0)
    private String outputPath = "";

    @Parameter(names = "-p", description = "Префикс создаваемых файлов", order = 2)
    private String prefix;

    @Parameter(names = "-a", description = "Отключить перезапись файлов", order = 1)
    private boolean append;

    @Parameter(names = "-s", description = "Вывод короткой статистики", order = 3)
    private boolean shortStats;

    @Parameter(names = "-f", description = "Вывод полной статистики", order = 4)
    private boolean fullStats;

    @Parameter(names = {"--help", "-h"}, help = true, description = "Вывод этого сообщения", order = 5)
    private boolean help;

    @Parameter
    private List<String> files = new ArrayList<>();


    public static void main(String[] args) {

        Main main = new Main();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(main)
                .programName("fileFilterUtil")
                .build();
        try {
            jCommander.parse(args);
            if (main.help) {
                jCommander.usage();
                return;
            }
            main.run();
        } catch (ParameterException exception) {
            System.out.println("Произошла ошибка " + exception.getMessage());
            jCommander.usage();
        }
    }

    public void run() {
        FileFilter fileFilter = new FileFilter();
        fileFilter.processFile(files.toArray(new String[0]), getJarPath(), prefix, append, shortStats, fullStats);
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

    private String getJarPath() {
        if (outputPath.isEmpty()) {
            try {
                return new File(Main.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI())
                        .getParent();
            } catch (URISyntaxException e) {
                System.out.println(e.getMessage());
            }
        }
        return outputPath;
    }
}
