/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import History.History;
import bean.Command;
import bean.CommandShow;
import bean.Display;
import bean.GlobalConstants;
import parser.JListeeParser;
import storage.Storage;
import storage.StorageFilePath;

public class Logic {
    private static Storage storage = Storage.getInstance();
    private static Display display;
    private static String file;

    public static boolean createFile(String filePath) {
        file = filePath;
        try {
            storage.createFile(filePath);
            return true;
        } catch (IOException error) {
            return false;
        }
    }

    public static Display changeFilePath(String filePath) {
        try{
            StorageFilePath.changeFilePath(filePath);
            display.setMessage("File path changed to: " + filePath);
        }catch(IOException e){
            display.setMessage("Can't change filePath");
        }
        return display;
    }

    public static Display initializeProgram(String filePath) {
        file = filePath;
        initializeDisplay();
        initialiseOverdueTasksReminder();
        synchronized (display) {
            History.saveDisplay(display.deepClone());
        }
        initialiseNatty();
        return display;
    }

    private static void initialiseOverdueTasksReminder() {
        Timer timer = new Timer(GlobalConstants.IS_DAEMON_TASK);
        timer.schedule(new ReminderOverdue(), GlobalConstants.TIMER_DELAY, GlobalConstants.TIMER_PERIOD);
    }

    /*
     * display is set to show only overdue and today's task on startup
     */
    private static void initializeDisplay() {
        display = getDisplayFromStorage();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        display = new CommandShow(null, null, start, end, new ArrayList<String>()).execute(display);
        display.setMessage("Welcome Back! These are today's agenda and overdue tasks.");
    }

    public static Display executeUserCommand(String userInput) {
        Command userCommand = parseUserInput(userInput);
        synchronized (display) {
            display = executeCommand(userCommand);
        }
        return display;
    }

    private static void saveDisplayToHistory(Command userCommand) {
        if (userCommand.requiresSaveHistory()) {
            History.saveDisplay(display.deepClone());
        }
    }

    public static Display getDisplay() {
        return display;
    }

    public static Display executeCommand(Command userCommand) {
        display = userCommand.execute(display);

        if (requiresFileUpdate(userCommand)) {
            if (successfullyUpdatesFile()) {
                saveDisplayToHistory(userCommand);
            } else {
                display.setMessage(GlobalConstants.MESSAGE_ERROR_UPDATE_FILE);
            }
        } else {
            saveDisplayToHistory(userCommand);
        }
        return display;
    }

    private static boolean requiresFileUpdate(Command userCommand) {
        return userCommand.requiresUpdateFile();
    }

    private static Command parseUserInput(String userInput) {
        JListeeParser myParser = new JListeeParser();
        Command userCommand = myParser.ParseCommand(userInput);
        return userCommand;
    }

    private static Display getDisplayFromStorage() {
        Display thisDisplay = null;
        try {
            thisDisplay = storage.getDisplay(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thisDisplay;
    }

    private static boolean successfullyUpdatesFile() {
        try {
            storage.saveFile(display);
            return true;
        } catch (IOException error) {
            return false;
        }
    }

    private static void initialiseNatty() {
        JListeeParser myParser = new JListeeParser();
        myParser.ParseCommand("add this from tmr 3pm");
    }
}
