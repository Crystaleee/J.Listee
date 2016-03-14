/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:50am
 * CS2103
 */

package bean;

public class CommandInvalid implements Command {
    private final String MESSAGE_INVALID_COMMAND = "Pls enter a valid command";
    private boolean updateFile;

    public CommandInvalid() {
        updateFile = false;
    }

    public Display execute(Display display) {
        return (new Display(MESSAGE_INVALID_COMMAND));
    }

    public boolean getUpdateFile() {
        return updateFile;
    }
}
