/*
 * @@author A0139995E
 */
package logic;

import history.History;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Command;
import entity.CommandShow;
import entity.Display;
import entity.GlobalConstants;
import entity.GlobalLogger;
import parser.JListeeParser;
import storage.Storage;
import storage.StorageFilePath;

public class Logic {
    private static Storage storage = Storage.getInstance();
    private static Display display;
    private static String file;
    private static Logger logger = GlobalLogger.getLogger();

    public static boolean createFile(String filePath) {
        logger.log(Level.INFO, "Logic: Create filepath " + filePath);
        file = filePath;
        try {
            storage.createFile(filePath);
            return true;
        } catch (IOException error) {
            return false;
        }
    }

    public static Display changeFilePath(String filePath) {
        logger.log(Level.INFO, "Logic: Change filepath" + filePath);
        try{
        	file = filePath;
            StorageFilePath.changeFilePath(filePath);
            initializeDisplay();
            display.setMessage(GlobalConstants.MESSAGE_FILE_PATH_CHANGE + filePath);
        }catch(IOException e){
            display.setMessage(GlobalConstants.MESSAGE_ERROR_CHANGE_FILE_PATH);
        }
        return display;
    }

    public static Display initializeProgram(String filePath) {
        logger.log(Level.INFO, "Logic: Initialise Program" + filePath);
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
        display.setMessage(GlobalConstants.MESSAGE_START_UP);
    }

    public static Display executeUserCommand(String userInput) {
        logger.log(Level.INFO, "Logic: Parsing user input " + userInput);
        Command userCommand = parseUserInput(userInput);
        assert userCommand != null: "Null Command";
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
        logger.log(Level.INFO, "Logic: Executing command ");
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
            assert thisDisplay != null: "Logic: Null display from storage";
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
        myParser.ParseCommand(GlobalConstants.MESSAGE_NATTY);
    }
}
