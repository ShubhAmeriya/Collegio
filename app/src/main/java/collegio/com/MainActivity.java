
package collegio.com;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText taskTitleInput;
    private Button addTaskButton, dueDateButton;
    private Spinner reminderSpinner;
    private LinearLayout taskContainer;
    private TextView taskSummary;

    private final ArrayList<Task> tasks = new ArrayList<>();
    private Calendar selectedDateTime = Calendar.getInstance();

    private final String[] reminderOptions = {
            "15 minutes before",
            "1 hour before",
            "1 day before"
    };

    private final int[] reminderMinutes = {15, 60, 1440};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskTitleInput = findViewById(R.id.taskTitleInput);
        addTaskButton = findViewById(R.id.addTaskButton);
        dueDateButton = findViewById(R.id.dueDateButton);
        taskContainer = findViewById(R.id.taskContainer);
        taskSummary = findViewById(R.id.taskSummary);
        reminderSpinner = findViewById(R.id.reminderSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                reminderOptions
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        reminderSpinner.setAdapter(adapter);

        dueDateButton.setOnClickListener(v -> openDateTimePicker());
        addTaskButton.setOnClickListener(v -> addNewTask());

        requestNotificationPermission();
        updateSummary();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33
                && checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    },
                    101
            );
        }
    }

    private void openDateTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    selectedDateTime.set(
                            year, month, day,
                            selectedDateTime.get(
                                    Calendar.HOUR_OF_DAY
                            ),
                            selectedDateTime.get(
                                    Calendar.MINUTE
                            ),
                            0
                    );

                    Calendar currentTime = Calendar.getInstance();

                    TimePickerDialog timePicker =
                            new TimePickerDialog(
                                    this,
                                    (timeView, hour, minute) -> {
                                        selectedDateTime.set(
                                                Calendar.HOUR_OF_DAY,
                                                hour
                                        );
                                        selectedDateTime.set(
                                                Calendar.MINUTE,
                                                minute
                                        );
                                        selectedDateTime.set(
                                                Calendar.SECOND, 0
                                        );

                                        updateDateButton();
                                    },
                                    currentTime.get(
                                            Calendar.HOUR_OF_DAY
                                    ),
                                    currentTime.get(
                                            Calendar.MINUTE
                                    ),
                                    false
                            );

                    timePicker.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }

    private void updateDateButton() {
        String formatted = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
        ).format(selectedDateTime.getTime());

        dueDateButton.setText(formatted);
    }

    private void addNewTask() {
        String title = taskTitleInput.getText()
                .toString().trim();

        if (title.isEmpty()) {
            taskTitleInput.setError("Enter a task title");
            return;
        }

        if (selectedDateTime.getTimeInMillis()
                <= System.currentTimeMillis()) {
            Toast.makeText(
                    this,
                    "Choose a future due date and time",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        int position = reminderSpinner.getSelectedItemPosition();
        int minutes = reminderMinutes[position];

        Task task = new Task(
                String.valueOf(System.currentTimeMillis()),
                title,
                selectedDateTime.getTime(),
                "Pending",
                minutes
        );

        tasks.add(task);
        task.addTask();

        scheduleReminder(task);

        taskTitleInput.setText("");
        selectedDateTime = Calendar.getInstance();
        dueDateButton.setText("Choose due date & time");

        displayTasks();
        updateSummary();
    }

    private PendingIntent getReminderPendingIntent(Task task) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("taskId", task.getTaskId());
        intent.putExtra("title", task.getTitle());

        return PendingIntent.getBroadcast(
                this,
                task.getTaskId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private void scheduleReminder(Task task) {
        long triggerAt = task.getDueDate().getTime()
                - task.getReminderMinutes() * 60_000L;

        if (triggerAt <= System.currentTimeMillis()) {
            Toast.makeText(
                    this,
                    "Deadline is too close for this reminder. "
                            + "Choose an earlier reminder or a later deadline.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent =
                getReminderPendingIntent(task);

        try {
            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
            );

            Toast.makeText(
                    this,
                    "Task reminder scheduled",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (SecurityException e) {
            Toast.makeText(
                    this,
                    "Unable to schedule reminder",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void cancelReminder(Task task) {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(getReminderPendingIntent(task));
    }

    private void displayTasks() {
        taskContainer.removeAllViews();

        for (Task task : tasks) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(18, 16, 18, 16);

            GradientDrawable background = new GradientDrawable();
            background.setColor(Color.WHITE);
            background.setCornerRadius(24);
            background.setStroke(
                    1, Color.rgb(230, 231, 240)
            );
            card.setBackground(background);

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            cardParams.setMargins(0, 0, 0, 14);
            card.setLayoutParams(cardParams);

            TextView titleView = new TextView(this);
            titleView.setText(task.getTitle());
            titleView.setTextSize(17);
            titleView.setTextColor(Color.rgb(32, 35, 58));
            titleView.setTypeface(null, Typeface.BOLD);

            if ("Completed".equals(task.getStatus())) {
                titleView.setPaintFlags(
                        titleView.getPaintFlags()
                                | Paint.STRIKE_THRU_TEXT_FLAG
                );
            }

            TextView details = new TextView(this);

            String due = new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
            ).format(task.getDueDate());

            details.setText(
                    "Due: " + due
                            + "\nStatus: " + task.getStatus()
                            + "\nReminder: "
                            + reminderOptions[
                            getReminderIndex(task)
                            ]
            );

            details.setTextSize(13);
            details.setTextColor(Color.rgb(108, 99, 255));
            details.setPadding(0, 8, 0, 12);

            LinearLayout actions = new LinearLayout(this);
            actions.setOrientation(LinearLayout.HORIZONTAL);
            actions.setGravity(Gravity.CENTER_VERTICAL);

            Button statusButton = new Button(this);
            Button deleteButton = new Button(this);

            statusButton.setText(
                    "Completed".equals(task.getStatus())
                            ? "Undo" : "Complete"
            );
            statusButton.setTextSize(12);

            deleteButton.setText("Delete");
            deleteButton.setTextSize(12);

            statusButton.setOnClickListener(v -> {
                if ("Completed".equals(task.getStatus())) {
                    task.updateStatus("Pending");
                    scheduleReminder(task);
                } else {
                    task.updateStatus("Completed");
                    cancelReminder(task);
                }

                displayTasks();
                updateSummary();
            });

            deleteButton.setOnClickListener(v -> {
                cancelReminder(task);
                task.deleteTask();
                tasks.remove(task);

                displayTasks();
                updateSummary();
            });

            actions.addView(statusButton);
            actions.addView(deleteButton);

            card.addView(titleView);
            card.addView(details);
            card.addView(actions);

            taskContainer.addView(card);
        }
    }

    private int getReminderIndex(Task task) {
        int minutes = task.getReminderMinutes();

        if (minutes == 60) return 1;
        if (minutes == 1440) return 2;
        return 0;
    }

    private void updateSummary() {
        int completed = 0;

        for (Task task : tasks) {
            if ("Completed".equals(task.getStatus())) {
                completed++;
            }
        }

        taskSummary.setText(
                tasks.size() + " total tasks • "
                        + completed + " completed"
        );
    }
}
