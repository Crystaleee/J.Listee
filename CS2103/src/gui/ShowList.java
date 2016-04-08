package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import main.App;
import netscape.javascript.JSObject;

/**
 * @@author A0149527W
 */
public class ShowList extends AppPage {
	private List<String> userCmd = new ArrayList<String>();// store user's
															// commands
	private int cmdIndex;// the presenting cmd's index
	private Display display = new Display();
	private JSObject win;
	private InputSuggestion suggestion = InputSuggestion.getInstance();

	public ShowList(Display display) {
		super("/view/html/list.html");
		this.display = display;
		addLoadListener();
	}

	/**
	 * add load listener to webengine
	 */
	private void addLoadListener() {
		webEngine
				.getLoadWorker()
				.stateProperty()
				.addListener(
						(ObservableValue<? extends State> ov, State oldState,
								State newState) -> {
							if (newState == Worker.State.FAILED) {
								webEngine.load(WelcomeAndChooseStorage.class
										.getResource(this.html)
										.toExternalForm());
							}
							if (newState == Worker.State.SUCCEEDED) {
								this.win = (JSObject) webEngine
										.executeScript("window");
								win.setMember("app", new ListBridge());

								resetTaskNumber();

								// construct JSON to pass to JS and call add
								// task
								addTasks();

								setMessageAndAnimation();
							}
						});
	}

	/**
	 * set message and animation in display
	 */
	private void setMessageAndAnimation() {
		setMessage();

		JSONArray jsonFocus = constructFocusArray();

		setAddAnimation(jsonFocus);
		setFocusAnimation(jsonFocus);
		setConflictAnimation();
	}

	/**
	 * add all sorts of tasks
	 */
	private void addTasks() {
		addDeadline();
		addEvent();
		addFloat();
		addReserved();
		addCompleted();
	}

	/**
	 * reset task number
	 */
	private void resetTaskNumber() {
		webEngine.executeScript("reset()");
	}

	/**
	 * set focus animation
	 * 
	 * @param jsonFocus
	 */
	private void setFocusAnimation(JSONArray jsonFocus) {
		win.call("setFocus", jsonFocus);
	}

	/**
	 * set conflict animation
	 */
	private void setConflictAnimation() {
		JSONArray jsonConflict = new JSONArray();
		for (int i = 0; i < this.display.getConflictingTasksIndices().size(); i++) {
			jsonConflict.put(this.display.getConflictingTasksIndices().get(i));
		}
		win.call("showConflict", jsonConflict);
	}

	/**
	 * set add animation
	 * 
	 * @param jsonFocus
	 */
	private void setAddAnimation(JSONArray jsonFocus) {
		if (this.display.getCommandType() == "Add") {
			win.call("addShowAnimation", jsonFocus);
		}
	}

	/**
	 * construct JSONArray for completed tasks and call js
	 */
	private void addCompleted() {
		if (this.display.getVisibleCompletedTasks() != null) {
			List<Task> completeds = this.display.getVisibleCompletedTasks();
			JSONArray jsonTask = new JSONArray();

			for (Task completed : completeds) {
				JSONObject task = constructJSON(completed);
				jsonTask.put(task);
			}
			win.call("addTasks", jsonTask, "completed");
		}
	}

	/**
	 * construct JSONArray for reserved tasks and call js
	 */
	private void addReserved() {
		if (this.display.getVisibleReservedTasks() != null) {
			List<TaskReserved> reservations = this.display
					.getVisibleReservedTasks();
			JSONArray jsonReserved = new JSONArray();
			for (TaskReserved reserved : reservations) {
				JSONObject task = new JSONObject(reserved);
				task.remove("startDates");
				task.remove("endDates");
				for (int i = 0; i < reserved.getStartDates().size(); i++) {
					try {
						task.append("startDates", new SimpleDateFormat(
								"EEE MM/dd HH:mm", Locale.ENGLISH)
								.format(reserved.getStartDates().get(i)
										.getTime()));
						task.append("endDates",
								new SimpleDateFormat("EEE MM/dd HH:mm",
										Locale.ENGLISH).format(reserved
										.getEndDates().get(i).getTime()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				jsonReserved.put(task);
			}
			win.call("addReservedTask", jsonReserved);
		}
	}

	/**
	 * construct JSONArray for floating tasks and call js
	 */
	private void addFloat() {
		if (this.display.getVisibleFloatTasks() != null) {
			List<TaskFloat> floatings = this.display.getVisibleFloatTasks();
			JSONArray jsonFloating = new JSONArray();
			for (Task floating : floatings) {
				JSONObject task = constructJSON(floating);
				jsonFloating.put(task);
			}
			win.call("addTasks", jsonFloating, "floating");
		}
	}

	/**
	 * construct JSONArray for event tasks and call js
	 */
	private void addEvent() {
		if (this.display.getVisibleEvents() != null) {
			List<TaskEvent> events = this.display.getVisibleEvents();
			JSONArray jsonEvent = new JSONArray();
			for (TaskEvent event : events) {
				JSONObject task = constructJSON(event);
				jsonEvent.put(task);
			}
			win.call("addTasks", jsonEvent, "event");
		}
	}

	/**
	 * construct JSONArray for deadline tasks and call js
	 */
	private void addDeadline() {
		// deadline tasks
		if (this.display.getVisibleDeadlineTasks() != null) {
			List<TaskDeadline> deadlines = this.display
					.getVisibleDeadlineTasks();
			JSONArray jsonDeadline = new JSONArray();
			for (TaskDeadline deadline : deadlines) {
				JSONObject task = constructJSON(deadline);
				jsonDeadline.put(task);
			}

			win.call("addTasks", jsonDeadline, "deadline");
		}
	}

	/**
	 * construct single task json to pass to javascript
	 * 
	 * @param completed
	 * @return
	 */
	private JSONObject constructJSON(Task completed) {
		JSONObject task = new JSONObject(completed);
		task.remove("startDate");
		task.remove("endDate");
		try {
			if (completed instanceof TaskEvent) {

				task.put("startDate", new SimpleDateFormat("EEE MM/dd HH:mm",
						Locale.ENGLISH).format(((TaskEvent) completed)
						.getStartDate().getTime()));
			}
			if (completed instanceof TaskDeadline
					|| completed instanceof TaskEvent) {
				task.put("endDate", new SimpleDateFormat("EEE MM/dd HH:mm",
						Locale.ENGLISH).format(((TaskDeadline) completed)
						.getEndDate().getTime()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}

	public void setList(Display display) {
		if (this.display != null) {
			this.display = display;

			// add animation when deleting
			if (this.display.getCommandType() == "Delete") {
				setDeleteAnimation();
			} else {
				webEngine.reload();
			}
		}
	}

	public void setHtml(String html) {
		this.html = html;
		// load web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(this.html)
				.toExternalForm());
	}

	/**
	 * set delete animation
	 */
	private void setDeleteAnimation() {
		setMessage();

		JSONArray jsonFocus = constructFocusArray();

		setFocusAnimation(jsonFocus);
		win.call("addHideAnimation", jsonFocus);
		win.call("clearCommandLine");
		win.call("hideSuggestion");
	}

	/**
	 * set message in display
	 */
	private void setMessage() {
		if (this.display.getMessage() != null)
			win.call("showFeedBack", this.display.getMessage());
	}

	/**
	 * construct JSONArray from list of integers
	 * 
	 * @return
	 */
	private JSONArray constructFocusArray() {
		JSONArray jsonFocus = new JSONArray();
		for (int i = 0; i < this.display.getTaskIndices().size(); i++) {
			jsonFocus.put(this.display.getTaskIndices().get(i));
		}
		return jsonFocus;
	}

	public Display getDisplay() {
		return this.display;
	}

	// JavaScript interface object
	public class ListBridge {
		/**
		 * receive use cmd, add cmd history and decide what to do
		 * 
		 * @param command
		 */
		public void receiveCommand(String command) {
			String cmd = command.trim();

			setCmdHistory(cmd);

			if (cmd.toLowerCase().equals("help")
					|| cmd.toLowerCase().equals("show help")) {
				GUIController.displayHelp();
			} else if (cmd.toLowerCase().equals("change filepath")) {
				changeStorageLocation();
			} else if (cmd.toLowerCase().equals("exit")
					|| cmd.toLowerCase().equals("quit")) {
				App.terminate();
			} else {
				GUIController.handelUserInput(cmd);
			}
		}

		/**
		 * set cmd history
		 * 
		 * @param cmd
		 */
		private void setCmdHistory(String cmd) {
			userCmd.add(cmd);
			cmdIndex = userCmd.size() - 1;
		}

		/**
		 * pop up a file chooser and send new filepath
		 */
		private void changeStorageLocation() {
			DirectoryChooser fileChooser = new DirectoryChooser();
			File selectedFile = fileChooser.showDialog(App.stage);
			fileChooser.setTitle("Please select a folder for storage location");

			if (selectedFile != null) {
				App.filePath = selectedFile.getAbsolutePath()
						+ "\\J.Listee.txt";
				// create file under the file folder chosen by user
				GUIController.changeFilePath(App.filePath);
			}
		}

		public String getCommandsuggestion(String cmd) {
			String cmdSuggestion = suggestion.getSuggestedInput(cmd.trim());
			if (cmdSuggestion.equals(new String("null")) == false) {
				return cmdSuggestion;
			} else {
				return "";
			}
		}

		public String getPreviousCmd() {
			if (cmdIndex > 0) {
				return userCmd.get(cmdIndex--);
			} else {
				return userCmd.get(0);
			}
		}

		public String getLaterCmd() {
			if (cmdIndex < userCmd.size() - 1) {
				return userCmd.get(++cmdIndex);
			} else
				return "";
		}
	}
}
