package com.library.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic helper for reading and writing CSV-style data files.
 * Keeps file I/O concerns out of the service and model layers.
 */
public class FileManager {

    /**
     * Reads all lines from a file and maps each to an object of type T.
     * Returns an empty list if the file does not yet exist.
     */
    public static <T> List<T> readAll(String path, Function<String, T> parser) {
        List<T> results = new ArrayList<>();
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            return results;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                results.add(parser.apply(line));
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read " + path + " (" + e.getMessage() + ")");
        }
        return results;
    }

    /**
     * Writes a full list of items to file, overwriting any previous content.
     */
    public static <T> void writeAll(String path, List<T> items, Function<T, String> toCsv) {
        Path filePath = Paths.get(path);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (T item : items) {
                    writer.write(toCsv.apply(item));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: could not save " + path + " (" + e.getMessage() + ")");
        }
    }
}
