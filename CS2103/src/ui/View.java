package ui;

import java.awt.CardLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import bean.Task;

/**
 * @author Zhu Bingjing
 * @date 2016年3月1日 下午8:48:26
 * @version 1.0
 */
public class View {
	protected static String programPath;
	private static final int WINDOW_WIDTH=600;
	private static final int WINDOW_HEIGHT=590;
	
	private static CardLayout card = new CardLayout(0, 0);
	private static JPanel panels = new JPanel(card);
	private static JFrame frame = new JFrame("J.Listee");
	protected static WelcomeAndChooseStorage welcome;
	public static ShowList showList;
	
	public View() {
		initialize();
		welcome=new WelcomeAndChooseStorage();
		panels.add(welcome.browserView, "welcome");
		frame.add(panels);
		setFrame();
	}
	
	/**
	 * set parameters about programPath and os name
	 */
	static void initialize() {
		programPath = System.getProperty("user.dir");
		System.setProperty("os.name", "Windows 10");		
	}

	/**
	 * set frame parameters
	 * 
	 * @param browserView
	 * @return the frame
	 */
	private static void setFrame() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setLocationRelativeTo(null);
		//frame.setUndecorated(true);
		frame.setVisible(false);
	}

	/**
	 * display list in the frame
	 * 
	 * @param taskList
	 */
	public static void displayList(List<Task> taskList) {
		View.showList=new ShowList(taskList);
		View.panels.add(View.showList.browserView,"showList");
		View.card.show(View.panels, "showList");
		View.frame.setVisible(true);
	}	
	
	/**
	 * display welcome page in the frame
	 */
	public static void displayWelcome() {
		View.card.show(View.panels, "welcome");
		View.frame.setVisible(true);
	}
}
