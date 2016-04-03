/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

public interface Command {

	public Display execute(Display display);

	public boolean getUpdateFile();

	public boolean getSaveHistory();
}