package storage;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月23日 下午7:10:13 
 * @version 1.0 
 */
public class LogStorageTest {
	File logFile=new File(LogStorage.logFile);
	String filePath="This is a file Path";
	
	@Before
	public void setUp() throws Exception {		
		if(logFile.exists()){
			System.out.println("exist");
			System.out.println(logFile.delete());
		}
	}

	//Test read log file
	//This is the test case  for the ‘read nonexistent log file’ partition
	@Test
	public void testReadNonExistentLog() throws IOException {
		assertEquals(null, LogStorage.readLog());
	}
	
	//This is the test case for 'read empty log file' partition
	@Test 
	public void testReadEmptyLog() throws IOException{
		assertEquals(null, LogStorage.readLog());
	}
	
	//This the test case for 'read valid log file' partition
	@Test
	public void testReadValidLog() throws IOException{
		FileOutputStream fos = new FileOutputStream(LogStorage.logFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));	
		bw.write(filePath);
		bw.flush();
		fos.close();
		bw.close();
		assertEquals(filePath, LogStorage.readLog());
	}
	
	//Test writeLogFile()
	//This is the test case for 'write directory file' partition
	@Test
	public void testWriteNonexistentFile() {
		LogStorage.logFile="D:\\";
		try {
			LogStorage.writeLogFile("");
			fail( "My method didn't throw IOException" );
		} catch (IOException e) {
		}
	}
	
	//This the test case for 'valid wrting' partition
	@Test
	public void testValidWriting() throws IOException{
		LogStorage.logFile="D:\\J.Listee.log";
		LogStorage.writeLogFile(filePath);
		assertEquals(filePath, LogStorage.readLog());
	}

}
