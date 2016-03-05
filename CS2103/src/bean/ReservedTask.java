package bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月4日 下午9:52:44 
 * @version 1.0 
 */
public class ReservedTask extends Task{
	private List<Calendar> startDateTimeList;
	private List<Calendar> endDateTimeList;
	
	public ReservedTask(String description, String location,
			ArrayList<String> tags, List<Calendar> start, List<Calendar> end) {
		super(description, location, tags);
		this.setStartDateTimeList(start);
		this.setEndDateTimeList(end);
	}

	public List<Calendar> getStartDateTimeList() {
		return startDateTimeList;
	}

	public void setStartDateTimeList(List<Calendar> startDateTimeList) {
		this.startDateTimeList = startDateTimeList;
	}

	public List<Calendar> getEndDateTimeList() {
		return endDateTimeList;
	}

	public void setEndDateTimeList(List<Calendar> endDateTimeList) {
		this.endDateTimeList = endDateTimeList;
	}

}
