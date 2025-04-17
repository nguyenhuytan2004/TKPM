package com.example.Project.shared.util;

public class StringHelper {

    // Example: "Hello World" -> "hello-world"
    // Example: "HelloWorld" -> "hello-world"
    public static String toKebabCase(String input) {
        if (input == null || input.isEmpty())
            return "";

        // Bước 1: Replace khoảng trắng bằng dấu gạch ngang
        String kebab = input.replaceAll("\\s+", "-");

        // Bước 2: Thêm gạch ngang giữa các từ PascalCase (chữ hoa chuyển chữ thường)
        kebab = kebab.replaceAll("([a-z])([A-Z])", "$1-$2");

        // Bước 3: Đưa về lowercase
        kebab = kebab.toLowerCase();

        return kebab;
    }

    // Example: "Hello World" -> "HelloWorld"
    // Example: "hello-world" -> "HelloWorld"
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty())
            return "";

        // Thay các ký tự phân cách (gạch ngang, gạch dưới, khoảng trắng) thành khoảng
        // trắng tạm
        String normalized = input.trim().replaceAll("[-_\\s]+", " ");

        StringBuilder result = new StringBuilder();
        for (String word : normalized.split(" ")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
            }
        }

        return result.toString();
    }

    // Example: "hello-world" -> Hello World
    // Example: "helloWorld" -> Hello World
    public static String toNormalCase(String input) {
        if (input == null || input.isEmpty())
            return "";

        // Bước 1: Thay thế dấu gạch ngang bằng khoảng trắng
        String normal = input.replaceAll("-", " ");

        // Bước 2: Thêm khoảng trắng giữa các từ PascalCase
        normal = normal.replaceAll("([a-z])([A-Z])", "$1 $2");

        // Bước 3: Đưa về dạng chữ hoa đầu câu
        String[] words = normal.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }

        return result.toString().trim();
    }
}
