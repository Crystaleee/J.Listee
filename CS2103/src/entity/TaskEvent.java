/*
 * @@author A0139995E
 */
package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TaskEvent extends TaskDeadline {
	private Calendar _startDate;

	// @@author A0149063E
	// String formats for the toString() method
	private static final String STRING_DESCRIPTION = "Description: %1$s\r\n";
	private static final String STRING_START_DATE = "Start Date: %1$s\r\n";
	private static final String STRING_END_DATE = "End Date: %1$s\r\n";
	private static final String STRING_LOCATION = "Location: %1$s\r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags:%1$s\r\n\r\n";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

	// @@author A0139995E
	public TaskEvent() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		this.setEndDate(null);
		this._startDate = null;
	}

	public TaskEvent(String description, String location, Calendar startDate, Calendar endDate,
			ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.setEndDate(endDate);
		this._startDate = startDate;
	}

	public void setStartDate(Calendar startDate) {
		this._startDate = startDate;
	}

	public Calendar getStartDate() {
		return _startDate;
	}

	// @@author A0149063E
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(STRING_DESCRIPTION, this.getDescription()));

		String startDate = sdf.format(this._startDate.getTime());
		sb.append(String.format(STRING_START_DATE, startDate));

		String endDate = sdf.format(this.getEndDate().getTime());
		sb.append(String.format(STRING_END_DATE, endDate));

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