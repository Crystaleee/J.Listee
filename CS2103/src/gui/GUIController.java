package gui;

import java.io.IOException;

import javax.swing.JOptionPane;

import bean.Display;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Logic;
import main.App;
import storage.LogStorage;

/**
 * @@author A0149527W
 */
public class GUIController {
	private static Stage stage;
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;

	private static AppPage welcome;
	private static AppPage showList;
	private static AppPage help;

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
	public static void displayWelcome() {
		Scene scene = stage.getScene();
		welcome = new WelcomeAndChooseStorage();
		if (scene == null) {
			scene = new Scene(welcome, WINDOW_WIDTH, WINDOW_HEIGHT);
			scene.setFill(null);
			setMouseMovable(scene);
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
	public static void displayList(Display display) {
			Scene scene = stage.getScene();
			
			((ShowList) showList).setList(display);
				        
			if (scene == null) {			
				scene = new Scene(showList,WINDOW_WIDTH,WINDOW_HEIGHT);
				scene.setFill(null);
				setMouseMovable(scene);
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
	 * display help page in the frame
	 * 
	 * @param display
	 * @return
	 */
	public static void displayHelp() {
		Scene scene = stage.getScene();
		help = new Help();
		
		if (scene == null) {			
			scene = new Scene(help, WINDOW_WIDTH, WINDOW_HEIGHT);
			scene.setFill(null);
			setMouseMovable(scene);
			//set Esc key for close
			setCloseOnEsc(stage, scene);
			stage.setScene(scene);
		} else {
			stage.getScene().setRoot(help);
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
	 * add mouse listener to scene
	 */
	private static void setMouseMovable(Scene scene){
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event) {
                App.xOffset = event.getSceneX();
                App.yOffset = event.getSceneY();
            }
		});
		scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                stage.setX(event.getScreenX() - App.xOffset);
	                stage.setY(event.getScreenY() - App.yOffset);
	            }
	        });
	}
	
	/**
	 * initialize the start page which display deadlines, events and floating tasks
	 */
	public static void initializeList(String filePath){
			//call to logic to get all the tasks
			Display display=Logic.initializeProgram(filePath);
			 //assert display!=null :"Display is null!";
			if(display==null)
				JOptionPane.showMessageDialog(null, "display is null!");
			showList=new ShowList(display);
			displayList(display);				
	}
	
	public static void handelUserInput(String command) {
		Display display=Logic.executeUserCommand(command);
		 //assert display!=null :"Display is null!";
		if(display==null)
			JOptionPane.showMessageDialog(null, "display is null!");
		displayList(display);		
	}

	public static AppPage getShowList() {
		return showList;
	}

	public static void changeFilePath(String filePath) {
		Display display=Logic.changeFilePath(filePath);
		 //assert display!=null :"Display is null!";
		if(display==null)
			JOptionPane.showMessageDialog(null, "display is null!");
		displayList(display);		
	}

}
