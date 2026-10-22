package ru.edu.pr02;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = args.length > 0 ? args[0] : "data/sample.csv";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            int number = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(number + ") " + line + "  ->  " + TimesheetAnalyzer.buildReport(line));
                number++;
            }
        }
    }
}
