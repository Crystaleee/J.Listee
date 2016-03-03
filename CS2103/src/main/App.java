package main;

import java.io.IOException;
import java.util.List;

import logic.Logic;
import storage.LogStorage;
import ui.ShowList;
import ui.View;
import ui.WelcomeAndChooseStorage;
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

	public static void main(String[] args) throws IOException {
		filePath=LogStorage.readLog();
		//check if it's the first time that user use the application
		if (filePath==null){
			displayWelcome();
			//TODO logic tell storage to create a new task file
			//waiting when user hasn't decide
			while(WelcomeAndChooseStorage.getUserChosenFilePath()==null){}
			filePath=WelcomeAndChooseStorage.getUserChosenFilePath();
			LogStorage.writeLogFile(filePath);
			Logic.createFile(filePath);
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

	private static void executeUntilExit() throws IOException {
		//call to logic to get all the tasks
		//TODO
		List<Task> taskList=Logic.initializeProgram(filePath).getList();
				
		while (true) {
			displayList(taskList);				
			//do nothing when command is null
			while(command==null){}
			//TODO logic handle user command
			taskList=Logic.handleCommand(command).getList();
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
