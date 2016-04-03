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
import parser.JListeeParser;
import storage.Storage;

public class Logic {

    private static final int TIMER_PERIOD = 1000;

    private static final int TIMER_DELAY = 0;

    private static final boolean IS_DAEMON_TASK = true;

    public static String MESSAGE_ADD_SUCCESS = "added: \"%1$s\"";

    public static final String MESSAGE_FILE_CREATED = "File created and ready for use";
    public static final String MESSAGE_ERROR_FILE_EXISTS = "File already exists";
    public static final String MESSAGE_ERROR_READING_FILE = "Error occured while reading file";
    public static final String MESSAGE_NO_DESCRIPTION = "Please enter a description";
    public static final String MESSAGE_ERROR_UPDATE_FILE = "Error occured while updating to file";

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

    public static Display initializeProgram(String filePath) {
        file = filePath;
        initializeDisplay();
        initialiseOverdueTasksReminder();
        synchronized(display){
            History.saveDisplay(display.deepClone());
        }
        initialiseNatty();
        return display;
    }

    private static void initialiseOverdueTasksReminder() {
        Timer timer = new Timer(IS_DAEMON_TASK);
        timer.schedule(new ReminderOverdue(), TIMER_DELAY, TIMER_PERIOD);
    }

    private static void initializeDisplay() {
        display = getDisplayFromStorage();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        display = new CommandShow(null, null, start, end, new ArrayList<String>()).execute(display);
        display.setMessage(null);
    }

    public static Display executeUserCommand(String userInput) {
        History.saveUserInput(userInput);
        Command userCommand = parseUserInput(userInput);
        synchronized (display) {
            display = executeCommand(userCommand);
        }
        return display;
    }

    private static void saveDisplayToHistory(Command userCommand) {
        if (userCommand.getSaveHistory()) {
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
                display.setMessage(MESSAGE_ERROR_UPDATE_FILE);
            }
        } else {
            saveDisplayToHistory(userCommand);
        }
        return display;
    }

    private static boolean requiresFileUpdate(Command userCommand) {
        return userCommand.getUpdateFile();
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
