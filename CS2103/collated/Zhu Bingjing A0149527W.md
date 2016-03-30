# Zhu Bingjing A0149527W
###### src\gui\AppPage.java
``` java
 * @date 2016å¹´3æœˆ21æ—¥ ä¸Šå�ˆ10:54:00 
 * @version 1.0 
 */
public abstract class AppPage extends Pane{
	WebView browser;
	WebEngine webEngine;
	String html;
//	JSObject win;
	
	public AppPage(String html){
		this.browser = new WebView();
		this.webEngine = browser.getEngine();
		this.html=html;
		browser. setContextMenuEnabled(false);

		//load web page
		webEngine.load(WelcomeAndChooseStorage.class.getResource(
				this.html).toExternalForm());
		
		// add the web view to the scene
		this.getChildren().add(browser);
	}
	
	
}
```
###### src\gui\GUIController.java
``` java
 * @date 2016å¹´3æœˆ5æ—¥ ä¸Šå�ˆ10:52:04
 * @version 1.0
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
```
###### src\gui\ShowList.java
``` java
 * @date 2016å¹´3æœˆ2æ—¥ ä¸Šå�ˆ12:06:39
 * @version 1.0
 */
public class ShowList extends AppPage {
	private List<String> userCmd=new ArrayList<String>();//store user's commands
	private int cmdIndex;//the presenting cmd's index
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
						
						//reserved tasks
						if(this.display.getReservedTasks()!=null){
							List<TaskReserved> reservations=this.display.getReservedTasks();
							JSONArray jsonReserved = new JSONArray();
							for (TaskReserved reserved: reservations) {
								JSONObject  task= new JSONObject(reserved);
								System.out.println(task);
								task.remove("startDates");
								task.remove("endDates");
								for(int i=0;i<reserved.getStartDates().size();i++){
									task.append("startDates",
											new SimpleDateFormat(
													"yy-MM-dd HH:mm")
													.format( reserved
															.getStartDates().get(i)
															.getTime()));
									task.append("endDates",
											new SimpleDateFormat(
													"yy-MM-dd HH:mm")
													.format( reserved.getEndDates().get(i)
															.getTime()));
								}						
								jsonReserved.put(task);
							}
							System.out.println(jsonReserved);
							win.call("addReservedTask", jsonReserved);							
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
			userCmd.add(command);
			cmdIndex=userCmd.size()-1;
			
			GUIController.handelUserInput(command);
		}
		
		public String getPreviousCmd(){
			System.out.println(cmdIndex);
			if(cmdIndex>0){
				return userCmd.get(cmdIndex--);
			}else{
				return userCmd.get(0);
			}			
		}
		
		public String getLaterCmd(){
			System.out.println(cmdIndex);
			if(cmdIndex<userCmd.size()-1){
				return userCmd.get(++cmdIndex);
			}else return "";
		}
	}
}
```
###### src\gui\WelcomeAndChooseStorage.java
``` java
 * @date 2016å¹´3æœˆ1æ—¥ ä¸‹å�ˆ10:08:56
 * @version 1.0
 */
public class WelcomeAndChooseStorage extends AppPage {
	
	public WelcomeAndChooseStorage() {
		super("/view/html/launch.html");
		JSObject win = (JSObject) webEngine
				.executeScript("window");
		win.setMember("app", new WelcomeBridge());
	}

	// JavaScript interface object
	public class WelcomeBridge {

		public void chooseFolder()  {
			JFileChooser fileChooser = new JFileChooser("");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showOpenDialog(fileChooser);
			if (returnVal == JFileChooser.APPROVE_OPTION) {				
				App.filePath=fileChooser.getSelectedFile().getAbsolutePath()+"\\J.Listee.txt";
				// create file under the file folder chosen by user
				try {
					GUIController.createFile(App.filePath);
					//display starting page
					GUIController.initializeList(App.stage, App.filePath);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(fileChooser, e.getMessage());
				}
			}
		}
	}

}
```
###### src\main\App.java
``` java
 * @date 2016å¹´3æœˆ1æ—¥ ä¸‹å�ˆ5:03:02
 * @version 1.0
 */
public class App extends Application{	
	//This the scene stage of application
	public static Stage stage;
	//this is the task file
	public static String filePath;

	public static void main(String[] args){
		Application.launch(args);			
	}


	@Override
	public void start(Stage primaryStage) {	
		stage = primaryStage;
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
			GUIController.displayWelcome(stage);
		}else{		
			GUIController.initializeList(stage, filePath);
		}
	}

}
```
###### src\storage\LogStorage.java
``` java
 * @date 2016年3月2日 下�?�1:16:22 
 * @version 1.0 
 */
public class LogStorage {
	  //this is the log file path, storing the location of the task file
		public static String logFile="logs\\J.Listee.log";
		
		
	/**
	 * Read the log file, and find the file of task list. 
	 * check if log file exists, if not exists, it's the very first time that user use J.Listee
	 * 
	 * @return null if there's no file path in the log file or log file doesn't exist, else return file path
	 * @throws IOException
	 *             If an I/O error occurs during readLine()
	 */
	public static String readLog() throws IOException {
		File file = new File(logFile);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					throw new IOException("Error: Cannot create log file");
				}
				return null;
			}else{
				return readLogFile();
			}
	}

	/**
	 * Write in the log file the filepath
	 * @throws IOException
	 *             If an I/O error occurs during operations of bw and fos
	 */
	public static void writeLogFile(String filePath) throws IOException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(logFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			bw.write(filePath);
			bw.flush();
			fos.close();
			bw.close();
		} catch ( FileNotFoundException e) {
			throw new FileNotFoundException("Log file not found!");
		} catch (IOException e) {
			throw new IOException("Cannot write to log file!");
		}
		
	}
	
	/**
	 * read from the log file to find the filePath
	 * 
	 * @return the file path in the log file, null if it doesn't exist
	 * @throws IOException
	 */
	private static String readLogFile() throws IOException{
		String filePath=null;
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					logFile)));
		String lineStr;
			lineStr = br.readLine();
			if(lineStr!=null){
				filePath=lineStr;
			}
			br.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Log file not found!");
		}catch (IOException e) {
			throw new IOException("Cannot read from log file!");
		}		
		return filePath;
	}
}
```
###### src\storage\LogStorageTest.java
``` java
 * @date 2016å¹´3æœˆ23æ—¥ ä¸‹å�ˆ7:10:13 
 * @version 1.0 
 */
public class LogStorageTest {
	File logFile=new File(LogStorage.logFile);
	String filePath="This is a file Path";
	
	@Before
	public void setUp() throws Exception {		
		if(logFile.exists()){
			System.out.println("exist");
			System.out.println(logFile.delete());
		}
	}

	//Test read log file
	//This is the test case  for the â€˜read nonexistent log fileâ€™ partition
	@Test
	public void testReadNonExistentLog() throws IOException {
		assertEquals(null, LogStorage.readLog());
	}
	
	//This is the test case for 'read empty log file' partition
	@Test 
	public void testReadEmptyLog() throws IOException{
		assertEquals(null, LogStorage.readLog());
	}
	
	//This the test case for 'read valid log file' partition
	@Test
	public void testReadValidLog() throws IOException{
		FileOutputStream fos = new FileOutputStream(LogStorage.logFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));	
		bw.write(filePath);
		bw.flush();
		fos.close();
		bw.close();
		assertEquals(filePath, LogStorage.readLog());
	}
	
	//Test writeLogFile()
	//This is the test case for 'write directory file' partition
	@Test
	public void testWriteNonexistentFile() {
		LogStorage.logFile="D:\\";
		try {
			LogStorage.writeLogFile("");
			fail( "My method didn't throw IOException" );
		} catch (IOException e) {
		}
	}
	
	//This the test case for 'valid wrting' partition
	@Test
	public void testValidWriting() throws IOException{
		LogStorage.logFile="D:\\J.Listee.log";
		LogStorage.writeLogFile(filePath);
		assertEquals(filePath, LogStorage.readLog());
	}

}
```
