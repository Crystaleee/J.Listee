/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 2:40am
 * CS2103
 */
package bean;

public interface Command {

    public Display execute(Display display);

    public boolean getUpdateFile();
}