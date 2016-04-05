//@@author Chloe Odquier Fortuna (A0149063E)
package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StorageFilePath {
	
	private String readOldFilePath() throws IOException {
		return LogStorage.readLog();
	}
	
	public void changeFilePath(String newFilePathString) throws IOException {
		String oldFilePathString = readOldFilePath();
		
		Path oldFilePath = Paths.get(oldFilePathString);
		Path newFilePath = Paths.get(newFilePathString);
		
		Files.move(oldFilePath, newFilePath, StandardCopyOption.ATOMIC_MOVE);
		
		LogStorage.writeLogFile(newFilePathString);
	}
}




