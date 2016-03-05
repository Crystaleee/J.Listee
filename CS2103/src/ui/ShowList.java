package ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import netscape.javascript.JSObject;
import bean.DeadlineTask;
import bean.Display;
import bean.EventTask;
import bean.Task;
import javafx.concurrent.Worker;

/**
 * @author Zhu Bingjing
 * @date 2016年3月2日 上午12:06:39
 * @version 1.0
 */
public class ShowList extends Pane {
	private static WebView browser = new WebView();
	private static WebEngine webEngine = browser.getEngine();
	private Display display;

	public ShowList(Display display) {
		this.display=display;
		// load the web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				"/html/list.html").toExternalForm());
		// add the web view to the scene
		this.getChildren().add(browser);

		if (display != null) {
			webEngine
					.getLoadWorker()
					.stateProperty()
					.addListener(
							(ObservableValue<? extends State> ov,
									State oldState, State newState) -> {
								if (newState == Worker.State.SUCCEEDED) {
									JSObject win = (JSObject) webEngine
											.executeScript("window");

									// construct JSON to pass to JS
									//deadline tasks
									if(display.getDeadlineTasks()!=null){
										List<Task> deadlines=display.getDeadlineTasks();
										JSONArray jsonDeadline = new JSONArray();
										for (Task deadline: deadlines) {
											JSONObject task = new JSONObject(deadline);
											task.remove("startDateTime");
											task.remove("endDateTime");
											task.put("endDateTime",
													new SimpleDateFormat(
															"yy-MM-dd HH:mm")
															.format(((DeadlineTask)deadline).getEndDateTime()
																	.getTime()));
											jsonDeadline.put(task);
										}
										win.call("addTasks", jsonDeadline,"deadline");
									}
									
									
									//event tasks
									if(display.getEventTasks()!=null){
										List<Task> events=display.getEventTasks();
										JSONArray jsonEvent = new JSONArray();
										for (Task event: events) {
											JSONObject task = new JSONObject(event);
											task.remove("startDateTime");
											task.remove("endDateTime");
											task.put("startDateTime",
													new SimpleDateFormat(
															"yy-MM-dd HH:mm")
															.format(((EventTask) event)
																	.getStartDateTime()
																	.getTime()));
											task.put("endDateTime",
													new SimpleDateFormat(
															"yy-MM-dd HH:mm")
															.format(((EventTask) event).getEndDateTime()
																	.getTime()));
											jsonEvent.put(task);
										}
										win.call("addTasks", jsonEvent,"event");
									}
									
									
									//floating tasks
									if(display.getFloatTasks()!=null){
										List<Task> floatings=display.getFloatTasks();
										JSONArray jsonFloating = new JSONArray();
										for (Task floating: floatings) {
											JSONObject task = new JSONObject(floating);
											task.remove("startDateTime");
											task.remove("endDateTime");
											jsonFloating.put(task);
										}
										win.call("addTasks", jsonFloating,"floating");
									}
									
									
									win.setMember("app", new ListBridge());
								}
							});
		}
	}

	// JavaScript interface object
	public class ListBridge {
		public void receiveCommand(String command) throws IOException {
			System.out.println(command);
			ui.feedback(command);
		}
	}
}
