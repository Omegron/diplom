package com.example.diplom.Diary;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.CalendarUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChecksDataPush extends AppCompatActivity {

    private DiaryDB database;
    private Tasks taskD;
    private Checks check;
    private final List<String> tasks = Arrays.asList("Работа", "Завдання 10", "Завдання 9", "Завдання 8", "Завдання 7",
            "Завдання 6", "Завдання 5", "Завдання 4", "Завдання 3", "Завдання 2", "Завдання 1");

    void tasksDBDataPush(Context context) {

        database = DiaryDB.getInstance(context);
        if (database.diaryDAO().getAllTasks().isEmpty()) {
            for (String task : tasks) {
                taskD = new Tasks();
                taskD.setTask(task);
                database.diaryDAO().insertTask(taskD);
            }
        }

    }

    void checksForDayPush(LocalDate date) {

        List<Tasks> tasks = database.diaryDAO().getAllTasks();

        List<Checks> checks = database.diaryDAO().getDayC(CalendarUtils.formattedDate(date));

        if (!checks.isEmpty()) {
            for (int i = 0; i < tasks.size(); i++) {
                boolean temp = false;
                for (int j = 0; j < checks.size(); j++) {
                    if (Objects.equals(tasks.get(i).getTask(), checks.get(j).getTask())) {
                        temp = true;
                        break;
                    }
                }
                if (!temp) {
                    check = new Checks();
                    check.setTask(tasks.get(i).getTask());
                    check.setDate(CalendarUtils.formattedDate(date));
                    database.diaryDAO().insertCheck(check);
                    checks.add(check);
                }
            }
        } else {
            for (Tasks task : tasks) {
                check = new Checks();
                check.setTask(task.getTask());
                check.setDate(CalendarUtils.formattedDate(date));
                database.diaryDAO().insertCheck(check);
            }
        }
    }

    void deleteChecksForDay(String task) {

        database.diaryDAO().deleteByTaskT(task);
        database.diaryDAO().deleteByTaskC(task);

    }

    void updateChecksForDay(String taskOld, String taskNew) {

        database.diaryDAO().updateTaskT(taskOld, taskNew);
        database.diaryDAO().updateTaskC(taskOld, taskNew);

    }

    void insertChecksForDay(String task, LocalDate date) {

        taskD = new Tasks();
        taskD.setTask(task);
        database.diaryDAO().insertTask(taskD);

        check = new Checks();
        check.setTask(task);
        check.setDate(CalendarUtils.formattedDate(date));
        database.diaryDAO().insertCheck(check);
    }

}
