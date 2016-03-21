/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 21 Mar, 11:00pm
 */

package bean;

public class CommandInvalid implements Command {
    private final String MESSAGE_INVALID_COMMAND = "Pls enter a valid command";
    private boolean updateFile;
    private boolean saveHistory = false;

    public CommandInvalid() {
        updateFile = false;
    }

    public Display execute(Display display) {
        return (new Display(MESSAGE_INVALID_COMMAND));
    }

    public boolean getSaveHistory() {
        return saveHistory;
    }
    
    public boolean getUpdateFile() {
        return updateFile;
    }
}
