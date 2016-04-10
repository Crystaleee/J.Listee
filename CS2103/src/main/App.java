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
import entity.GlobalLogger;
import gui.GUIController;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

/**
 * @@author A0149527W
 */
public class App extends Application {
    // shortcut to quick launch
    private static final String SHORTCUT_LAUNCH = "control alt SPACE";

    // the tray icon path
    private static final String ICON_TRAY = "/view/images/tray-icon.png";
    private static final String ICON_DESKTOP = "/view/images/desktop-icon.png";
    
    //the window title and menu items
    private static final String TITLE_WINDOW="J.Listee";
    private static final String MENU_SHOW="Show";
    private static final String MENU_MINIMIZE="Minimize";
    private static final String MENU_EXIT="Exit";

    // This the scene stage of application
    public static Stage stage;

    // this is the task file
    public static String filePath;

    // this is the coordinates of the window
    public static double xOffset;
    public static double yOffset;

    // shortcut provider
    private static Provider shortcut = Provider.getCurrentProvider(false);

    // global hotey listener
    private HotKeyListener listener = null;

    // the tray icon of this app
    private static TrayIcon trayIcon;

    public static void main(String[] args) {
        GlobalLogger.createLogHandler();
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);

        setHotKeyListener();

        // register global hotkey
        shortcut.register(KeyStroke.getKeyStroke(SHORTCUT_LAUNCH), listener);

        stage = primaryStage;
        
        //set tray
        enableTray();
        
        //set stage style
        initializeStage();
        
        //show start page
        judgeAndShowStart();
    }

    /**
     * initilaize stage styles
     */
    private void initializeStage() {
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream(ICON_DESKTOP)));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(TITLE_WINDOW);
        stage.setResizable(false);
        GUIController.setStage(stage);
    }

    /**
     * set hot key listener
     */
    private void setHotKeyListener() {
        listener = new HotKeyListener() {
            @Override
            public void onHotKey(HotKey hotkey) {
                if (stage.isShowing()) {
                    hideStage();
                } else {
                    showStage();
                }
            }
        };
    }

    private void enableTray() {
        PopupMenu popupMenu = new PopupMenu();
        java.awt.MenuItem openItem = new java.awt.MenuItem(MENU_SHOW);
        java.awt.MenuItem hideItem = new java.awt.MenuItem(MENU_MINIMIZE);
        java.awt.MenuItem quitItem = new java.awt.MenuItem(MENU_EXIT);

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
            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(ICON_TRAY));
            trayIcon = new TrayIcon(image, TITLE_WINDOW, popupMenu);
            trayIcon.setToolTip(TITLE_WINDOW);
            tray.add(trayIcon);
            trayIcon.addMouseListener(mouseListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ActionListener setTrayActionListener() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();

                if (item.getLabel().equals(MENU_EXIT)) {
                    terminate();
                    return;
                }
                if (item.getLabel().equals(MENU_SHOW)) {
                    showStage();
                    return;
                }
                if (item.getLabel().equals(MENU_MINIMIZE)) {
                    hideStage();
                    return;
                }
            }
        };
        return actionListener;
    }

    /**
     * mouse listener of the tray icon
     * 
     * @return when double click icon, hide or show the stage
     */
    private MouseListener setMouseListener() {
        MouseListener mouseListener = new MouseListener() {
            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (stage.isShowing()) {
                        hideStage();
                    } else {
                        showStage();
                    }
                }
            }
        };
        return mouseListener;
    }


    /**
     * new a thread to show the stage
     */
    private void showStage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.show();
            }
        });
    }

    /**
     * new a thread to hide the stage
     */
    private void hideStage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.hide();
            }
        });
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

    /**
     * terminate the app
     */
    public static void terminate() {
        SystemTray.getSystemTray().remove(trayIcon);
        shortcut.reset();
        shortcut.stop();
        Platform.exit();
    }

}
