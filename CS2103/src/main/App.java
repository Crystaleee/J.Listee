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
			View.displayWelcome();
			//waiting when user hasn't decide
			while(WelcomeAndChooseStorage.getUserChosenFilePath()==null){}
			filePath=WelcomeAndChooseStorage.getUserChosenFilePath();
			LogStorage.writeLogFile(filePath);
			Logic.createFile(filePath+"\\J.Listee.txt");
		}
		executeUntilExit();
	}



	private static void executeUntilExit() throws IOException {
		//call to logic to get all the tasks
		List<Task> taskList=Logic.initializeProgram(filePath+"\\J.Listee.txt").getList();
				
		while (true) {
			View.displayList(taskList);				
			//do nothing when command is null
			while(command==null){}
			//TODO logic handle user command
			taskList=Logic.handleCommand(command).getList();
		}
	}


}
