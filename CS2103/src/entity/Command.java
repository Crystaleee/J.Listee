/*
 * @@author A0139995E
 */
package entity;
/**
 * The interface which all commands implement.
 */
public interface Command {

	public Display execute(Display display);

    /*
     * Since not all commands require saving to storage,
     * this is used to check if that command requires saving to storage
     */
	public boolean requiresUpdateFile();

    /*
     * Since not all commands require saving to history,
     * this is used to check if that command requires saving to history
     */
	public boolean requiresSaveHistory();
}