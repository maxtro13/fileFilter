package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private static final String MAX_STRING_LENGTH = "Произошла ошибка, введена максимально допустимая длина строки";
    private static final String MAX_VALUE = "Введено максимально допустимое значение";
    private int stringCounter = 0;
    private int longCounter = 0;
    private int floatCounter = 0;

    private List<String> strings = new ArrayList<String>();
    private List<String> longs = new ArrayList<>();
    private List<String> doubles = new ArrayList<>();

    private Long maxLong = Long.MAX_VALUE;
    private Long minLong = Long.MIN_VALUE;
    private Double averageLong = 0.0;
    private Long sumLong = 0L;

    private Double maxDouble = Double.MAX_VALUE;
    private Double minDouble = Double.MIN_VALUE;
    private Double averageDouble = 0.0;
    private Double sumDouble = 0.0;

    private int maxStringLength = Integer.MAX_VALUE;
    private int minStringLength = Integer.MIN_VALUE;

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
            System.out.println("Произошла ошибка при обработке файла");
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
            longCounter++;
            longs.add(line);
        } else if (isDouble(line)) {
            floatCounter++;
            doubles.add(line);
        } else {
            stringCounter++;
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
            Long value = Long.parseLong(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDouble(String line) {
        try {
            Double value = Double.parseDouble(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    private void longsStatistics() {
//        longCounter++;
//        try {
//
//        }
//    }

    private void printStatistics() {
        System.out.printf("В файл записано %d строк", stringCounter);
        System.out.printf("В файл записано %d целых чисел", longCounter);
        System.out.printf("В файл записано %d вещественных чисел", floatCounter);
    }


    private static class BadException extends RuntimeException {
        private static final String MAX_STRING_LENGTH = "Произошла ошибка, введена максимально допустимая длина строки";
        private static final String MAX_VALUE = "Введено максимально допустимое значение";

        private BadException(String message) {
        }
    }
}
