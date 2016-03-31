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

	@Test
	public void testUpdateDeadlineDescription() {
		display = Logic.executeUserCommand("add Deadline Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateDeadlineTime() {
		display = Logic.executeUserCommand("add Deadline Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 Friday 4pm");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateEventStartTime() {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 from Thursday 12pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateEventEndTime() {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 to Thursday 7pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateEventTimes() {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 Wednesday 9 pm to Thursday 10am");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	/************************
	 * Deleting Tasks Tests *
	 ************************/

	@Test
	public void testDeleteFloating() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeleteDeadline() {
		display = Logic.executeUserCommand("add Deadline Test tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Deadline Test tomorrow 3pm\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeleteEvent() {
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Event Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeleteAll() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete all");
		String expected = "All tasks deleted";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeleteMultiple() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1,2,3");
		String expected = "deleted: \"Event Test\", \"Floating Test\", \"Deadline Test tomorrow 3pm\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeleteInvalidTaskNumber() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 5");
		String expected = "You have specified invalid task numbers: 5";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	/*************************
	 * Reserving Tasks Tests *
	 *************************/

	@Test
	public void testReserveSingle() {
		display = Logic.executeUserCommand("reserve Reservation Test Thursday 3pm to 4pm @NUS #tag");
		String expected = "Reserved: Reservation Test";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testReserveDouble() {
		display = Logic
				.executeUserCommand("reserve Reservation Test Thursday 3pm to 4pm and Friday 4pm to 6pm @NUS #tag");
		String expected = "Reserved: Reservation Test";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testReserveTriple() {
		display = Logic.executeUserCommand(
				"reserve Reservation Test Thursday 3pm to 4pm and Saturday 10 am to 11 am @NUS #tag");
		String expected = "Reserved: Reservation Test";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	/***********************
	 * Undo and Redo Tests *
	 ***********************/

	@Test
	public void testUndo() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		String expected = "Undid last command";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testRedo() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		display = Logic.executeUserCommand("redo");
		String expected = "Redid last command";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

	@Test
	public void testRedoError() {
		display = Logic.executeUserCommand("redo");
		String expected = "You have reached the latest point possible";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	/******************************
	 * Mark Done and Undone Tests *
	 ******************************/
	
	@Test
	public void testMarkDone() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		String expected = "Completed: \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMarkDoneInvalidNumber() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMarkUnDone() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show Test");
		display = Logic.executeUserCommand("undone 1");
		String expected = "Undone task: \"Floating Test\"";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMarkUnDoneInvalidNumber() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show Test");
		display = Logic.executeUserCommand("undone 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();
		assertEquals(expected, actual);
	}

}
