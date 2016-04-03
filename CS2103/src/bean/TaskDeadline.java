/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDeadline extends Task {
	private Calendar endDate;

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

	// @@author Chloe Odquier Fortuna (A0149063E)
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		String dateString = sdf.format(this.endDate.getTime());
		sb.append("Deadline: " + dateString + "\r\n");

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