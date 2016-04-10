/*
 * @@author A0139995E
 */
package logic;

import java.util.Calendar;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Display;
import entity.GlobalConstants;
import entity.GlobalLogger;
import gui.GUIController;
import javafx.application.Platform;

public class ReminderOverdue extends TimerTask {
    private String message_overdue = "You have overdue tasks!";
    private int numOverdue = 0;
    private int minute = Calendar.getInstance().get(Calendar.MINUTE);
    private Logger logger = GlobalLogger.getLogger();

    public ReminderOverdue() {
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                Display display = Logic.getDisplay();
                assert display != null: "Reminder: null display";
                synchronized (display) {
                    int overdueTasks = display.setOverdueTasks();
                    if (notifyUserOfOverdueTasks(overdueTasks)) {
                        logger.log(Level.INFO, "Reminder: Notify user of overdue tasks");
                        display.setCommandType(GlobalConstants.GUI_ANIMATION_INVALID);
                        numOverdue = overdueTasks;
                        display.setMessage(message_overdue);
                        GUIController.displayList(display);
                        minute = Calendar.getInstance().get(Calendar.MINUTE);
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
