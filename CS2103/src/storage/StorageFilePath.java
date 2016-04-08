//@@author A0149063E
package storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StorageFilePath {
	
	private static String readOldFilePath() throws IOException {
		return LogStorage.readLog();
	}
	
	public static void changeFilePath(String newFilePathString) throws IOException {
		String oldFilePathString = readOldFilePath();
		
		File newFile = new File(newFilePathString);
		if(!newFile.exists()) {
			Path oldFilePath = Paths.get(oldFilePathString);
			Path newFilePath = Paths.get(newFilePathString);
			Files.move(oldFilePath, newFilePath, StandardCopyOption.ATOMIC_MOVE);
		}
		LogStorage.writeLogFile(newFilePathString);
	}
}




