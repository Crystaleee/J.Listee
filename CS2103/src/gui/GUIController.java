package gui;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import entity.Display;
import logic.Logic;
import main.App;
import storage.LogStorage;

/**
 * @@author A0149527W
 */
public class GUIController {
    // this is the width and height of the window
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 780;

    private static final String ERR_CREATEFILE = "Error: Cannot create file!";
    private static final String ERR_DISPLAY_NULL = "Error: Display is null!";

    // this is the stage to be display
    private static Stage stage;

    // these are the app pages
    private static AppPage welcome;
    private static AppPage showList;
    private static AppPage help;

    public static void setStage(Stage stage_) {
        stage = stage_;
    }

    /**
     * create J.Listee file under the given file path
     * 
     * @throws IOException
     *             if file can not be created
     */
    public static void createFile(String userChosenfile) throws IOException {
        LogStorage.writeLogFile(userChosenfile);
        if (!Logic.createFile(userChosenfile)) {
            throw new IOException(ERR_CREATEFILE);
        }
    }

    /**
     * display welcome page in the frame
     */
    public static void displayWelcome() {
        welcome = new WelcomePage();
        setScene(stage.getScene(), welcome);
    }

    /**
     * display list page in the frame
     * 
     * @param display
     * @return
     */
    public static void displayList(Display display) {
        // set list and reload the list page
        ((ShowListPage) showList).setList(display);

        setScene(stage.getScene(), showList);
    }

    /**
     * display help page in the frame
     * 
     * @param display
     * @return
     */
    public static void displayHelp() {
        help = new HelpPage();
        setScene(stage.getScene(), help);
    }

    /**
     * @param scene
     */
    private static void setScene(Scene scene, AppPage page) {
        if (scene == null) {
            scene = new Scene(page, WINDOW_WIDTH, WINDOW_HEIGHT);
            setStyleOnScene(scene);
            stage.setScene(scene);
        } else {
            if (scene.getRoot() != page) {
                stage.getScene().setRoot(page);
            }
        }
        stage.sizeToScene();
        stage.show();
    }

    /**
     * set scene's fill to transparent, set it movable by mouse set esc on close
     * 
     * @param scene
     */
    private static void setStyleOnScene(Scene scene) {
        scene.setFill(null);
        setMouseMovable(scene);
        setEscOnClose(scene);
    }

    /**
     * set esc on close
     * 
     * @param stage
     * @param scene
     */
    private static void setEscOnClose(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.ESCAPE) {
                    App.terminate();
                }
            }
        });
    }

    /**
     * add mouse listener to scene
     */
    private static void setMouseMovable(Scene scene) {
        // add mouse press lisnter
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                App.xOffset = event.getSceneX();
                App.yOffset = event.getSceneY();
            }
        });
        // add mouse drag listener
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - App.xOffset);
                stage.setY(event.getScreenY() - App.yOffset);
            }
        });
    }

    /**
     * initialize the start page which display deadlines, events and floating
     * tasks
     */
    public static void initializeList(String filePath) {
        // call to logic to get all the tasks
        Display display = Logic.initializeProgram(filePath);

        assert display != null : ERR_DISPLAY_NULL;

        showList = new ShowListPage(display);
        displayList(display);
    }

    /**
     * handle user input: pass to logic, and display new content
     * 
     * @param command
     *            user input
     */
    public static void handelUserInput(String command) {
        Display display = Logic.executeUserCommand(command);

        assert display != null : ERR_DISPLAY_NULL;

        displayList(display);
    }

    public static AppPage getShowList() {
        return showList;
    }

    public static void changeFilePath(String filePath) {
        Display display = Logic.changeFilePath(filePath);

        assert display != null : ERR_DISPLAY_NULL;

        displayList(display);
    }

}
