/*
 * @@author A0139995E
 */
package bean;

public interface Command {

	public Display execute(Display display);

	public boolean requiresUpdateFile();

	public boolean requiresSaveHistory();
}