/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 */

package bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskReserved extends Task {
	private ArrayList<Calendar> startDates;
	private ArrayList<Calendar> endDates;

	public TaskReserved() {
		this.setDescription(null);
		this.setLocation(null);
		this.setTags(null);
		startDates = null;
		endDates = null;
	}

	public TaskReserved(String description, String location, ArrayList<Calendar> startDates,
			ArrayList<Calendar> endDates, ArrayList<String> tags) {
		this.setDescription(description);
		this.setLocation(location);
		this.setTags(tags);
		this.startDates = startDates;
		this.endDates = endDates;
	}

	public void setStartDates(ArrayList<Calendar> startDates) {
		this.startDates = startDates;
	}

	public ArrayList<Calendar> getStartDates() {
		return startDates;
	}

	public void setEndDates(ArrayList<Calendar> endDates) {
		this.endDates = endDates;
	}

	public ArrayList<Calendar> getEndDates() {
		return endDates;
	}

	// @@author Chloe Odquier Fortuna (A0149063E)
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Description: " + this.getDescription() + "\r\n");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

		String startDates = "";
		for (int i = 0; i < this.startDates.size(); i++) {
			if (i == this.startDates.size() - 1) {
				startDates += sdf.format(this.startDates.get(i).getTime());
			} else {
				startDates += sdf.format(this.startDates.get(i).getTime()) + ", ";
			}
		}
		sb.append("Start Dates: " + startDates + "\r\n");

		String endDates = "";
		for (int i = 0; i < this.endDates.size(); i++) {
			if (i == this.endDates.size() - 1) {
				endDates += sdf.format(this.endDates.get(i).getTime());
			} else {
				endDates += sdf.format(this.endDates.get(i).getTime()) + ", ";
			}
		}
		sb.append("End Dates: " + endDates + "\r\n");

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
