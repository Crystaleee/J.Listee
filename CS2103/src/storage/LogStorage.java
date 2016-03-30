package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @@author  A0149527W
 * @date 2016年3月2日 下午1:16:22 
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
	public static String readLogFile() throws IOException{
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
