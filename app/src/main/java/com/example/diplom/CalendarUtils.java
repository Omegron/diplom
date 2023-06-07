package com.example.diplom;

import androidx.core.content.res.TypedArrayUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static LocalDate formattedDateReverse(String dateS) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return LocalDate.parse(dateS, formatter);
    }

    public static String formattedDateUa(LocalDate date) {
        DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("dd ");
        DateTimeFormatter formatterY = DateTimeFormatter.ofPattern(" yyyy");
        return date.format(formatterD) + monthUa(date) + date.format(formatterY);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy");
        return monthUa(date) + date.format(formatter);
    }

    public static String monthDayFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d");
        return monthUa(date) + date.format(formatter);
    }

    public static String monthDayName(DayOfWeek dayOfWeek) {
        return dayUa(dayOfWeek);
    }

    public static ArrayList<LocalDate> daysInMonthArray() {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate prevMonth = selectedDate.minusMonths(1);

        YearMonth prevYearMonth = YearMonth.from(prevMonth);
        int prevDaysInMonth = prevYearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;


        for(int i = 1; i <= 42; i++) {
            if(i <= dayOfWeek) {
                daysInMonthArray.add(LocalDate.of(prevMonth.getYear(),prevMonth.getMonth(),prevDaysInMonth + i - dayOfWeek));
            } else if(i > daysInMonth + dayOfWeek) {
                break;
            } else {
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = mondayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate mondayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if(current.getDayOfWeek() == DayOfWeek.MONDAY) {
                return current;
            }
            current = current.minusDays(1);
        }
        return null;
    }

    public static String monthUa(LocalDate date) {
        String[] monthEn = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        String[] monthUa = {"Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень", "Листопад", "Грудень"};

        String month = "";
        for (int i = 0; i < monthEn.length; i++) {
            if (monthEn[i].equals(date.getMonth().toString())) {
                month = monthUa[i];
                break;
            }
        }
        return month;
    }

    public static String dayUa(DayOfWeek dayOfWeek) {
        String[] dayEn = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        String[] dayUa = {"Понеділок", "Вівторок", "Середа", "Четвер", "П'ятниця", "Субота", "Неділя"};

        String day = "";
        for (int i = 0; i < dayEn.length; i++) {
            if (dayEn[i].equals(dayOfWeek.toString())) {
                day = dayUa[i];
                break;
            }
        }
        return day;
    }
}