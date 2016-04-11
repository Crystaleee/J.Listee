/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class TaskFloat extends Task {
	// @@author A0149063E
	// String formats for the toString() method
	private static final String STRING_DESCRIPTION = "Description: %1$s\r\n";
	private static final String STRING_LOCATION = "Location: %1$s\r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags:%1$s\r\n\r\n";

	// @@author A0139995E
	public TaskFloat() {
		super();
	}

	public TaskFloat(String description, String location, ArrayList<String> tags) {
		super(description, location, tags);
	}

	// @@author A0149063E
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format(STRING_DESCRIPTION, this.getDescription()));
		String location = this.getLocation();

		if (location == null) {
			location = "";
		}
		sb.append(String.format(STRING_LOCATION, location));

		ArrayList<String> tagsList = this.getTags();
		String tagsString = "";
		for (String tag : tagsList) {
			tagsString += String.format(STRING_TAG, tag);
		}

		sb.append(String.format(STRING_TAGS, tagsString));
		return sb.toString();
	}

}
