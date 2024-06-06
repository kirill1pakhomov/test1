import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.Map;

class Main {
    private static final Map<Character, Integer> romanToIntMap = new LinkedHashMap<>();
    private static final Map<Integer, String> intToRomanMap = new LinkedHashMap<>();

    static {
        romanToIntMap.put('I', 1);
        romanToIntMap.put('V', 5);
        romanToIntMap.put('X', 10);
        romanToIntMap.put('L', 50);
        romanToIntMap.put('C', 100);
        romanToIntMap.put('D', 500);
        romanToIntMap.put('M', 1000);

        intToRomanMap.put(1000, "M");
        intToRomanMap.put(900, "CM");
        intToRomanMap.put(500, "D");
        intToRomanMap.put(400, "CD");
        intToRomanMap.put(100, "C");
        intToRomanMap.put(90, "XC");
        intToRomanMap.put(50, "L");
        intToRomanMap.put(40, "XL");
        intToRomanMap.put(10, "X");
        intToRomanMap.put(9, "IX");
        intToRomanMap.put(5, "V");
        intToRomanMap.put(4, "IV");
        intToRomanMap.put(1, "I");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение (например, 2 + 2 или II + II):");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                String result = calc(input);
                System.out.println("Результат: " + result);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static String calc(String input) throws Exception {
        input = input.trim();
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new Exception("Неверный формат выражения. Ожидается формат: число операция число (например, 2 + 2).");
        }

        boolean isRoman = isRoman(parts[0]) && isRoman(parts[2]);
        boolean isArabic = isArabic(parts[0]) && isArabic(parts[2]);

        if (!(isRoman || isArabic)) {
            throw new Exception("Нельзя смешивать арабские и римские числа.");
        }

        int num1, num2;
        if (isRoman) {
            num1 = romanToInt(parts[0]);
            num2 = romanToInt(parts[2]);
        } else {
            num1 = Integer.parseInt(parts[0]);
            num2 = Integer.parseInt(parts[2]);
        }

        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Числа должны быть в диапазоне от 1 до 10 включительно.");
        }

        int result = calculateResult(num1, num2, parts[1]);

        if (isRoman) {
            if (result < 1) {
                throw new Exception("Результат римских чисел не может быть меньше единицы.");
            }
            return intToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static int calculateResult(int num1, int num2, String operator) throws Exception {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> {
                if (num2 == 0) {
                    throw new Exception("Деление на ноль невозможно."); // Проверка, оставленная для безопасности
                }
                yield num1 / num2;
            }
            default -> throw new Exception("Неверная операция. Доступны операции: +, -, *, /.");
        };
    }

    private static boolean isRoman(String value) {
        return value.matches("^[IVXLCDM]+$");
    }

    private static boolean isArabic(String value) {
        return value.matches("^\\d+$");
    }

    private static int romanToInt(String roman) {
        int result = 0;
        int length = roman.length();
        for (int i = 0; i < length; i++) {
            if (i > 0 && romanToIntMap.get(roman.charAt(i)) > romanToIntMap.get(roman.charAt(i - 1))) {
                result += romanToIntMap.get(roman.charAt(i)) - 2 * romanToIntMap.get(roman.charAt(i - 1));
            } else {
                result += romanToIntMap.get(roman.charAt(i));
            }
        }
        return result;
    }

    private static String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        for (Integer key : intToRomanMap.keySet()) {
            while (num >= key) {
                sb.append(intToRomanMap.get(key));
                num -= key;
            }
        }
        return sb.toString();
    }
}
