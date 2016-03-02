import java.util.ArrayList;
import java.util.Calendar;

class Parser{
	private ArrayList<String> calendarDescription = new ArrayList<String>();
	
	private  String[] splitEndTime(ArrayList<String> calendarDescription) {
		String endTime = calendarDescription.get(4).substring(0,calendarDescription.get(4).length()-1);	
		String[] endTimeHourMinute = endTime.split(":");
		return endTimeHourMinute;
	}

	private  void endDateTime(ArrayList<String> calendarDescription, Command add) {
		String[] endDateYYMMDD = splitEndDate(calendarDescription);

		int endDateYear = Integer.valueOf(endDateYYMMDD[2]);					
		int endDateMonth = Integer.valueOf(endDateYYMMDD[1]);
		int endDateDay = Integer.valueOf(endDateYYMMDD[0]);


		String[] endTimeHourMinute = splitEndTime(calendarDescription);
		int endHour = Integer.valueOf(endTimeHourMinute[0]);
		int endMin = Integer.valueOf(endTimeHourMinute[1]);

		add.setEndDateTime(endDateYear,endDateMonth,endDateDay,endHour,endMin);
	}

	private  String[] splitEndDate(ArrayList<String> calendarDescription) {
		String endDate = calendarDescription.get(3);
		String[] endDateYYMMDD = endDate.split("/");
		return endDateYYMMDD;
	}

	private  void startDateTime(ArrayList<String> calendarDescription, Command add) {
		String startDate = calendarDescription.get(0).substring(1, calendarDescription.get(0).length());
		String[] startDateYYMMDD = startDate.split("/");

		String startTime = calendarDescription.get(1);
		String[] startTimeHourMinute = startTime.split(":");

		int startDateYear = Integer.valueOf(startDateYYMMDD[2]);
		int startDateMonth = Integer.valueOf(startDateYYMMDD[1]);
		int startDateDay = Integer.valueOf(startDateYYMMDD[0]);

		add.setDate(startDateYear,startDateMonth,startDateDay);

		int startHour = Integer.valueOf(startTimeHourMinute[0]);
		int startMin = Integer.valueOf(startTimeHourMinute[1]);

		add.setTime(startHour, startMin);

		add.setStartDateTime(startDateYear,startDateMonth,startDateDay,startHour,startMin);
	}

	private  void setStartEndDateTime(ArrayList<String> calendarDescription, Command add){

		startDateTime(calendarDescription, add);

		if ( calendarDescription.get(2).equals("-") ){
			endDateTime(calendarDescription, add);
		}

	}

	private  void addTask(int i, String[] args, Command add) {
		

		while (i < args.length && !args[i].substring(0,1).equals("(")){
			add.setTaskDescription(args[i]);
			i++;
		}

		while (i < args.length && !args[i].substring(0,1).equals("@")){
			calendarDescription.add(args[i]);
			i++;
		}

		setStartEndDateTime(calendarDescription, add);

		if (i < args.length && args[i].substring(0,1).equals("@")){
			add.setLocation(args[i]);
			i++;
		}

		while (i < args.length && args[i].substring(0,1).equals("#")){
			add.setTags(args[i].substring(1, args[i].length()));
			i++;
		}
	}


	public Command ParseCommand(String inputLine){
		Command command = new Command();

		String[] args = inputLine.split(" ");	

		command.setCommandType(args[0].trim());

		if (command.getCommandType().equals("add")){
			addTask(1, args, command);
		}


		if (command.getCommandType().equals("update")){
			command.setTaskNumber(Integer.valueOf(args[1]));

			for (int j=2; j<args.length;j++){
				if (args[j].substring(0, 1).equals("@")){
					command.setLocation(args[j]);
				}

				if (args[j].substring(0, 1).equals("#")){
					command.setTags(args[j]);
				}

				if (args[j].substring(0, 1).equals("(")){
					while (j < args.length){
						
						calendarDescription.add(args[j]);

						if (args[j].substring(args[j].length()-1,args[j].length()).equals(")")){
							j++;
							break;
						}

						j++;

					}

				}
			}
		}

		/*	String endtDate = calendarDescription.get(0).substring(1, calendarDescription.get(0).length());
					String[] startDateYYMMDD = startDate.split("/");

					String startTime = calendarDescription.get(1);
					String[] startTimeHourMinute = startTime.split(":");

					int startDateYear = Integer.valueOf(startDateYYMMDD[2]);
					int startDateMonth = Integer.valueOf(startDateYYMMDD[1]);
					int startDateDay = Integer.valueOf(startDateYYMMDD[0]);
					int startHour = Integer.valueOf(startTimeHourMinute[0]);
					int startMin = Integer.valueOf(startTimeHourMinute[1]);
					startDateTime.set(startDateYear,startDateMonth,startDateDay,startHour,startMin);
		 */


	return command;
	}

}


public class Command {
	private  String commandType;
	private  ArrayList<String> taskDescription = new ArrayList<String>();
	private  String  recurringDescription = null;
	private  ArrayList<String> calendarDescription = new ArrayList<String>();
	private  Calendar startDateTime = Calendar.getInstance();
	private  Calendar endDateTime = Calendar.getInstance();
	private  Calendar date = Calendar.getInstance();
	private  Calendar time = Calendar.getInstance();
	private  String[] args;
	private  String location;
	private  ArrayList<String> tagLists = new ArrayList<String>();
	private  int taskNumber;
	
	public String getCommandType(){
		return commandType;
	}
	
	public void setTaskNumber(Integer taskNum) {
		taskNumber = taskNum;		
	}

	public void setTags(String tag) {
		tagLists.add(tag);
	}

	public void setTime(int startHour, int startMin) {
		time.set(startHour,startMin); //double check later
	}
	
	public void setDate(int startDateYear, int startDateMonth, int startDateDay) {
		date.set(startDateYear, startDateMonth, startDateDay);
	}
	
	public Calendar getDate(){
		return date;
	}
	
	public void setEndDateTime(int endDateYear, int endDateMonth, int endDateDay, int endHour, int endMin) {
		endDateTime.set(endDateYear,endDateMonth,endDateDay,endHour,endMin);
		
	}

	public void setStartDateTime(int startDateYear, int startDateMonth, int startDateDay, int startHour, int startMin) {
		startDateTime.set(startDateYear,startDateMonth,startDateDay,startHour,startMin);
	}
	
	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
		
	}
	
	public void setTaskDescription(String input){
		taskDescription.add(input);
	}
	public ArrayList<String> getTaskDescription(){
		return taskDescription;
	}
	
	public Calendar getStartDateTime(){
		return startDateTime;
	}

	public Calendar getEndDateTime(){
		return endDateTime;
	}
	
	public String getLocation(){
		return location;
	}
	
	public ArrayList<String> getTags(){
		return tagLists;
	}
	
	public void setLocation(String location){
		this.location = location;
		
	}

	public  int getTaskNumber() {
		return taskNumber;
	}

	
	
}
