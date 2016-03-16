package main;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.stage.Stage;
import storage.LogStorage;
import ui.UI;
/**
 * @author Zhu Bingjing
 * @date 2016年3月1日 下午5:03:02
 * @version 1.0
 */
public class App extends Application{	
	//This the scene stage of application
	public static Stage stage;
	//this is the task file
	public static String filePath;
	public UI ui=UI.getInstance();

	public static void main(String[] args){
		Application.launch(args);			
	}


	@Override
	public void start(Stage primaryStage) {	
		stage = primaryStage;
	    stage.setTitle("J.Listee");
	    //TODO EXCEPTION
	    try {
			filePath=LogStorage.readLog();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
		//check if it's the first time that user use the application
		if (filePath==null){
			ui.displayWelcome(stage);
		}else{		
			ui.initializeList(stage, filePath);
		}		
	}

}
