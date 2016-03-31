/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 3:00am
 * CS2103
 */
package bean;

import java.util.Calendar;

import History.History;

public class CommandRedo implements Command {
	private final String MESSAGE_REDO = "Redid last command";
	private final String MESSAGE_ERROR_REDO = "You have reached the latest point possible";
	private boolean updateFile = true;
	private boolean saveHistory = false;
    private int count;
    
    public CommandRedo() {
        count = 1;
    }

    public CommandRedo(int count) {
        this.count = count;
    }

	public Display execute(Display display) {
        Display nextDisplay = History.getDisplay(count);
        if(nextDisplay == null){
            updateFile = false;
            display.setMessage(MESSAGE_ERROR_REDO);
            return display;
        }
        display = nextDisplay.deepClone();
        display.setOverdueTasks();
		display.setMessage(MESSAGE_REDO);
		return display;
	}
    
    

	public boolean getSaveHistory() {
		return saveHistory;
	}

	public boolean getUpdateFile() {
		return updateFile;
	}
}
