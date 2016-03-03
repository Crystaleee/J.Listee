package parser;

import java.util.ArrayList;

import bean.Command;

public class Parser{
	private static ArrayList<String> calendarDescription = new ArrayList<String>();
	
	private static  String[] splitEndTime(ArrayList<String> calendarDescription) {
		String endTime = calendarDescription.get(4).substring(0,calendarDescription.get(4).length()-1);	
		String[] endTimeHourMinute = endTime.split(":");
		return endTimeHourMinute;
	}

	private static  void endDateTime(ArrayList<String> calendarDescription, Command add) {
		String[] endDateYYMMDD = splitEndDate(calendarDescription);

		int endDateYear = Integer.valueOf(endDateYYMMDD[2]);					
		int endDateMonth = Integer.valueOf(endDateYYMMDD[1]);
		int endDateDay = Integer.valueOf(endDateYYMMDD[0]);


		String[] endTimeHourMinute = splitEndTime(calendarDescription);
		int endHour = Integer.valueOf(endTimeHourMinute[0]);
		int endMin = Integer.valueOf(endTimeHourMinute[1]);

		add.setAddEndDateTime(endDateYear,endDateMonth,endDateDay,endHour,endMin);
	}

	private static  String[] splitEndDate(ArrayList<String> calendarDescription) {
		String endDate = calendarDescription.get(3);
		String[] endDateYYMMDD = endDate.split("/");
		return endDateYYMMDD;
	}

	private static  void startDateTime(ArrayList<String> calendarDescription, Command add) {
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

		add.setAddStartDateTime(startDateYear,startDateMonth,startDateDay,startHour,startMin);
	}

	private static  void setStartEndDateTime(ArrayList<String> calendarDescription, Command add){

		startDateTime(calendarDescription, add);

		if ( calendarDescription.get(2).equals("-") ){
			endDateTime(calendarDescription, add);
		}

	}

	private static  void addTask(int i, String[] args, Command add) {
		

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
			add.setAddLocation(args[i]);
			i++;
		}

		while (i < args.length && args[i].substring(0,1).equals("#")){
			add.setTags(args[i].substring(1, args[i].length()));
			i++;
		}
	}


	public static Command parseCommand(String inputLine){
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
					command.setAddLocation(args[j]);
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

	else if (command.getCommandType().equals("delete")){
			args = inputLine.substring(7, inputLine.length()).split(",");
			for (int i=0; i<args.length; i++){
				//command.setDeleteTaskNumber(Integer.valueOf(args[i]));
			}
		}
		
		else if (command.getCommandType().equals("postpone")){
			command.setTaskNumber(Integer.valueOf(args[1]));
			//command.setNumberOfDaysToPostpone(Integer.valueOf(args[2]));
		}
		
/*		else if (command.getCommandType().equals("show")){
				(need to double cfm)
		} */
		
		else if (command.getCommandType().equals("done")){
			args = inputLine.substring(5, inputLine.length()).split(",");
			for (int i=0; i<args.length; i++){
				//command.setDoneTaskNumber(Integer.valueOf(args[i]));
			}
		}
		

	return command;
	}

}



