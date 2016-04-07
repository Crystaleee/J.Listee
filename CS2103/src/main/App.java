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
import javafx.scene.image.Image;
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
	//shortcut to quick launch
	private static final String SHORTCUT_LAUNCH="control alt SPACE";
	
	//the tray icon path
	private static final String ICON_TRAY="/view/images/tray-icon.png";
	private static final String ICON_DESKTOP="/view/images/desktop-icon.png";
	
	// This the scene stage of application
	public static Stage stage;
	
	// this is the task file
	public static String filePath;
	
	// this is the coordinates of the window
	public static double xOffset;
	public static double yOffset;
	
	//shortcut provider
	private static Provider shortcut=Provider.getCurrentProvider(false);
	
	//global hotey listener
	private HotKeyListener listener=null;
	
	//the tray icon of this app
	private static TrayIcon trayIcon;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Platform.setImplicitExit( false );

		setHotKeyListener();
		
		//register global hotkey
        shortcut.register(KeyStroke.getKeyStroke(SHORTCUT_LAUNCH), listener);
        
		stage = primaryStage;

		enableTray();
		
		stage.getIcons().add(new Image(this.getClass().getResourceAsStream(ICON_DESKTOP)));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setTitle("J.Listee");
		stage.setResizable(false);
		GUIController.setStage(stage);
		judgeAndShowStart();
	}

	/**
	 * set hot key listener
	 */
	private void setHotKeyListener() {
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
	}

	private void enableTray() {
		PopupMenu popupMenu = new PopupMenu();
		java.awt.MenuItem openItem = new java.awt.MenuItem("Show");
		java.awt.MenuItem hideItem = new java.awt.MenuItem("Minimize");
		java.awt.MenuItem quitItem = new java.awt.MenuItem("Exit");

		ActionListener actionListener = setTrayActionListener();

		// set listener for double click
		MouseListener mouseListener = setMouseListener();

		openItem.addActionListener(actionListener);
		quitItem.addActionListener(actionListener);
		hideItem.addActionListener(actionListener);
 
		popupMenu.add(openItem);
		popupMenu.add(hideItem);
		popupMenu.add(quitItem);
 
		try {
			SystemTray tray = SystemTray.getSystemTray();
			BufferedImage image = ImageIO.read(this.getClass()
					.getResourceAsStream(ICON_TRAY));
			trayIcon = new TrayIcon(image, "J.Listee", popupMenu);
			trayIcon.setToolTip("J.Listee");
			tray.add(trayIcon);
			trayIcon.addMouseListener(mouseListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private ActionListener setTrayActionListener() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();

				if (item.getLabel().equals("Exit")) {				
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
		return actionListener;
	}

	/**
	 * @return
	 */
	private MouseListener setMouseListener() {
		MouseListener mouseListener = new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}

			public void mouseClicked(MouseEvent e) {
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
		return mouseListener;
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
		SystemTray.getSystemTray().remove(trayIcon);
		shortcut.reset();
		shortcut.stop();
		Platform.exit();
	}

}
