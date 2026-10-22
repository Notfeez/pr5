package ru.edu.pr02;

import java.util.Arrays;

public class SelfCheck {
    private static int failed = 0;

    public static void main(String[] args) {
        boolean ok = false;
        try { ok = (TimesheetAnalyzer.total(new int[]{8,8,8,8,8}) == 40 && TimesheetAnalyzer.overtimeDays(new int[]{9,8,7,10,8}) == 2); } catch (Exception e) { System.out.println("FAIL exception " + e.getMessage()); }

        int[] typical = TimesheetAnalyzer.parseHours("9;8;7;10;8");
        check("типовая строка: разбор", Arrays.equals(typical, new int[]{9, 8, 7, 10, 8}));
        check("типовая строка: сумма 42", TimesheetAnalyzer.total(typical) == 42);
        check("типовая строка: среднее 8.4", Math.abs(TimesheetAnalyzer.average(typical) - 8.4) < 1e-9);
        check("типовая строка: 2 дня переработки", TimesheetAnalyzer.overtimeDays(typical) == 2);
        check("типовая строка: недельная переработка 7", TimesheetAnalyzer.weeklyOvertime(typical) == 7);

        int[] zeros = TimesheetAnalyzer.parseHours("0;0;0;0;0");
        check("нижняя граница: 0;0;0;0;0", TimesheetAnalyzer.total(zeros) == 0
                && TimesheetAnalyzer.weeklyOvertime(zeros) == 0 && TimesheetAnalyzer.overtimeDays(zeros) == 0);
        expectError("ниже границы: -1", "-1;8;8;8;8", "Позиция 1");

        int[] max = TimesheetAnalyzer.parseHours("16;16;16;16;16");
        check("верхняя граница: 16;16;16;16;16", TimesheetAnalyzer.total(max) == 80
                && TimesheetAnalyzer.overtimeDays(max) == 5 && TimesheetAnalyzer.weeklyOvertime(max) == 45);
        expectError("выше границы: 17", "8;8;17;8;8", "Позиция 3");

        expectError("пустая строка", "", "получено 1");
        expectError("null вместо строки", null, "не задана");
        expectError("пустой элемент", "8;;8;8;8", "Позиция 2");

        expectError("текст вместо числа", "8;abc;8;8;8", "Позиция 2");
        expectError("дробное число", "8;8;8;7.5;8", "Позиция 4");
        expectError("четыре значения", "8;8;8;8", "получено 4");
        expectError("шесть значений", "8;8;8;8;8;8", "получено 6");
        expectError("лишний разделитель в конце", "8;8;8;8;8;", "получено 6");

        check("8 часов - не переработка", TimesheetAnalyzer.overtimeDays(new int[]{8, 8, 8, 8, 8}) == 0);
        check("9 часов - переработка", TimesheetAnalyzer.overtimeDays(new int[]{9, 0, 0, 0, 0}) == 1);
        check("сумма ровно 35 - переработки нет", TimesheetAnalyzer.weeklyOvertime(new int[]{7, 7, 7, 7, 7}) == 0);
        check("сумма 36 - переработка 1", TimesheetAnalyzer.weeklyOvertime(new int[]{8, 7, 7, 7, 7}) == 1);
        check("сумма 40 - переработка 5", TimesheetAnalyzer.weeklyOvertime(new int[]{8, 8, 8, 8, 8}) == 5);

        check("одинаковые значения и пробелы", TimesheetAnalyzer.total(TimesheetAnalyzer.parseHours(" 8; 8;8 ;8;8 ")) == 40);

        check("отчёт при ошибке", TimesheetAnalyzer.buildReport("8;abc;8;8;8").startsWith("Ошибка: Позиция 2"));
        check("отчёт при успехе", TimesheetAnalyzer.buildReport("8;8;8;8;8").contains("недельная переработка: 5"));

        ok = ok && failed == 0;
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }

    private static void check(String name, boolean condition) {
        if (!condition) {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void expectError(String name, String line, String fragment) {
        try {
            TimesheetAnalyzer.parseHours(line);
            failed++;
            System.out.println("FAIL: " + name + " (исключения не было)");
        } catch (IllegalArgumentException e) {
            if (!e.getMessage().contains(fragment)) {
                failed++;
                System.out.println("FAIL: " + name + " (сообщение: " + e.getMessage() + ")");
            }
        }
    }
}
