package gui;

import java.io.IOException;

import javax.swing.JOptionPane;

import bean.Display;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.Logic;
import storage.LogStorage;

/**
 * @@author A0149527W
 */
public class GUIController {
	private static Stage stage;
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 590;

	private static AppPage welcome;
	private static AppPage showList;

	public static void setStage(Stage stage2) {
		stage=stage2;
	}

	/**
	 * create J.Listee file under the given file path
	 * 
	 * @throws IOException if file can not be created
	 */
	public static void createFile(String userChosenfile) throws IOException {
		LogStorage.writeLogFile(userChosenfile);	
		//TODO EXCEPTION
		if(!Logic.createFile(userChosenfile)){
			throw new IOException("Error: Cannot create file!");
		}		
	}

	/**
	 * display welcome page in the frame
	 */
	public static void displayWelcome(Stage stage) {
		Scene scene = stage.getScene();
		welcome = new WelcomeAndChooseStorage();
		if (scene == null) {
			scene = new Scene(welcome, WINDOW_WIDTH, WINDOW_HEIGHT);
			setCloseOnEsc(stage, scene);
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
			
			((ShowList) showList).setList(display);
			if (scene == null) {			
				scene = new Scene(showList, WINDOW_WIDTH, WINDOW_HEIGHT);
				//set Esc key for close
				setCloseOnEsc(stage, scene);
				stage.setScene(scene);
			} else {
				stage.getScene().setRoot(showList);
			}
			stage.sizeToScene();
			stage.show();		
	}

	/**
	 * @param stage
	 * @param scene
	 */
	private static void setCloseOnEsc(Stage stage, Scene scene) {
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>
		  () {@Override
		        public void handle(KeyEvent t) {
		          if(t.getCode()==KeyCode.ESCAPE){
		              stage.close();
		          }
		        }
		    });
	}

	/**
	 * initialize the start page which display deadlines, events and floating tasks
	 */
	public static void initializeList(Stage stage, String filePath){
			//call to logic to get all the tasks
			Display display=Logic.initializeProgram(filePath);
			 //assert display!=null :"Display is null!";
			if(display==null)
				JOptionPane.showMessageDialog(null, "display is null!");
			showList= new ShowList(display);
			displayList(stage,display);				
	}
	
	public static void handelUserInput(String command) {
		Display display=Logic.executeUserCommand(command);
		 //assert display!=null :"Display is null!";
		if(display==null)
			JOptionPane.showMessageDialog(null, "display is null!");
		displayList(stage,display);		
	}
}
