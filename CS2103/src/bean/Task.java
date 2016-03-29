/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:55pm
 * CS2103
 */
package bean;

import java.util.ArrayList;

public class Task {
	private String description;
	private String location;
	private ArrayList<String> tags;

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

//	// @@author Chloe Odquier Fortuna (A0149063E)
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Description: " + this.getDescription() + "\r\n");
//		String location = this.getLocation();
//		if (location == null) {
//			location = "";
//		}
//		sb.append("Location: " + location + "\r\n");
//
//		ArrayList<String> tagsList = this.getTags();
//		String tagsString = "";
//		for (String tag : tagsList) {
//			tagsString += " #" + tag;
//		}
//
//		sb.append("Tags:" + tagsString + "\r\n");
//		sb.append("\r\n");
//		return sb.toString();
//	}
}
