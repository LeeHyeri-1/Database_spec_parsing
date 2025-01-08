import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "./files/input.txt";
        FileReader fileReader = new FileReader(filePath);

        try {
            String fileContent = fileReader.readFile();
            parseFields(fileContent);
        } catch (RuntimeException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
    }

    public static void parseFields(String input) {
        String[] lines = input.split("\n");
        List<String> fields = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> hasNumbers = new ArrayList<>();
        List<String> isKey = new ArrayList<>();
        List<String> isNecessary = new ArrayList<>();
        List<String> defaults = new ArrayList<>();
        List<String> columnNumbers = new ArrayList<>();
        int columnCount = 0;

        // 헤더 제외하고 처리
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) continue;

            String[] parts = lines[i].split(";", -1);
            columnCount++;
            columnNumbers.add(String.valueOf(columnCount));

            // Field
            fields.add(parts[0]);

            // Type (기본 타입 추출)
            String type = parts[1].split("\\(")[0];
            types.add(type);

            // Number check
            hasNumbers.add(parts[1].matches(".*\\d+.*") ? extractNumber(parts[1]) : " ");

            // Key check
            isKey.add((parts[3] != null && (parts[3].contains("PRI") || parts[3].contains("MUL") || parts[3].contains("UNI"))) ? "Y" : "N");

            // Necessary check
            isNecessary.add(parts[2].contains("NO") ? "Y" : "N");

            // Default check - 수정된 부분
            String defaultValue = parts[4].equals("\\N") || parts[4].isEmpty() ? "NULL" : parts[4];
            String extraValue = parts.length > 5 ? parts[5] : "";

            if (extraValue.contains("auto_increment")) {
                defaults.add("auto_increment");
            } else if (defaultValue.contains("current_timestamp")) {
                defaults.add("current_timestamp()");
            } else if (defaultValue.matches("\\d+")) {
                defaults.add(defaultValue);
            } else {
                defaults.add("NULL");
            }
        }

        // 결과 출력
        System.out.println("=== Column Numbers ===");
        columnNumbers.forEach(System.out::println);

        System.out.println("=== Fields ===");
        fields.forEach(System.out::println);

        System.out.println("\n=== Types ===");
        types.forEach(System.out::println);

        System.out.println("\n=== Has Numbers ===");
        hasNumbers.forEach(System.out::println);

        System.out.println("\n=== Is Key ===");
        isKey.forEach(System.out::println);

        System.out.println("\n=== Is Necessary ===");
        isNecessary.forEach(System.out::println);

        System.out.println("\n=== Defaults ===");
        defaults.forEach(System.out::println);
    }
    private static String extractNumber(String part) {
        StringBuilder numbers = new StringBuilder();
        for (char c : part.toCharArray()) {
            if (Character.isDigit(c)) {
                numbers.append(c);
            }
        }
        return numbers.length() > 0 ? numbers.toString() : "\\n";
    }
}