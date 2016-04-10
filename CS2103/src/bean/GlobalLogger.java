// @@author A0149063E
package bean;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GlobalLogger {

	private static final String LOGGER_ERROR = "Unable to set up logger.";
	private static final String LOGGER_FILE = "JListeeLog.log";

	private static Logger logger = Logger.getGlobal();
	private static FileHandler handler;

	public static void createLogHandler() {
		try {
			handler = new FileHandler(LOGGER_FILE);
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (IOException e) {
			System.out.println(LOGGER_ERROR);
		}
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void closeHandler() {
		handler.close();
	}
}
