package src;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private static final String INPUT_LINE_ERROR = "Произошла ошибка при обработке входящей строки";
    private static final String INPUT_FILE_ERROR = "Произошла ошибка при обработке файла";

    private int stringCounter = 0;
    private int longCounter = 0;
    private int floatCounter = 0;

    private final DecimalFormat format = new DecimalFormat("#.####");


    private List<String> strings = new ArrayList<String>();
    private List<String> longs = new ArrayList<>();
    private List<String> doubles = new ArrayList<>();

    private BigInteger maxLong = BigInteger.ZERO;
    private BigInteger minLong = BigInteger.valueOf(Long.MAX_VALUE);

    private BigDecimal averageLong = BigDecimal.ZERO;
    private BigInteger sumLong = BigInteger.ZERO;

    private BigDecimal maxDouble = BigDecimal.ZERO;
    private BigDecimal minDouble = new BigDecimal(Double.MAX_VALUE);
    private BigDecimal averageDouble = BigDecimal.ZERO;
    private BigDecimal sumDouble = BigDecimal.ZERO;

    private int maxStringLength = 0;
    private int minStringLength = Integer.MAX_VALUE;

    public void processFile(String[] inputFiles, String outputPath, String prefix,
                            boolean append, boolean shortStat, boolean fullStat) {
        for (String fileName : inputFiles) {
            readFile(fileName);
        }
        writeResults(outputPath, append, prefix, shortStat, fullStat);

    }

    private void readFile(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringDefinition(line);
            }
        } catch (IOException exception) {
            System.out.println(INPUT_FILE_ERROR);
            System.out.println(exception.getMessage());
        }
    }

    private void writeResults(String outputPath, boolean append, String prefix, boolean shortStats, boolean fullStats) {
        Path pathToFile = Paths.get(outputPath);
        if (Files.notExists(pathToFile)) {
            try {
                Files.createDirectories(pathToFile);
            } catch (IOException exception) {
                System.out.println("Ошибка при создании пути к файлу" + exception.getMessage());
                return;
            }
        }
        if (shortStats) {
            printShortStatistics();
        }
        if (fullStats) {
            printFullStatistics();
        }
        String outputFileName = pathToFile.toString() + "\\" + (prefix == null ? "" : prefix);
        if (!longs.isEmpty()) {
            writeToFile(outputFileName + "integers.txt", longs, append);
        }
        if (!strings.isEmpty()) {
            writeToFile(outputFileName + "strings.txt", strings, append);
        }
        if (!doubles.isEmpty()) {
            writeToFile(outputFileName + "floats.txt", doubles, append);
        }
    }

    private void stringDefinition(String line) {
        line = line.trim().replace(",", ".");
        if (line.isEmpty()) {
            return;
        }

        if (isInteger(line)) {
            longsStats(line);
            longs.add(line);
        } else if (isDouble(line)) {
            doubleStats(line);
            doubles.add(line);
        } else {
            stringsStats(line);
            strings.add(line);
        }
    }

    private void writeToFile(String fileName, List<String> values, boolean append) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, append))) {
            for (String line : values) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.out.println("При записи файла произошла ошибка");
        }
    }

    private boolean isInteger(String line) {
        try {
//            Long.parseLong(line);
            new BigInteger(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String line) {
        try {
            return new BigDecimal(line).stripTrailingZeros().scale() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void longsStats(String line) {
        longCounter++;
        try {
            BigInteger value = new BigInteger(line);
            sumLong = sumLong.add(value);
            maxLong = maxLong.max(value);
            minLong = minLong.min(value);
            calculateAverageLong();
        } catch (NumberFormatException exception) {
            System.out.println(INPUT_LINE_ERROR);
        }
    }

    private void doubleStats(String line) {
        floatCounter++;
        try {
            BigDecimal value = new BigDecimal(line);
            sumDouble = sumDouble.add(value)
                    .setScale(4, RoundingMode.HALF_UP);
            maxDouble = maxDouble.max(value);
            minDouble = minDouble.min(value);
            calculateAverageDouble();
        } catch (NumberFormatException exception) {
            System.out.println(INPUT_LINE_ERROR);
        }
    }

    private void stringsStats(String line) {
        int length = line.length();
        stringCounter++;
        if (maxStringLength < length) maxStringLength = length;
        if (minStringLength > length) minStringLength = length;
    }

    private void printShortStatistics() {
        System.out.println("Выведена краткая статистика");
        System.out.printf("В файл записано %d строк\n", stringCounter);
        System.out.printf("В файл записано %d целых чисел\n", longCounter);
        System.out.printf("В файл записано %d вещественных чисел\n", floatCounter);
    }

    private void printFullStatistics() {
        System.out.print("Выведена полная статистика\n");
        System.out.println("-".repeat(100));

        System.out.println("Статистика целых чисел");
        System.out.printf("В файл записано %d целых чисел\n", longCounter);
        System.out.printf("Максимальное целое число %d\n", maxLong);
        System.out.printf("Минимальное целое число %d\n", minLong);
        System.out.printf("Сумма целых чисел %d\n", sumLong);
        System.out.printf("Среднее целых чисел %s\n", averageLong.toPlainString());
        System.out.println("-".repeat(100));

        System.out.println("Статистика вещественных чисел");
        System.out.printf("В файл записано %d вещественных чисел\n", floatCounter);
        System.out.printf("Максимальное вещественное число %.4f\n", maxDouble);
        System.out.printf("Минимальное вещественное число %.4f\n", minDouble);
        System.out.printf("Сумма вещественных чисел %s\n", sumDouble.toPlainString());
        System.out.printf("Среднее вещественных чисел %s\n", averageDouble.toPlainString());
        System.out.println("-".repeat(100));

        System.out.println("Статистика строк");
        System.out.printf("В файл записано %d строк\n", stringCounter);
        System.out.printf("Максимальная длина строка %d\n", maxStringLength);
        System.out.printf("Минимальная длина строка %d\n", minStringLength);
    }

    private void calculateAverageLong() {
        if (longCounter > 0) {
            try {
                averageLong = new BigDecimal(sumLong)
                        .divide(BigDecimal.valueOf(longCounter), 4, RoundingMode.HALF_UP)
                        .stripTrailingZeros();
            } catch (ArithmeticException exception) {
                System.out.println("Ошибка при вычислении среднего значения для целочисленных элементов");
            }
        } else {
            averageLong = BigDecimal.ZERO;
        }
    }

    private void calculateAverageDouble() {
        if (floatCounter > 0) {
            try {
                averageDouble = sumDouble
                        .stripTrailingZeros()
                        .divide(BigDecimal.valueOf(floatCounter), 4, RoundingMode.HALF_UP)
                        .stripTrailingZeros();
            } catch (ArithmeticException exception) {
                System.out.println("Ошибка при вычислении среднего значения для вещественных элементов");
            }
        } else {
            averageDouble = BigDecimal.ZERO;
        }
    }

}
