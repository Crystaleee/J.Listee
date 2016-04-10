// @@author A0149063E
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.Display;
import logic.Logic;
import storage.Storage;

public class IntegrationTest {

	private static Display display;
	private Storage storage = Storage.getInstance();
	private String filepath = "src\\tests\\systematicTest.txt";

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

//	@Test
//	public void testChangeFilePath() {
//		GUIController.changeFilePath("C:\\Users\\Chloe\\Downloads\\J.Listee.txt");
//	}
	
	/***********************
	 * Adding Tasks Tests  * 
	 * @throws IOException *
	 ***********************/

	@Test
	public void testAddFloating() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		String expected = "added: \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		System.out.println(display);
		assertTrue(display.toString().contains("floatTasks=[Description: Floating Test"));
	}

	@Test
	public void testAddDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due Thursday 3pm @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=[Description: Deadline Test"));
	}

	@Test
	public void testAddDeadlineWithoutTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due Thursday @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=[Description: Deadline Test"));
	}

	@Test
	public void testAddEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test from Thursday 3pm to 4pm @NUS #tag");
		String expected = "added: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=[Description: Event Test"));
	}

	@Test
	public void testAddEmpty() throws IOException {
		display = Logic.executeUserCommand("add");
		String expected = "Please enter a description";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertEquals("Display [message=Please enter a description, events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]", display.toString());
	}

	/************************
	 * Updating Tasks Tests * 
	 * @throws IOException  *
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
		assertTrue(display.toString().contains("floatTasks=[Description: New Description"));
	}

	@Test
	public void testUpdateFloatingLocation() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 @newPlace");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Location: newPlace"));
	}

	@Test
	public void testUpdateFloatingTags() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 -#tag #newTag");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Tags: #newTag"));
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
		assertTrue(display.toString().contains("deadlineTasks=[Description: New Description"));
	}

	@Test
	public void testUpdateDeadlineTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow @NUS #tag");
		display = Logic.executeUserCommand("update 1 /end Friday 4pm");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("16:00"));
	}

	@Test
	public void testUpdateEventStartTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test from Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 /start Thursday 12pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("12:00"));
	}

	@Test
	public void testUpdateEventEndTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test from Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 /end Thursday 7pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();
		
		assertEquals(expected, actual);
		assertTrue(display.toString().contains("19:00"));
	}

	@Test
	public void testUpdateEventTimes() throws IOException {
		display = Logic.executeUserCommand("add Event Test from Thursday 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 /both Wednesday 9 pm to Thursday 10am");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("21:00"));
		assertTrue(display.toString().contains("10:00"));
	}

	/************************
	 * Deleting Tasks Tests * 
	 * @throws IOException  *
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
		System.out.println(display);
		assertFalse(display.toString().contains("floatTasks=[Floating Test"));
	}

	@Test
	public void testDeleteDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertFalse(display.toString().contains("deadlineTasks=[Deadline Test"));
	}

	@Test
	public void testDeleteEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test from tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		System.out.println("BLAH: " + display);
		assertFalse(display.toString().contains("events=[Event Test"));
	}

	@Test
	public void testDeleteAll() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete all");
		String expected = "All tasks deleted";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertEquals(display.toString(), "Display [message=All tasks deleted, events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]");
	}

	@Test
	public void testDeleteMultiple() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test from tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1,2,3");
		String expected = "deleted: \"Deadline Test\", \"Event Test\", \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertEquals(display.toString(), "Display [message=deleted: \"Deadline Test\", \"Event Test\", \"Floating Test\", events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]");
	}

	@Test
	public void testDeleteInvalidTaskNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 5");
		String expected = "You have specified invalid numbers: 5";
		String actual = display.getMessage();

		Display storageDisplay = storage.getDisplay(filepath);
		storageDisplay.setMessage(expected);

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Floating Test"));
	}

	/*************************
	 * Reserving Tasks Tests * 
	 * @throws IOException   *
	 *************************/

	@Test
	public void testReserveSingle() throws IOException {
		display = Logic.executeUserCommand("reserve Reservation Test from Thursday 3pm to 4pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Reservation Test"));
	}

	@Test
	public void testReserveDouble() throws IOException {
		display = Logic
				.executeUserCommand("reserve Reservation Test from Thursday 3pm to 4pm and Friday 4pm to 6pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Reservation Test"));
	}

	/***********************
	 * Undo and Redo Tests * 
	 * @throws IOException *
	 ***********************/

	@Test
	public void testUndo() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		String expected = "Undid previous commands";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertFalse(display.toString().contains("Floating Test"));
	}

	@Test
	public void testRedo() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		display = Logic.executeUserCommand("redo");
		String expected = "Redid command(s)";
		String actual = display.getMessage();


		assertEquals(expected, actual);
		assertTrue(display.toString().contains("Floating Test"));
	}

	@Test
	public void testRedoError() throws IOException {
		display = Logic.executeUserCommand("redo");
		String expected = "You have reached the latest point possible";
		String actual = display.getMessage();

		assertEquals(expected, actual);
	}

	/******************************
	 * Mark Done and Undone Tests *
	 * @throws IOException        *
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
		display = Logic.executeUserCommand("show /done");
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
