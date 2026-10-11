
package collegio.com;

import java.util.Date;

public class Task {

    private String taskId;
    private String title;
    private Date dueDate;
    private String status;
    private int reminderMinutes;

    public Task(String taskId, String title, Date dueDate,
                String status, int reminderMinutes) {
        this.taskId = taskId;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
        this.reminderMinutes = reminderMinutes;
    }

    public Task(String taskId, String title, Date dueDate,
                String status) {
        this(taskId, title, dueDate, status, 15);
    }

    public void addTask() {
        System.out.println("Task added: " + title);
    }

    public void updateStatus(String newStatus) {
        status = newStatus;
    }

    public void deleteTask() {
        System.out.println("Task deleted: " + title);
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }
}
