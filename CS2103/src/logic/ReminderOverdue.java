package logic;

import java.util.Calendar;
import java.util.TimerTask;

import bean.Display;
import bean.TaskDeadline;
import bean.TaskEvent;

public class ReminderOverdue extends TimerTask {
    private int counter;
    private int numOverdue = 0;
    private String message_overdue = "You have overdue tasks!";

    public ReminderOverdue() {
        counter = 9;
    }

    @Override
    public void run() {
        Display display = Logic.getDisplay();
        synchronized (display) {
            setOverdueTasks(display);
            if (++counter == 10) {
                if (numOverdue > 0) {
                    display.setMessage(message_overdue);
                }
                counter = 0;
            }

        }
    }

    private void setOverdueTasks(Display display) {
        if (display.getDeadlineTasks() != null) {
            for (int i = 0; i < display.getDeadlineTasks().size(); i++) {
                TaskDeadline task = display.getDeadlineTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    System.out.println(task.getDescription() + " is overdue");
                    numOverdue++;
                    task.setIsOverdue(true);
                }

            }
        }
        if (display.getEventTasks() != null) {
            for (int i = 0; i < display.getEventTasks().size(); i++) {
                TaskEvent task = display.getEventTasks().get(i);
                if (task.getEndDate().before(Calendar.getInstance())) {
                    System.out.println(task.getDescription() + " is overdue");
                    numOverdue++;
                    task.setIsOverdue(true);
                }

            }
        }
    }

}
