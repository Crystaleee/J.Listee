package bean;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月4日 下午9:51:08 
 * @version 1.0 
 */
public class DeadlineTask extends Task{
	private Calendar endDateTime;
	
	public DeadlineTask(String description, String location,
			ArrayList<String> tags, Calendar deadline) {
		super(description, location, tags);
		this.setEndDateTime(deadline);
	}

	public Calendar getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Calendar deadline) {
		this.endDateTime = deadline;
	}

}
