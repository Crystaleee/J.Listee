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

import ui.App;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月2日 下午1:16:22 
 * @version 1.0 
 */
public class LogStorage {
	  //this is the log file path, storing the location of the task file
		public static String logFile="D:\\J.Listee.log";
		
		
	/**
	 * Read the log file, and find the file of task list. 
	 * check if log file exists, if not exists, it's the very first time that user use J.Listee
	 * 
	 * @return null if there's no file path in the log file or log file doesn't exist, else return file path
	 * @throws IOException
	 *             If an I/O error occurs during readLine()
	 */
	public static String readLog() {
		File file = new File(logFile);
		try {	
			if (!file.exists()) {
				file.createNewFile();
				return null;
			}else{
				return readLogFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write in the log file the filepath
	 * 
	 * @throws IOException
	 *             If an I/O error occurs during operations of bw and fos
	 */
	public static void writeLogFile(String filePath) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(logFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			bw.write(filePath);
			bw.flush();
			fos.close();
			bw.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * read from the log file to find the filePath
	 * 
	 * @return the file path in the log file, null if it doesn't exist
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String readLogFile() throws FileNotFoundException, IOException {
		String filePath=null;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
				logFile)));
		String lineStr = br.readLine();
		if(lineStr!=null){
			filePath=lineStr;
		}
		br.close();
		return filePath;
	}
}
