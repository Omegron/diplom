package com.example.diplom.Diary;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.CalendarUtils;
import com.example.diplom.Diary.ChecksTasks.Tasks;
import com.example.diplom.Diary.ChecksTasks.TasksDB;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChecksDataPush extends AppCompatActivity {

    private TasksDB databaseT;
    private Tasks taskD;
    private ChecksDB databaseC;
    private Checks check;
    private final List<String> tasks = Arrays.asList("Работа", "Завдання 10", "Завдання 9", "Завдання 8", "Завдання 7",
            "Завдання 6", "Завдання 5", "Завдання 4", "Завдання 3", "Завдання 2", "Завдання 1");

    void tasksDBDataPush(Context context) {

        databaseT = TasksDB.getInstance(context);
        if (databaseT.tasksDAO().getAll().isEmpty()) {
            for (String task : tasks) {
                taskD = new Tasks();
                taskD.setTask(task);
                databaseT.tasksDAO().insert(taskD);
            }
        }

    }

    void checksForDayPush(Context context, LocalDate date) {

        databaseC = ChecksDB.getInstance(context);
        List<Tasks> tasks = databaseT.tasksDAO().getAll();

        List<Checks> checks = databaseC.checksDAO().getDay(CalendarUtils.formattedDate(date));

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
                    databaseC.checksDAO().insert(check);
                    checks.add(check);
                }
            }
        } else {
            for (Tasks task : tasks) {
                check = new Checks();
                check.setTask(task.getTask());
                check.setDate(CalendarUtils.formattedDate(date));
                databaseC.checksDAO().insert(check);
            }
        }
    }

    void deleteChecksForDay(String task) {

        databaseT.tasksDAO().deleteByTask(task);
        databaseC.checksDAO().deleteByTask(task);

    }

    void updateChecksForDay(String taskOld, String taskNew) {

        databaseT.tasksDAO().updateTask(taskOld, taskNew);
        databaseC.checksDAO().updateTask(taskOld, taskNew);

    }

    void insertChecksForDay(String task, LocalDate date) {

        taskD = new Tasks();
        taskD.setTask(task);
        databaseT.tasksDAO().insert(taskD);

        check = new Checks();
        check.setTask(task);
        check.setDate(CalendarUtils.formattedDate(date));
        databaseC.checksDAO().insert(check);
    }

}
