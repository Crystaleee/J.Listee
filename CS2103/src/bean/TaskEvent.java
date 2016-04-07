/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */
package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class TaskEvent extends TaskDeadline {
	private Calendar startDate;

	public TaskEvent() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		this.setEndDate(null);
		this.startDate = null;
	}

	public TaskEvent(String description, String location, Calendar startDate, Calendar endDate,
			ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.setEndDate(endDate);
		this.startDate = startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	// @@author Chloe Odquier Fortuna (A0149063E)
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		String startDate = sdf.format(this.startDate.getTime());
		sb.append("Start Date: " + startDate + "\r\n");

		String endDate = sdf.format(this.getEndDate().getTime());
		sb.append("End Date: " + endDate + "\r\n");

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