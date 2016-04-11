// @@author A0149063E
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entity.Display;
import logic.Logic;

public class IntegrationTest {

	private Display display;
	private String filepath = "src\\tests\\integrationTest.txt";

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
	 * Adding Tasks Tests *
	 **********************/

	@Test
	public void testAddFloating() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		String expected = "added: \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddDeadline() {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr 3pm @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: Deadline Test\r\n"
				+ "Deadline: 14/04/16 15:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddDeadlineWithoutTime() {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr @NUS #tag");
		String expected = "added: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: Deadline Test\r\n"
		        + "Deadline: 14/04/16 23:59\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddEvent() {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		String expected = "added: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=["
		        + "Description: Event Test\r\n"
		        + "Start Date: 14/04/16 15:00\r\n"
		        + "End Date: 14/04/16 16:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testAddEmpty() {
		display = Logic.executeUserCommand("add");
		String expected = "Please enter a description";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertEquals("Display [message=Please enter a description, events=[], deadlineTasks=[], "
				+ "floatTasks=[], reservedTasks=[], completedTasks=[]]", display.toString());
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
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: New Description\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateFloatingLocation() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 @newPlace");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: newPlace\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateFloatingTags() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("update 1 -#tag #newTag");
		String expected = "Edited : \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #newTag\r\n\r\n]"));
	}

	@Test
	public void testUpdateDeadlineDescription() {
		display = Logic.executeUserCommand("add Deadline Test due April 11 @NUS #tag");
		display = Logic.executeUserCommand("update 1 New Description");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: New Description\r\n"
		        + "Deadline: 11/04/16 23:59\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateDeadlineTime() {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow @NUS #tag");
		display = Logic.executeUserCommand("update 1 +end 15th Apr 4pm");
		String expected = "Edited : \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks="
		        + "[Description: Deadline Test\r\n"
		        + "Deadline: 15/04/16 16:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventStartTime() {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +start 14th Apr 12pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=["
		        + "Description: Event Test\r\n"
		        + "Start Date: 14/04/16 12:00"
		        + "\r\nEnd Date: 14/04/16 16:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventEndTime() {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +end 14th Apr 7pm");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=["
		        + "Description: Event Test\r\n"
		        + "Start Date: 14/04/16 15:00\r\n"
		        + "End Date: 14/04/16 19:00\r\nLocation: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	@Test
	public void testUpdateEventTimes() {
		display = Logic.executeUserCommand("add Event Test from 14th Apr 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("update 1 +both 13th Apr 9 pm to 14th Apr 10am");
		String expected = "Edited : \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=["
		        + "Description: Event Test\r\n"
		        + "Start Date: 13/04/16 21:00\r\n"
		        + "End Date: 14/04/16 10:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
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
		assertTrue(display.toString().contains("floatTasks=[]"));
	}

	@Test
	public void testDeleteDeadline() {
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Deadline Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=[]"));
	}

	@Test
	public void testDeleteEvent() {
		display = Logic.executeUserCommand("add Event Test from tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1");
		String expected = "deleted: \"Event Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=[]"));
	}

	@Test
	public void testDeleteAll() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete all");
		String expected = "All tasks deleted";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], "
		        + "reservedTasks=[], completedTasks=[]]"));
	}

	@Test
	public void testDeleteMultiple() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("add Deadline Test due tomorrow 3pm @NUS #tag");
		display = Logic.executeUserCommand("add Event Test from tomorrow 3pm to 4pm @NUS #tag");
		display = Logic.executeUserCommand("delete 1,2,3");
		String expected = "deleted: \"Deadline Test\", \"Event Test\", \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], "
		        + "reservedTasks=[], completedTasks=[]]"));
	}

	@Test
	public void testDeleteInvalidTaskNumber() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("delete 5");
		String expected = "You have specified invalid numbers: 5";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	/*************************
	 * Reserving Tasks Tests *
	 *************************/

	@Test
	public void testReserveSingle() {
		display = Logic.executeUserCommand("reserve Reservation Test from 14th Apr 3pm to 4pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("reservedTasks=["
		        + "Description: Reservation Test\r\n"
		        + "Start Dates: 14/04/16 15:00\r\n"
		        + "End Dates: 14/04/16 16:00\r\n"
				+ "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testReserveDouble() {
		display = Logic.executeUserCommand(
				"reserve Reservation Test from 14th Apr 3pm to 4pm and 15th Apr 4pm to 6pm @NUS #tag");
		String expected = "Reserved: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("reservedTasks=["
		        + "Description: Reservation Test\r\n"
	        	+ "Start Dates: 14/04/16 15:00, 15/04/16 16:00\r\n"
				+ "End Dates: 14/04/16 16:00, 15/04/16 18:00\r\n"
		        + "Location: NUS\r\nTags: #tag\r\n\r\n]"));
	}

	/***********************
	 * Undo and Redo Tests *
	 ***********************/

	@Test
	public void testUndo() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		String expected = "Undid previous commands";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=[]"));
	}

	@Test
	public void testRedo() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("undo");
		display = Logic.executeUserCommand("redo");
		String expected = "Redid command(s)";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
	         	+ "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
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
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], "
		        + "reservedTasks=[], completedTasks=[]"));
	}

	@Test
	public void testMarkDoneInvalidNumber() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 10");
		String expected = "You have specified invalid task numbers: 10";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testMarkUnDone() {
		display = Logic.executeUserCommand("add Floating Test @NUS #tag");
		display = Logic.executeUserCommand("done 1");
		display = Logic.executeUserCommand("show /done");
		display = Logic.executeUserCommand("undone 1");
		String expected = "Undone task: \"Floating Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("floatTasks=["
		        + "Description: Floating Test\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
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
		assertTrue(display.toString()
				.contains("events=[], deadlineTasks=[], floatTasks=[], "
		        + "reservedTasks=[], completedTasks=[]"));
	}

	/*****************
	 * Confirm Tests *
	 *****************/

	@Test
	public void testConfirmReservation() {
		display = Logic.executeUserCommand(
				"reserve Reservation Test from 14th Apr 3pm to 4pm and 15th Apr 4pm to 6pm @NUS #tag");
		display = Logic.executeUserCommand("confirm 1 1");
		String expected = "added: \"Reservation Test\"";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("events=["
		        + "Description: Reservation Test\r\n"
		        + "Start Date: 14/04/16 15:00\r\n"
		        + "End Date: 14/04/16 16:00\r\n"
				+ "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testConfirmInvalidReservation() {
		display = Logic.executeUserCommand(
				"reserve Reservation Test from 14th Apr 3pm to 4pm and 15th Apr 4pm to 6pm @NUS #tag");
		display = Logic.executeUserCommand("confirm 2 2");
		String expected = "Please specify a valid index";
		String actual = display.getMessage();

		assertEquals(expected, actual);
	}

	/*****************
	 * Postpone Tests *
	 *****************/

	@Test
	public void testPostponeHours() {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr 3pm @NUS #tag");
		display = Logic.executeUserCommand("postpone 1 2hours");
		String expected = "Postponed: Deadline Test";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: Deadline Test\r\n"
	        	+ "Deadline: 14/04/16 17:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testPostponeDays() {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr 3pm @NUS #tag");
		display = Logic.executeUserCommand("postpone 1 2days");
		String expected = "Postponed: Deadline Test";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: Deadline Test\r\n"
		        + "Deadline: 16/04/16 15:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}

	@Test
	public void testPostponeInvalid() {
		display = Logic.executeUserCommand("add Deadline Test due 14th Apr 3pm @NUS #tag");
		display = Logic.executeUserCommand("postpone 3 2days");
		String expected = "You can only pospone deadline tasks and events!";
		String actual = display.getMessage();

		assertEquals(expected, actual);
		assertTrue(display.toString().contains("deadlineTasks=["
		        + "Description: Deadline Test\r\n"
		        + "Deadline: 14/04/16 15:00\r\n"
		        + "Location: NUS\r\n"
		        + "Tags: #tag\r\n\r\n]"));
	}
}