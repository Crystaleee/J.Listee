/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Task implements java.io.Serializable {
	private String description;
	private String location;
	private ArrayList<String> tags;
	private boolean isOverdue;

	// @@author A0149063E
	private static final String STRING_DESCRIPTION = "Description: %1$s \r\n";
	private static final String STRING_LOCATION = "Location: %1$s \r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags: %1$s \r\n\r\n";

	// @@author A0139995E
	public Task() {
		description = null;
		location = null;
		tags = null;
	}

	public Task(String description, String location, ArrayList<String> tags) {
		this.description = description;
		this.location = location;
		this.tags = tags;
	}

	public void setIsOverdue(boolean isOverdue) {
		this.isOverdue = isOverdue;
	}

	public boolean isOverdue() {
		return isOverdue;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
