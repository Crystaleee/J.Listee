/*
 * @@author A0139995E
 */
package entity;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Task implements java.io.Serializable {
	private String _description;
	private String _location;
	private ArrayList<String> _tags;
	private boolean _isOverdue;

	// @@author A0149063E
	// String formats for the toString() method
	private static final String STRING_DESCRIPTION = "Description: %1$s\r\n";
	private static final String STRING_LOCATION = "Location: %1$s\r\n";
	private static final String STRING_TAG = " #%1$s";
	private static final String STRING_TAGS = "Tags:%1$s\r\n\r\n";

	// @@author A0139995E
	public Task() {
		_description = null;
		_location = null;
		_tags = null;
	}

	public Task(String description, String location, ArrayList<String> tags) {
		this._description = description;
		this._location = location;
		this._tags = tags;
	}

	public void setIsOverdue(boolean isOverdue) {
		this._isOverdue = isOverdue;
	}

	public boolean isOverdue() {
		return _isOverdue;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	public String getDescription() {
		return _description;
	}

	public void setTags(ArrayList<String> tags) {
		this._tags = tags;
	}

	public ArrayList<String> getTags() {
		return _tags;
	}

	public String getLocation() {
		return _location;
	}

	public void setLocation(String location) {
		this._location = location;
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
