/*
 * @@author A0139995E
 */
package logic;
/**
 * This class serves as the interface between ui and 
 * the sub components. 
 */
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

public class Logic {
    private static Storage _storage = Storage.getInstance();
    private static Display _display;
    private static String _file;
    private static Logger _logger = GlobalLogger.getLogger();

    /*
     * call storage to create text file specified by the filepath
     */
    public static boolean createFile(String filePath) {
        _logger.log(Level.INFO, "Logic: Create filepath " + filePath);
        _file = filePath;
        try {
            _storage.createFile(filePath);
            return true;
        } catch (IOException error) {
            return false;
        }
    }

    /*
     * call storage to change the file path of text file
     */
    public static Display changeFilePath(String filePath) {
        _logger.log(Level.INFO, "Logic: Change filepath" + filePath);
        try{
        	_file = filePath;
            _storage.changeFilePath(filePath);
            initializeDisplay();
            _display.setMessage(GlobalConstants.MESSAGE_FILE_PATH_CHANGE + filePath);
        }catch(IOException e){
            _display.setMessage(GlobalConstants.MESSAGE_ERROR_CHANGE_FILE_PATH);
        }
        return _display;
    }

    /*
     * sets up the program on first start up of the program
     */
    public static Display initializeProgram(String filePath) {
        _logger.log(Level.INFO, "Logic: Initialise Program" + filePath);
        _file = filePath;
        initializeDisplay();
        initialiseOverdueTasksReminder();
        synchronized (_display) {
            History.saveDisplay(_display.deepClone());
        }
        initialiseNatty();
        return _display;
    }

    /*
     * initialise the timertask to constantly poll
     * for overdue tasks
     */
    private static void initialiseOverdueTasksReminder() {
        Timer timer = new Timer(GlobalConstants.IS_DAEMON_TASK);
        timer.schedule(new ReminderOverdue(), GlobalConstants.TIMER_DELAY, GlobalConstants.TIMER_PERIOD);
    }

    /*
     * display is set to show only overdue and today's task on startup
     */
    private static void initializeDisplay() {
        _display = getDisplayFromStorage();
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        _display = new CommandShow(null, null, start, end, new ArrayList<String>()).execute(_display);
        _display.setMessage(GlobalConstants.MESSAGE_START_UP);
    }

    /*
     * called by ui whenever user enters a command and
     * executes it
     */
    public static Display executeUserCommand(String userInput) {
        _logger.log(Level.INFO, "Logic: Parsing user input " + userInput);
        Command userCommand = parseUserInput(userInput);
        assert userCommand != null: "Null Command";
        synchronized (_display) {
            _display = executeCommand(userCommand);
        }
        return _display;
    }

    private static void saveDisplayToHistory(Command userCommand) {
        if (userCommand.requiresSaveHistory()) {
            History.saveDisplay(_display.deepClone());
        }
    }

    public static Display getDisplay() {
        return _display;
    }

    public static Display executeCommand(Command userCommand) {
        _logger.log(Level.INFO, "Logic: Executing command ");
        _display = userCommand.execute(_display);

        if (requiresFileUpdate(userCommand)) {
            if (successfullyUpdatesFile()) {
                saveDisplayToHistory(userCommand);
            } else {
                _display.setMessage(GlobalConstants.MESSAGE_ERROR_UPDATE_FILE);
            }
        } else {
            saveDisplayToHistory(userCommand);
        }
        return _display;
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
            thisDisplay = _storage.getDisplay(_file);
            assert thisDisplay != null: "Logic: Null display from storage";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thisDisplay;
    }

    private static boolean successfullyUpdatesFile() {
        try {
            _storage.saveFile(_display);
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
