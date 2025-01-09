package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    private int stringCounter = 0;
    private int longCounter = 0;
    private int doubleCounter = 0;
    private List<String> strings = new ArrayList<String>();
    private List<String> longs = new ArrayList<>();
    private List<String> doubles = new ArrayList<>();

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
                writeResults(stringDefinition(line), append);
            }
        } catch (IOException exception) {
            System.out.println("Произошла ошибка при обработке файла");
        }
    }

    private void writeResults(String outputFile, boolean append) {
        if (!longs.isEmpty()) {
            writeToFile(outputFile, longs, append);
        }
        if (!strings.isEmpty()) {
            writeToFile(outputFile, strings, append);
        }
        if (!doubles.isEmpty()) {
            writeToFile(outputFile, doubles, append);
        }

    }

    private String stringDefinition(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            throw new BadException("Строка пуста");
        }
        if (isInteger(line)) {
            longCounter++;
            longs.add(line);
            return "integers";
        } else if (isDouble(line)) {
            doubleCounter++;
            doubles.add(line);
            return "doubles";
        } else {
            stringCounter++;
            strings.add(line);
            return "strings";
        }
    }

    private void writeToFile(String fileName, List<String> values, boolean append) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName+".txt", append))) {
            for (String line : values) {
                bufferedWriter.write(line);
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

    private static class BadException extends RuntimeException {

        private BadException(String message) {
        }
    }
}
