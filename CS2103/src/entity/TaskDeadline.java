/*
 * @@author A0139995E
 */
package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TaskDeadline extends Task {
	private Calendar endDate;

	// @@author A0149063E
	private static final String STRING_DESCRIPTION = "Description: %1$s \r\n";
	private static final String STRING_DEADLINE = "Deadline: %1$s\r\n";
	private static final String STRING_LOCATION = "Location: %1$s \r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags: %1$s \r\n\r\n";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

	// @@author A0139995E
	public TaskDeadline() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		this.endDate = null;
	}

	public TaskDeadline(String description, String location, Calendar endDate, ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.endDate = endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	// @@author A0149063E
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(STRING_DESCRIPTION, this.getDescription()));

		String dateString = sdf.format(this.endDate.getTime());
		sb.append(String.format(STRING_DEADLINE, dateString));

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