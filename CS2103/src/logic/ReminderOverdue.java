package logic;

import java.util.TimerTask;

import bean.Display;

public class ReminderOverdue extends TimerTask {
    private int counter;
    private String message_overdue = "You have overdue tasks!";

    public ReminderOverdue() {
        counter = 9;
    }

    @Override
    public void run() {
        Display display = Logic.getDisplay();
        synchronized (display) {
            int numOverdue = display.setOverdueTasks();
            if (notifyUserOfOverdueTasks()) {
                if (numOverdue > 0) {
                    display.setMessage(message_overdue);
                    System.out.println("overdue");
                }
                System.out.println("this");
                counter = 0;
            }

        }
    }

    private boolean notifyUserOfOverdueTasks() {
        return ++counter == 10;
    }
}
