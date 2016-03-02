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
		//this is the task file
		public static String filePath;
		
	/**
	 * Read the log file, and find the file of task list. 
	 * check if log file exists, if not exists, it's the very first time that user use J.Listee
	 * 
	 * @throws IOException
	 *             If an I/O error occurs during readLine()
	 */
	public static void readLog() {
		File file = new File(logFile);
		try {	
			if (!file.exists()) {
				file.createNewFile();
				App.isFirstTime=true;
			}else{
				readLogFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write all the items in List<String> texts into the file
	 * 
	 * @throws IOException
	 *             If an I/O error occurs during operations of bw and fos
	 */
	public static void writeLogFile() {
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
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void readLogFile() throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
				logFile)));
		String lineStr = br.readLine();
		filePath=lineStr;
		br.close();
	}
}
