/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/15/2016, 1:45am
 * CS2103
 */
package bean;

import java.util.ArrayList;

public class TaskFloat extends Task {
    public TaskFloat() {
        super();
    }

    public TaskFloat(String description, String location, ArrayList<String> tags) {
        super(description, location, tags);
    }
}
