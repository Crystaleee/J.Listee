package logic;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.Display;
import storage.Storage;

public class SystematicTest {

	private static Display display;
	private Storage storage = Storage.getInstance();
	private String filepath = "src\\storage\\tests\\systematicTest.txt";

	@Before
	public void createTestFile() {
		Logic.createFile(filepath);
		Logic.initializeProgram(filepath);
	}

	@After
	public void deleteTestFile() {
		File testFile = new File(filepath);
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
	 * Adding Tasks Tests
	 * 
	 * @throws IOException
	 *             *
	 **********************/

	@Test
	public void testAddFloating() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		String expected = "added: \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testAddDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due Thursday 3pm @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testAddDeadlineWithoutTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due Thursday @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testAddEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		String expected = "added: \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testAddEmpty() throws IOException {
		display = Logic.executeUserCommand("add");
		String expected = "Pls enter a description";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	/************************
	 * Updating Tasks Tests
	 * 
	 * @throws IOException
	 *             *
	 ************************/

	@Test
	public void testUpdateFloatingDescription() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateFloatingLocation() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 @newPlace");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateFloatingTags() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 -#tag #newTag");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateDeadlineDescription() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateDeadlineTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow @NUS #tag");
		display = Logic.executeUserCommand("update 1 Friday 4pm");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateEventStartTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 from Thursday 12pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateEventEndTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 to Thursday 7pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testUpdateEventTimes() throws IOException {
		display = Logic.executeUserCommand("add Event Test Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 Wednesday 9 pm to Thursday 10am");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	/************************
	 * Deleting Tasks Tests
	 * 
	 * @throws IOException
	 *             *
	 ************************/

	@Test
	public void testDeleteFloating() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testDeleteDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testDeleteEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testDeleteAll() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete all");
		String expected = "All tasks deleted";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testDeleteMultiple() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1,2,3");
		String expected = "deleted: \"Deadline Test\", \"Event Test\", \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testDeleteInvalidTaskNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 5");
		String expected = "You have specified invalid task numbers: 5";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	/*************************
	 * Reserving Tasks Tests
	 * 
	 * @throws IOException
	 *             *
	 *************************/

	@Test
	public void testReserveSingle() throws IOException {
		display = Logic.executeUserCommand("reserve Reservation Test Thursday 3pm to 4pm @NUS #tag");
		String expected = "Reserved: Reservation Test";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testReserveDouble() throws IOException {
		display = Logic
				.executeUserCommand("reserve Reservation Test Thursday 3pm to 4pm and Friday 4pm to 6pm @NUS #tag");
		String expected = "Reserved: Reservation Test";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	/***********************
	 * Undo and Redo Tests
	 * 
	 * @throws IOException
	 *             *
	 ***********************/

	@Test
	public void testUndo() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		String expected = "Undid last command";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testRedo() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		display = Logic.executeUserCommand("redo");
		String expected = "Redid last command";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testRedoError() throws IOException {
		display = Logic.executeUserCommand("redo");
		String expected = "You have reached the latest point possible";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	/******************************
	 * Mark Done and Undone Tests *
	 * 
	 * @throws IOException
	 *             *
	 ******************************/

	@Test
	public void testMarkDone() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		String expected = "Completed: \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testMarkDoneInvalidNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testMarkUnDone() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show Test");
		display = Logic.executeUserCommand("undone 1");
		String expected = "Undone task: \"Floating Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

	@Test
	public void testMarkUnDoneInvalidNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show Test");
		display = Logic.executeUserCommand("undone 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertEquals(display.toString(), storageDisplay.toString());
	}

}
