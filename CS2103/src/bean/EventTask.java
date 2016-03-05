package bean;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月4日 下午9:47:08 
 * @version 1.0 
 */
public class EventTask extends Task{
	private Calendar startDateTime;
	private Calendar endDateTime;
	
	public EventTask(String description, String location, ArrayList<String> tags, Calendar start, Calendar end) {
		super(description, location, tags);
		this.setStartDateTime(start);
		this.setEndDateTime(end);
	}

	public Calendar getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Calendar startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Calendar getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Calendar endDateTime) {
		this.endDateTime = endDateTime;
	}

}
