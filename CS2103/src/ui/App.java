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
	//a boolean indicating whether it's the first time to use the application
	public static boolean isFirstTime=false;	
	
	//UI
	static View view=new View();

	public static void main(String[] args) {
		LogStorage.readLog();
		if (isFirstTime){
			View.card.show(View.panels, "welcome");
			View.frame.setVisible(true);
		}else{
			executeUntilExit();
		}		
	}

	private static void executeUntilExit() {
		//call to logic to get all the tasks
		//List<Task> taskList=Logic.getAllTasks(filePath);
		//just simulate what the logic returns
		String[] tags= {"#tag1","#tag2"};
		Task t1=new Task("description", "@location", new Date(2016, 3, 1, 14, 30), new Date(2016, 3, 2, 14, 30),tags);
		Task t2=new Task("description2", "@location2", new Date(2016, 3, 3, 14, 30), new Date(2016, 3, 5, 14, 30),tags);
		List<Task> taskList=new ArrayList<Task>();
		taskList.add(t1);
		taskList.add(t2);
		
		View.showList=new ShowList(taskList);
		View.panels.add(View.showList.browserView,"showList");
		View.card.show(View.panels, "showList");
		View.frame.setVisible(true);	
	}

	
}
