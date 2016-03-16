package ui;

import java.io.IOException;

import javax.swing.JOptionPane;

import bean.Display;
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
public class UI {
	public static UI ui;
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 590;

	private WelcomeAndChooseStorage welcome;
	private ShowList showList;

	public static UI getInstance(){
		if(ui==null)
			ui=new UI();
		return ui;
	}
	/**
	 * create J.Listee file under the given file path
	 * 
	 * @throws IOException if file can not be created
	 */
	public void createFile(String userChosecfile) throws IOException {
		LogStorage.writeLogFile(userChosecfile);	
		//TODO EXCEPTION
		if(!Logic.createFile(userChosecfile)){
			throw new IOException("Error: Cannot create file!");
		}		
	}

	/**
	 * display welcome page in the frame
	 */
	public void displayWelcome(Stage stage) {
		Scene scene = stage.getScene();
		this.welcome = WelcomeAndChooseStorage.getInstance();
		if (scene == null) {
			scene = new Scene(this.welcome, WINDOW_WIDTH, WINDOW_HEIGHT);
			stage.setScene(scene);
		} else {
			stage.getScene().setRoot(this.welcome);
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
	public void displayList(Stage stage, Display display) {
			Scene scene = stage.getScene();
			this.showList = new ShowList(display);
			if (scene == null) {			
				scene = new Scene(this.showList, WINDOW_WIDTH, WINDOW_HEIGHT);
				stage.setScene(scene);
			} else {
				stage.getScene().setRoot(this.showList);
			}
			stage.sizeToScene();
			stage.show();
		
	}

	/**
	 * initialize the start page which display deadlines, events and floating tasks
	 */
	public void initializeList(Stage stage, String filePath){
			//call to logic to get all the tasks
			Display display=Logic.initializeProgram(filePath);
			 //assert display!=null :"Display is null!";
			if(display==null)
				JOptionPane.showMessageDialog(null, "display is null!");
			ui.displayList(stage,display);				
	}
	
	public void feedback(String command) {
		Display display=Logic.executeUserCommand(command);
		 //assert display!=null :"Display is null!";
		if(display==null)
			JOptionPane.showMessageDialog(null, "display is null!");
		ui.displayList(App.stage,display);		
	}
}
