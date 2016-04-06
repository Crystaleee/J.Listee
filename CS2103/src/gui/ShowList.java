package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;

import org.json.JSONArray;
import org.json.JSONObject;

import parser.InputSuggestion;
import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskReserved;
import bean.TaskEvent;
import bean.TaskFloat;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.App;
import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class ShowList extends AppPage {
	private List<String> userCmd=new ArrayList<String>();//store user's commands
	private int cmdIndex;//the presenting cmd's index
	private Display display=new Display();
	private JSObject win;
	private InputSuggestion suggestion=InputSuggestion.getInstance();
	
	public ShowList(Display display) {
		super("/view/html/list.html");
		this.display=display;
				
		//add load listener
		webEngine
		.getLoadWorker()
		.stateProperty()
		.addListener(
				(ObservableValue<? extends State> ov,
						State oldState, State newState) -> {
					if (newState == Worker.State.SUCCEEDED) {
						
						this.win = (JSObject) webEngine
								.executeScript("window");
						win.setMember("app", new ListBridge());
																			
						//reset task number				
						webEngine.executeScript("reset()");
						
						//set message
						if( this.display.getMessage()!=null)
							win.call("showFeedBack", this.display.getMessage());

						// construct JSON to pass to JS
						//deadline tasks
						if(this.display.getVisibleDeadlineTasks()!=null){
							List<TaskDeadline> deadlines=this.display.getVisibleDeadlineTasks();
							JSONArray jsonDeadline = new JSONArray();
							for (TaskDeadline deadline: deadlines) {
								JSONObject task = new JSONObject(deadline);
								task.remove("startDate");
								task.remove("endDate");
								try {
									task.put("endDate",
											new SimpleDateFormat(
													"EEE MM/dd HH:mm",Locale.ENGLISH)
													.format(deadline.getEndDate()
															.getTime()));
								} catch (Exception e) {
									e.printStackTrace();
								}
								jsonDeadline.put(task);
							}
							
							win.call("addTasks", jsonDeadline,"deadline");
							System.out.println(jsonDeadline);
						}
						
						
						//event tasks
						if(this.display.getVisibleEvents()!=null){
							List<TaskEvent> events=this.display.getVisibleEvents();
							JSONArray jsonEvent = new JSONArray();
							for (TaskEvent event: events) {
								JSONObject task = new JSONObject(event);
								task.remove("startDate");
								task.remove("endDate");
								try {
									task.put("startDate",
											new SimpleDateFormat(
													"EEE MM/dd HH:mm",Locale.ENGLISH)
													.format( event
															.getStartDate()
															.getTime()));

									task.put("endDate",
											new SimpleDateFormat(
													"EEE MM/dd HH:mm",Locale.ENGLISH)
													.format( event.getEndDate()
															.getTime()));
								} catch (Exception e) {
									e.printStackTrace();
								}
								jsonEvent.put(task);
							}
							win.call("addTasks", jsonEvent,"event");
							System.out.println(jsonEvent);
						}				
						
						//floating tasks
						if(this.display.getVisibleFloatTasks()!=null){
							List<TaskFloat> floatings=this.display.getVisibleFloatTasks();
							JSONArray jsonFloating = new JSONArray();
							for (Task floating: floatings) {
								JSONObject task = new JSONObject(floating);
								task.remove("startDateTime");
								task.remove("endDateTime");
								jsonFloating.put(task);
							}
							win.call("addTasks", jsonFloating,"floating");
							System.out.println(jsonFloating);
						}
						
						//reserved tasks
						if(this.display.getVisibleReservedTasks()!=null){
							List<TaskReserved> reservations=this.display.getVisibleReservedTasks();
							JSONArray jsonReserved = new JSONArray();
							for (TaskReserved reserved: reservations) {
								JSONObject  task= new JSONObject(reserved);
								System.out.println(task);
								task.remove("startDates");
								task.remove("endDates");
								for(int i=0;i<reserved.getStartDates().size();i++){
									try {
										task.append("startDates",
												new SimpleDateFormat(
														"EEE MM/dd HH:mm",Locale.ENGLISH)
														.format( reserved
																.getStartDates().get(i)
																.getTime()));

										task.append("endDates",
												new SimpleDateFormat(
														"EEE MM/dd HH:mm",Locale.ENGLISH)
														.format( reserved.getEndDates().get(i)
																.getTime()));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}						
								jsonReserved.put(task);
							}
							System.out.println(jsonReserved);
							win.call("addReservedTask", jsonReserved);							
						}
						
						//completed tasks
						if(this.display.getVisibleCompletedTasks()!=null 
								&&this.display.getVisibleDeadlineTasks().size()==0
								&& this.display.getVisibleEvents().size()==0
								&& this.display.getVisibleFloatTasks().size()==0
								&& this.display.getVisibleReservedTasks().size()==0){
							List<Task> completeds=this.display.getVisibleCompletedTasks();
							JSONArray jsonTask= new JSONArray();
							
							for (Task completed: completeds) {
								JSONObject task = new JSONObject(completed);
								task.remove("startDate");
								task.remove("endDate");
								if(completed instanceof TaskEvent){
									try {
										task.put("startDate",
												new SimpleDateFormat(
														"EEE MM/dd HH:mm",Locale.ENGLISH)
														.format( ((TaskEvent) completed)
																.getStartDate()
																.getTime()));
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if(completed instanceof TaskDeadline || completed instanceof TaskEvent){
									try {
										task.put("endDate",
												new SimpleDateFormat(
														"EEE MM/dd HH:mm",Locale.ENGLISH)
														.format( ((TaskDeadline)completed).getEndDate()
																.getTime()));
									} catch (Exception e) {
										e.printStackTrace();
									}								
								}
								jsonTask.put(task);
							}
							win.call("addTasks", jsonTask,"completed");
							System.out.println(jsonTask);
						}				
						
						//focus tasks if necessary
					//	if(this.display.getCommandType()=="Add"||this.display.getCommandType()=="Update"||this.display.getCommandType()=="Undone"){
							JSONArray jsonFocus= new JSONArray();
							for(int i=0;i<this.display.getTaskIndices().size();i++){
								jsonFocus.put(this.display.getTaskIndices().get(i));
							}
							
							if(this.display.getCommandType()=="Add"){
								win.call("addShowAnimation", jsonFocus);
							}
							
							win.call("setFocus", jsonFocus);
							System.out.println("focus: "+jsonFocus);							
					//	}
							
							//add border to conflicting tasks
							JSONArray jsonConflict= new JSONArray();
							for(int i=0;i<this.display.getConflictingTasksIndices().size();i++){
								jsonConflict.put(this.display.getConflictingTasksIndices().get(i));
							}
							win.call("showConflict", jsonConflict);
							System.out.println(jsonConflict);
					}
				});
	}
	
	public void setList(Display display){
		if (this.display != null) {
			this.display=display;		
			
			//add animation when deleting
			if(this.display.getCommandType()=="Delete"){
				//set message
				if( this.display.getMessage()!=null)
					win.call("showFeedBack", this.display.getMessage());
				
				JSONArray jsonFocus= new JSONArray();
				for(int i=0;i<this.display.getTaskIndices().size();i++){
					jsonFocus.put(this.display.getTaskIndices().get(i)-1);
				}
				System.out.println("focus: "+jsonFocus);	
				
				win.call("setFocus", jsonFocus);
				win.call("addHideAnimation", jsonFocus);
				win.call("clearCommandLine");	
				win.call("hideSuggestion");
			}else{
				webEngine.reload();			
			}				
		}		
	}

	public Display getDisplay(){
		return this.display;
	}
	
	// JavaScript interface object
	public class ListBridge {
		public void receiveCommand(String command){
			String cmd=command.toLowerCase().trim();
			System.out.println(cmd);
			userCmd.add(cmd);
			cmdIndex=userCmd.size()-1;
			
			if(cmd.equals("help")||cmd.equals("show help")){
				GUIController.displayHelp();
			}else if(cmd.equals("change filepath")){
				
				DirectoryChooser fileChooser = new DirectoryChooser();
				File selectedFile = fileChooser.showDialog(App.stage);
				fileChooser.setTitle("Please select a folder for storage location");
				
				if(selectedFile!=null){			
					App.filePath=selectedFile.getAbsolutePath()+"\\J.Listee.txt";
					System.out.println(App.filePath);
					// create file under the file folder chosen by user
					GUIController.changeFilePath(App.filePath);
				}
			}else if(cmd.equals("exit")||cmd.equals("quit")){
				App.terminate();
			}else{
				GUIController.handelUserInput(cmd);
			}			
		}
		
		public String getCommandsuggestion(String cmd){
			String cmdSuggestion=suggestion.getSuggestedInput(cmd.trim());
			if(cmdSuggestion.equals(new String("null"))==false){
				return cmdSuggestion;
			}else{
				return "";
			}
		}
		
		public String getPreviousCmd(){
			if(cmdIndex>0){
				return userCmd.get(cmdIndex--);
			}else{
				return userCmd.get(0);
			}			
		}
		
		public String getLaterCmd(){
			if(cmdIndex<userCmd.size()-1){
				return userCmd.get(++cmdIndex);
			}else return "";
		}
	}
}
