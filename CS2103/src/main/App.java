package main;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import storage.LogStorage;
import gui.GUIController;
/**
 * @@author A0149527W
 */
public class App extends Application{	
	//This the scene stage of application
	public static Stage stage;
	//this is the task file
	public static String filePath;
	public static double xOffset;
	public static double yOffset;

	public static void main(String[] args){
		Application.launch(args);			
	}


	@Override
	public void start(Stage primaryStage) {	
		stage = primaryStage;
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initStyle(StageStyle.TRANSPARENT);
	    stage.setTitle("J.Listee");
	    stage.setResizable(false);
	    GUIController.setStage(stage);    
	    judgeAndShowStart();		
	}


	/**
	 * judge if it's the first time user use this app and show start page
	 */
	private void judgeAndShowStart() {
		//read log file
		try {
			filePath=LogStorage.readLog();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
		
		//check if it's the first time that user use the application
		if (filePath==null){
			GUIController.displayWelcome();
		}else{		
			GUIController.initializeList(filePath);
		}
	}

}
