package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Display;
import bean.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Logic;
import main.App;
import storage.LogStorage;

/**
 * @author Zhu Bingjing
 * @date 2016年3月5日 上午10:52:04
 * @version 1.0
 */
public class ui {
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 590;

	private static WelcomeAndChooseStorage welcome;
	private static ShowList showList;

	/**
	 * create J.Listee file under the given file path
	 * 
	 * @throws IOException
	 */
	static void createFile(String userChosecfilePath) throws IOException {
		LogStorage.writeLogFile(userChosecfilePath + "\\J.Listee.txt");
		Logic.createFile(userChosecfilePath + "\\J.Listee.txt");
		// return true or false
		// TODO
	}

	/**
	 * display welcome page in the frame
	 */
	public static void displayWelcome(Stage stage) {
		Scene scene = stage.getScene();
		welcome = new WelcomeAndChooseStorage();
		if (scene == null) {
			scene = new Scene(welcome, WINDOW_WIDTH, WINDOW_HEIGHT);
			stage.setScene(scene);
		} else {
			stage.getScene().setRoot(welcome);
		}
		stage.sizeToScene();
		stage.show();
	}

	/**
	 * display list page in the frame
	 * 
	 * @param display
	 * @return
	 */
	public static void displayList(Stage stage, Display display) {
		Scene scene = stage.getScene();
		showList = new ShowList(display);
		if (scene == null) {
			scene = new Scene(showList, WINDOW_WIDTH, WINDOW_HEIGHT);
			stage.setScene(scene);
		} else {
			stage.getScene().setRoot(showList);
		}
		stage.sizeToScene();
		stage.show();
	}

	/**
	 * initialize the start page which display deadlines, events and floating tasks
	 */
	public static void initializeList(Stage stage, String filePath) throws IOException {
		//call to logic to get all the tasks
			Display display=Logic.initializeProgram(filePath);
//		List<String> tags=new ArrayList<String>();
//		tags.add("#tag1");
//		tags.add("#tag2");
//		Calendar d1=Calendar.getInstance();
//		Calendar d2=Calendar.getInstance();
//		d1.setTime(new Date(2016, 3, 3, 14, 30));
//		d2.setTime(new Date(2016, 3, 5, 14, 30));
//		Task t2=new Task("description2", "@location2", d1, d2,tags);
//		List<Task> taskList=new ArrayList<Task>();
//		taskList.add(t2);
//		taskList.add(t2);
			ui.displayList(stage,display);		
	}
	
	public static void feedback(String command) {
		Display display=Logic.executeUserCommand(command);
		ui.displayList(App.stage,display);		
	}
}
