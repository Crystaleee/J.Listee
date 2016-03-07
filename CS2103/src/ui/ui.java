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
	static void createFile(String userChosecfile) throws IOException {
		LogStorage.writeLogFile(userChosecfile);
		Logic.createFile(userChosecfile);
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
		try{
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
		}catch(Exception e){			
		}
		
	}

	/**
	 * initialize the start page which display deadlines, events and floating tasks
	 */
	public static void initializeList(Stage stage, String filePath) throws IOException {
		try{
			//call to logic to get all the tasks
			Display display=Logic.initializeProgram(filePath);
			ui.displayList(stage,display);	
		}catch(Exception e){			
		}
			
	}
	
	public static void feedback(String command) {
		Display display=Logic.executeUserCommand(command);
		ui.displayList(App.stage,display);		
	}
}
