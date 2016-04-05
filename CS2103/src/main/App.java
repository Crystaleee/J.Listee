package main;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import storage.LogStorage;
import gui.GUIController;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
/**
 * @@author A0149527W
 */
public class App extends Application{
	private static final String SHORTCUT_LAUNCH="control ALT";
	private HotKeyListener listener=null;
	//the tray icon of this app
	private TrayIcon trayIcon;
	// This the scene stage of application
	public static Stage stage;
	// this is the task file
	public static String filePath;
	public static double xOffset;
	public static double yOffset;
	private static Provider shortcut=Provider.getCurrentProvider(false);
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		listener=new HotKeyListener() {
			
			@Override
			public void onHotKey(HotKey hotkey) {
				if (stage.isShowing()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.hide();
						}
					});
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.show();
						}
					});
				}
			}
		};
        shortcut.register(KeyStroke.getKeyStroke("control alt SPACE"), listener);
		stage = primaryStage;

		enableTray();

		stage.initStyle(StageStyle.UNDECORATED);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle("J.Listee");
		stage.setResizable(false);
		GUIController.setStage(stage);
		judgeAndShowStart();
	}

	private void enableTray() {
		PopupMenu popupMenu = new PopupMenu();
		java.awt.MenuItem openItem = new java.awt.MenuItem("Show");
		java.awt.MenuItem hideItem = new java.awt.MenuItem("Minimize");
		java.awt.MenuItem quitItem = new java.awt.MenuItem("Exit");

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();
				//override default the application will shutdown when it's last window is hidden.
				Platform.setImplicitExit(false); 

				if (item.getLabel().equals("Exit")) {
					SystemTray.getSystemTray().remove(trayIcon);
					terminate();
					return;
				}
				if (item.getLabel().equals("Show")) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.show();
						}
					});
					return;
				}
				if (item.getLabel().equals("Minimize")) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.hide();
						}
					});
					return;
				}
			}
		};

		// double click
		MouseListener mouseListener = new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}

			public void mouseClicked(MouseEvent e) {
				Platform.setImplicitExit(false); 
				if (e.getClickCount() == 2) {
					if (stage.isShowing()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.hide();
							}
						});
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								stage.show();
							}
						});
					}
				}
			}
		};

		openItem.addActionListener(actionListener);
		quitItem.addActionListener(actionListener);
		hideItem.addActionListener(actionListener);
 
		popupMenu.add(openItem);
		popupMenu.add(hideItem);
		popupMenu.add(quitItem);
 
		try {
			SystemTray tray = SystemTray.getSystemTray();
			BufferedImage image = ImageIO.read(this.getClass()
					.getResourceAsStream("image.jpg"));
			trayIcon = new TrayIcon(image, "J.Listee", popupMenu);
			trayIcon.setToolTip("J.Listee");
			tray.add(trayIcon);
			trayIcon.addMouseListener(mouseListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * judge if it's the first time user use this app and show start page
	 */
	private void judgeAndShowStart() {
		// read log file
		try {
			filePath = LogStorage.readLog();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		// check if it's the first time that user use the application
		if (filePath == null) {
			GUIController.displayWelcome();
		} else {
			GUIController.initializeList(filePath);
		}
	}

	public static void terminate(){
		shortcut.reset();
		shortcut.stop();
		Platform.exit();
	}

}
