package com.tyldanny.wordcounter;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String path = args[0];
            File file = validateFile(path);
            if (file == null) {
                return;
            }
            readFile(file);
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the file path of the file you want scanned:");

        try {
            String input = reader.readLine();
            if (input == null) {
                System.out.println("Unable to read file. Make sure you are pointing to a valid file.");
                return;
            }
            String path = input.split("\\s+")[0];
            File file = validateFile(path);
            if (file == null) {
                return;
            }
            readFile(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static File validateFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("No file found at \"" + path + "\"");
            return null;
        }
        if (!file.isFile()) {
            System.out.println("The path mentioned points to a directory. It must point to a file!");
            return null;
        }
        if (!file.canRead()) {
            System.out.println("Unable to read file at \"" + path + "\"");
            return null;
        }
        return file;
    }

    private static void readFile(File file) {
        Map<String, Integer> map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                String[] words = line.replaceAll("[^a-zA-Z' ]", "").toLowerCase().split("\\s+");
                for (String word : words) {
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
                line = br.readLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        printResult(getMapOrderedByValue(map));
    }

    private static void printResult(Map<String, Integer> result) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String key : result.keySet()) {
            sb.append(key).append(": ").append(result.get(key));
            if (count < result.size() - 1) {
                sb.append("\n");
                continue;
            }
            count++;
        }
        System.out.print(sb);
    }

    private static Map<String, Integer> getMapOrderedByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<String, Integer> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }
}