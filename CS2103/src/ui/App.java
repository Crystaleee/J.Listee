package ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import storage.LogStorage;
import bean.Task;

/**
 * @author Zhu Bingjing
 * @date 2016年3月1日 下午5:03:02
 * @version 1.0
 */
public class App {
	//this is the latest user command
	public static String command;
	//this is the task file
	public static String filePath;
	
	//UI
	static View view=new View();

	public static void main(String[] args) {
		filePath=LogStorage.readLog();
		//check if it's the first time that user use the application
		if (filePath==null){
			displayWelcome();
			//TODO logic tell storage to create a new task file
			//Logic.createFile(WelcomeAndChooseStorage.userChosenFilePath);
		}
		executeUntilExit();
	}

	/**
	 * 
	 */
	private static void displayWelcome() {
		View.card.show(View.panels, "welcome");
		View.frame.setVisible(true);
	}

	private static void executeUntilExit() {
		//call to logic to get all the tasks
		//TODO
		//List<Task> taskList=Logic.getAllTasks(filePath);
		//just simulate what the logic returns
		String[] tags= {"#tag1","#tag2"};
		Task t1=new Task("description", "@location", new Date(2016, 3, 1, 14, 30), new Date(2016, 3, 2, 14, 30),tags);
		Task t2=new Task("description2", "@location2", new Date(2016, 3, 3, 14, 30), new Date(2016, 3, 5, 14, 30),tags);
		List<Task> taskList=new ArrayList<Task>();
		taskList.add(t1);
		taskList.add(t2);
				
		while (true) {
			displayList(taskList);				
			//do nothing when command is null
			while(command==null){}
			//TODO logic handle user command
			//taskList=Logic.handle(command);
		}
	}

	/**
	 * @param taskList
	 */
	private static void displayList(List<Task> taskList) {
		View.showList=new ShowList(taskList);
		View.panels.add(View.showList.browserView,"showList");
		View.card.show(View.panels, "showList");
		View.frame.setVisible(true);
	}	
}
