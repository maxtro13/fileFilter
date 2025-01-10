package src;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private static final String INPUT_LINE_ERROR = "Произошла ошибка при обработке входящей строки";
    private static final String INPUT_FILE_ERROR = "Произошла ошибка при обработке файла";

    private int stringCounter = 0;
    private int longCounter = 0;
    private int floatCounter = 0;

    private List<String> strings = new ArrayList<String>();
    private List<String> longs = new ArrayList<>();
    private List<String> doubles = new ArrayList<>();

    private long maxLong = 0L;
    private long minLong = 0L;
    private double averageLong = 0.0;
    private BigInteger sumLong = BigInteger.ZERO;

    private double maxDouble = 0.0;
    private double minDouble = 0.0;
    private double averageDouble = 0.0;
    private BigDecimal sumDouble = BigDecimal.ZERO;

    private int maxStringLength = 0;
    private int minStringLength = 0;

    public void processFile(String[] inputFiles, boolean append) {
        for (String fileName : inputFiles) {
            readFile(fileName, append);
        }

        System.out.println(stringCounter);
        System.out.println(longCounter);
    }

    private void readFile(String fileName, boolean append) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringDefinition(line);
            }
            writeResults(fileName, append);
        } catch (IOException exception) {
            System.out.println(INPUT_FILE_ERROR);
        }
    }

    private void writeResults(String outputFile, boolean append) {
        if (!longs.isEmpty()) {
            writeToFile("integers", longs, append);
        }
        if (!strings.isEmpty()) {
            writeToFile("strings", strings, append);
        }
        if (!doubles.isEmpty()) {
            writeToFile("floats", doubles, append);
        }
    }

    private void stringDefinition(String line) {
        line = line.trim();
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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName + ".txt", append))) {
            for (String line : values) {
                bufferedWriter.write(line);
                System.out.println(line);
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            System.out.println("При записи файла произошла ошибка");
        }
    }

    private boolean isInteger(String line) {
        try {
            Long.parseLong(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String line) {
        try {
            Double.parseDouble(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void longsStats(String line) {
        longCounter++;
        try {
            long value = Long.parseLong(line);
            sumLong = sumLong.add(BigInteger.valueOf(value));
            if (value > maxLong) maxLong = value;
            if (value < minLong) minLong = value;
            calculateAverageLong();
        } catch (NumberFormatException exception) {
            System.out.println(INPUT_LINE_ERROR);
        }
    }

    private void doubleStats(String line) {
        floatCounter++;
        try {
            double value = Double.parseDouble(line);
            sumDouble = sumDouble.add(BigDecimal.valueOf(value));
            if (value > maxDouble) maxDouble = value;
            if (value < minDouble) minDouble = value;
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
        System.out.printf("В файл записано %d строк", stringCounter);
        System.out.printf("В файл записано %d целых чисел", longCounter);
        System.out.printf("В файл записано %d вещественных чисел", floatCounter);
    }

    private void printFullStatistics() {
        System.out.println("");
    }

    private void calculateAverageLong() {
        if (longCounter > 0) {
            try {
                averageLong = new BigDecimal(sumLong)
                        .divide(BigDecimal.valueOf(longCounter), 2, RoundingMode.HALF_UP)
                        .doubleValue();
            } catch (ArithmeticException exception) {
                System.out.println("Ошибка при вычислении среднего значения для целочисленных элементов");
            }
        } else {
            averageLong = 0.0;
        }
    }

    private void calculateAverageDouble() {
        if (floatCounter > 0) {
            try {
                averageDouble = sumDouble
                        .divide(BigDecimal.valueOf(floatCounter), 3, RoundingMode.HALF_UP)
                        .doubleValue();
            } catch (ArithmeticException exception) {
                System.out.println("Ошибка при вычислении среднего значения для вещественных элементов");
            }
        } else {
            averageDouble = 0.0;
        }
    }

    private static class BadException extends RuntimeException {
        private static final String MAX_STRING_LENGTH = "Произошла ошибка, введена максимально допустимая длина строки";
        private static final String MAX_VALUE = "Введено максимально допустимое значение";

        private BadException(String message) {
        }
    }
}
