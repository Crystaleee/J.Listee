/*
 * @@author A0139995E
 */
package logic;

import java.util.Calendar;
import java.util.TimerTask;

import bean.Display;
import bean.GlobalConstants;
import gui.GUIController;
import javafx.application.Platform;

public class ReminderOverdue extends TimerTask {
    // private int counter;
    private String message_overdue = "You have overdue tasks!";
    private int numOverdue = 0;
    private int minute = Calendar.getInstance().get(Calendar.MINUTE);

    public ReminderOverdue() {
        // counter = GlobalConstants.TIMER_REMINDER_PERIOD -1;
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                // System.out.println(counter);
                Display display = Logic.getDisplay();
                synchronized (display) {
                    int overdueTasks = display.setOverdueTasks();
                    if (notifyUserOfOverdueTasks(overdueTasks)) {
                        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
                        numOverdue = overdueTasks;
                        display.setMessage(message_overdue);
                        GUIController.displayList(display);
                        minute = Calendar.getInstance().get(Calendar.MINUTE);
                        // counter = 0;
                    }

                }
            }
        });
    }

    private boolean notifyUserOfOverdueTasks(int overdueTasks) {
        if (Calendar.getInstance().get(Calendar.MINUTE) != minute) {
            if (overdueTasks != numOverdue) {
                return true;
            }
        }
        return false;
    }
}
