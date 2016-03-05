package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import storage.LogStorage;
import ui.ui;
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
	

	public static void main(String[] args) throws IOException {
		Application.launch(args);			
	}


	@Override
	public void start(Stage primaryStage) throws Exception {	
		stage = primaryStage;
	    stage.setTitle("J.Listee");
	    
	    filePath=LogStorage.readLog();
		//check if it's the first time that user use the application
		if (filePath==null){
			ui.displayWelcome(stage);
		}else{		
			ui.initializeList(stage, filePath);
		}		
	}

}
