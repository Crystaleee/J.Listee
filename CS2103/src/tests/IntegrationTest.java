// @@author A0149063E
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entity.Display;
import logic.Logic;

public class IntegrationTest {

	private Display display;
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
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr 3pm @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"deadlineTasks=[Description: Deadline Test\r\nDeadline: 14/04/16 15:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddDeadlineWithoutTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"deadlineTasks=[Description: Deadline Test\r\nDeadline: 14/04/16 23:59\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		String expected = "added: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"events=[Description: Event Test\r\nStart Date: 14/04/16 15:00\r\nEnd Date: 14/04/16 16:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddEmpty() throws IOException {
		display = Logic.executeUserCommand("add");
		String expected = "Please enter a description";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertEquals(
				"Display [message=Please enter a description, events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]",
				display.toString());
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

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: New Description\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateFloatingLocation() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 @newPlace");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: newPlace\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateFloatingTags() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 -#tag #newTag");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #newTag\r\n\r\n]"));
	}

	@Test
	public void testUpdateDeadlineDescription() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due April 11 @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"deadlineTasks=[Description: New Description\r\nDeadline: 11/04/16 23:59\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateDeadlineTime() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow @NUS #tag");
		display = Logic.executeUserCommand("update 1 +end 15th Apr 4pm");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"deadlineTasks=[Description: Deadline Test\r\nDeadline: 15/04/16 16:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventStartTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +start 14th Apr 12pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"events=[Description: Event Test\r\nStart Date: 14/04/16 12:00\r\nEnd Date: 14/04/16 16:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventEndTime() throws IOException {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +end 14th Apr 7pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"events=[Description: Event Test\r\nStart Date: 14/04/16 15:00\r\nEnd Date: 14/04/16 19:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventTimes() throws IOException {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +both 13th Apr 9 pm to 14th Apr 10am");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"events=[Description: Event Test\r\nStart Date: 13/04/16 21:00\r\nEnd Date: 14/04/16 10:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
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

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=[]"));
	}

	@Test
	public void testDeleteDeadline() throws IOException {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=[]"));
	}

	@Test
	public void testDeleteEvent() throws IOException {
		display = Logic.executeUserCommand("add Event Test from tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=[]"));
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
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]"));
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
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]]"));
	}

	@Test
	public void testDeleteInvalidTaskNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 5");
		String expected = "You have specified invalid numbers: 5";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	/*************************
	 * Reserving Tasks Tests *
	 * @throws IOException   *
	 *************************/

	@Test
	public void testReserveSingle() throws IOException {
		display = Logic.executeUserCommand("reserve Reservation Test from 14th Apr 3pm to 4pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"reservedTasks=[Description: Reservation Test\r\nStart Dates: 14/04/16 15:00\r\nEnd Dates: 14/04/16 16:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testReserveDouble() throws IOException {
		display = Logic.executeUserCommand(
				"reserve Reservation Test from 14th Apr 3pm to 4pm and 15th Apr 4pm to 6pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains(
				"reservedTasks=[Description: Reservation Test\r\nStart Dates: 14/04/16 15:00, 15/04/16 16:00\r\nEnd Dates: 14/04/16 16:00, 15/04/16 18:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
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
		assertTrue(display.toString().contains("floatTasks=[]"));
	}

	@Test
	public void testRedo() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		display = Logic.executeUserCommand("redo");
		String expected = "Redid command(s)";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
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

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]"));
	}

	@Test
	public void testMarkDoneInvalidNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testMarkUnDone() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show /done");
		display = Logic.executeUserCommand("undone 1");
		String expected = "Undone task: \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("floatTasks=[Description: Floating Test\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testMarkUnDoneInvalidNumber() throws IOException {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show Test");
		display = Logic.executeUserCommand("undone 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], reservedTasks=[], completedTasks=[]"));
	}

}