package ru.edu.pr02;

import java.util.Locale;

public class TimesheetAnalyzer {
    public static int[] parseHours(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Строка табеля не задана (null)");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != TimesheetRules.DAYS_COUNT) {
            throw new IllegalArgumentException("Ожидалось " + TimesheetRules.DAYS_COUNT
                    + " значений, получено " + parts.length);
        }
        int[] hours = new int[TimesheetRules.DAYS_COUNT];
        for (int i = 0; i < parts.length; i++) {
            hours[i] = parseValue(parts[i], i + 1);
        }
        return hours;
    }

    private static int parseValue(String text, int position) {
        String s = text.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Позиция " + position + ": пустое значение");
        }
        int value;
        try {
            value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Позиция " + position + ": '" + s
                    + "' не является целым числом");
        }
        if (value < TimesheetRules.MIN_HOURS || value > TimesheetRules.MAX_HOURS) {
            throw new IllegalArgumentException("Позиция " + position + ": значение " + value
                    + " вне диапазона " + TimesheetRules.MIN_HOURS + ".." + TimesheetRules.MAX_HOURS);
        }
        return value;
    }

    public static int total(int[] hours) {
        int sum = 0;
        for (int i = 0; i < hours.length; i++) {
            sum += hours[i];
        }
        return sum;
    }

    public static double average(int[] hours) {
        if (hours.length == 0) {
            return 0.0;
        }
        return (double) total(hours) / hours.length;
    }

    public static int overtimeDays(int[] hours) {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] > TimesheetRules.DAILY_NORM) {
                count++;
            }
        }
        return count;
    }

    public static int weeklyOvertime(int[] hours) {
        return Math.max(0, total(hours) - TimesheetRules.WEEKLY_NORM);
    }

    public static String buildReport(String line) {
        try {
            int[] hours = parseHours(line);
            return String.format(Locale.ROOT,
                    "Сумма: %d ч, среднее: %.2f ч, дней с переработкой: %d, недельная переработка: %d ч",
                    total(hours), average(hours), overtimeDays(hours), weeklyOvertime(hours));
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        }
    }
}
