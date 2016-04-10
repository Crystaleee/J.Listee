/*
 * @@author A0139995E
 */
package bean;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class TaskFloat extends Task {
	public TaskFloat() {
		super();
	}

	public TaskFloat(String description, String location, ArrayList<String> tags) {
		super(description, location, tags);
	}

	// @@author A0149063E
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");
		String location = this.getLocation();
		if (location == null) {
			location = "";
		}
		sb.append("Location: " + location + "\r\n");

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += " #" + tag;
		}

		sb.append("Tags:" + tagsString + "\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
