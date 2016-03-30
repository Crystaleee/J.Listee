package logic;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.Display;

public class SystematicTest {

	private static Display display;

	@Before
	public void createTestFile() {
		Logic.createFile("src\\storage\\tests\\systematicTest.txt");
		Logic.initializeProgram("src\\storage\\tests\\systematicTest.txt");
	}

	@After
	public void deleteTestFile() {
		File testFile = new File("src\\storage\\tests\\systematicTest.txt");
		testFile.delete();
	}

	/**************************
	 * Input Validation Tests *
	 **************************/

	@Test
	public void testInvalidCommand() {
		display = Logic.executeUserCommand("This is an invalid command.");
		String expected = "You have specified an invalid command";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testEmptyCommand() {
		display = Logic.executeUserCommand("");
		String expected = "You have specified an invalid command";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	/**********************
	 * Adding Tasks Tests *
	 **********************/

	@Test
	public void testAddFloating() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		String expected = "added: \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testAddDeadlineFormatOne() {
		display = Logic.executeUserCommand("add Deadline Test Thursday 3pm @NUS #tag");
		String expected = "added: \"Deadline Test Thursday 3pm\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testAddDeadlineFormatTwo() {
		display = Logic.executeUserCommand("add Deadline Test 31/3/16 15:00 @NUS #tag");
		String expected = "added: \"Deadline Test 31/3/16 15:00\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testAddDeadlineWithoutTime() {
		display = Logic.executeUserCommand("add Deadline Test Thursday @NUS #tag");
		String expected = "added: \"Deadline Test Thursday\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testAddEvent() {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		String expected = "added: \"Event Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testAddEmpty() {
		display = Logic.executeUserCommand("add");
		String expected = "Pls enter a description";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	/************************
	 * Updating Tasks Tests *
	 ************************/

	@Test
	public void testUpdateFloatingDescription() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUpdateFloatingLocation() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 @newPlace");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUpdateFloatingTags() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 -#tag #newTag");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
}
