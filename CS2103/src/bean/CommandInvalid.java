/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */

package bean;

public class CommandInvalid implements Command {
    private final String MESSAGE_INVALID_COMMAND = "You have specified an invalid command";
    private boolean updateFile = false;
    private boolean saveHistory = false;

    public CommandInvalid() {
    }

    public Display execute(Display display) {
        display.setMessage(MESSAGE_INVALID_COMMAND);
        return display;
        //return (new Display(MESSAGE_INVALID_COMMAND));
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }
    
    public boolean getUpdateFile() {
        return updateFile;
    }
}
