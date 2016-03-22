package gui;

import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import bean.Display;
import bean.Task;
import bean.TaskDeadline;
import bean.TaskEvent;
import bean.TaskFloat;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import netscape.javascript.JSObject;

/**
 * @author Zhu Bingjing
 * @date 2016年3月2日 上午12:06:39
 * @version 1.0
 */
public class ShowList extends AppPage {
	private Display display=new Display();
	private JSObject win;
	
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
						
						// construct JSON to pass to JS
						//deadline tasks
						if(this.display.getDeadlineTasks()!=null){
							List<TaskDeadline> deadlines=this.display.getDeadlineTasks();
							JSONArray jsonDeadline = new JSONArray();
							for (TaskDeadline deadline: deadlines) {
								JSONObject task = new JSONObject(deadline);
								task.remove("startDate");
								task.remove("endDate");
								task.put("endDate",
										new SimpleDateFormat(
												"yy-MM-dd HH:mm")
												.format(deadline.getEndDate()
														.getTime()));
								jsonDeadline.put(task);
							}
							win.call("addTasks", jsonDeadline,"deadline");
							System.out.println(jsonDeadline);
						}
						
						
						//event tasks
						if(this.display.getEventTasks()!=null){
							List<TaskEvent> events=this.display.getEventTasks();
							JSONArray jsonEvent = new JSONArray();
							for (TaskEvent event: events) {
								JSONObject task = new JSONObject(event);
								task.remove("startDate");
								task.remove("endDate");
								task.put("startDate",
										new SimpleDateFormat(
												"yy-MM-dd HH:mm")
												.format( event
														.getStartDate()
														.getTime()));
								task.put("endDate",
										new SimpleDateFormat(
												"yy-MM-dd HH:mm")
												.format( event.getEndDate()
														.getTime()));
								jsonEvent.put(task);
							}
							win.call("addTasks", jsonEvent,"event");
							System.out.println(jsonEvent);
						}
						
						
						//floating tasks
						if(this.display.getFloatTasks()!=null){
							List<TaskFloat> floatings=this.display.getFloatTasks();
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
						if( this.display.getMessage()!=null)
							win.call("showFeedBack", this.display.getMessage());
					}
				});
	}
	
	public void setList(Display display){
		if (this.display != null) {
			this.display=display;		
			webEngine.reload();		
		}		
	}

	// JavaScript interface object
	public class ListBridge {
		public void receiveCommand(String command){
			System.out.println(command);
			GUIController.handelUserInput(command);
		}
	}
}
