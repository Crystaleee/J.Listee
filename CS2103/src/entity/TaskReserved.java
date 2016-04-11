/*
 * @@author A0139995E
 */

package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TaskReserved extends Task {
	private ArrayList<Calendar> _startDates;
	private ArrayList<Calendar> _endDates;

	// @@author A0149063E
	// String formats for the toString() method
	private static final String STRING_DESCRIPTION = "Description: %1$s\r\n";
	private static final String STRING_START_DATES = "Start Dates: %1$s\r\n";
	private static final String STRING_END_DATES = "End Dates: %1$s\r\n";
	private static final String STRING_LOCATION = "Location: %1$s\r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags:%1$s\r\n\r\n";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

	// @@author A0139995E
	public TaskReserved() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		_startDates = null;
		_endDates = null;
	}

	public TaskReserved(String description, String location, ArrayList<Calendar> startDates,
			ArrayList<Calendar> endDates, ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this._startDates = startDates;
		this._endDates = endDates;
	}

	public void setStartDates(ArrayList<Calendar> startDates) {
		this._startDates = startDates;
	}

	public ArrayList<Calendar> getStartDates() {
		return _startDates;
	}

	public void setEndDates(ArrayList<Calendar> endDates) {
		this._endDates = endDates;
	}

	public ArrayList<Calendar> getEndDates() {
		return _endDates;
	}

	// @@author A0149063E
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(STRING_DESCRIPTION, this.getDescription()));

		String startDates = "";
		for (int i = 0; i < this._startDates.size(); i++) {
			if (i == this._startDates.size() - 1) {
				startDates += sdf.format(this._startDates.get(i).getTime());
			} else {
				startDates += sdf.format(this._startDates.get(i).getTime()) + ", ";
			}
		}
		sb.append(String.format(STRING_START_DATES, startDates));

		String endDates = "";
		for (int i = 0; i < this._endDates.size(); i++) {
			if (i == this._endDates.size() - 1) {
				endDates += sdf.format(this._endDates.get(i).getTime());
			} else {
				endDates += sdf.format(this._endDates.get(i).getTime()) + ", ";
			}
		}
		sb.append(String.format(STRING_END_DATES, endDates));

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
