// @@author A0149063E
package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import parser.InputSuggestion;

public class InputSuggestionTest {

	InputSuggestion inputSuggester = InputSuggestion.getInstance();

	/* Partition for commands that start with "d" */
	@Test
	public void deleteAndDoneTest() {
		String actual = inputSuggester.getSuggestedInput("d");
		String expected = "del/delete <task number(s)> // done <task number(s)>";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "del" */
	@Test
	public void deleteTest() {
		String actual = inputSuggester.getSuggestedInput("de");
		String expected = "del/delete <task number(s)>";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "do" */
	@Test
	public void doneTest() {
		String actual = inputSuggester.getSuggestedInput("do");
		String expected = "done <task number(s)>";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "u" */
	@Test
	public void undoUndoneAndUpdateTest() {
		String actual = inputSuggester.getSuggestedInput("u");
		String expected = "update <task number> // undo // undone <task number(s)>";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "un" */
	@Test
	public void undoAndUndoneTest() {
		String actual1 = inputSuggester.getSuggestedInput("un");
		String actual2 = inputSuggester.getSuggestedInput("undo");
		String expected = "undo // undone <task number(s)>";
		assertEquals(actual1, actual2, expected);
	}

	/* Partition for commands that start with "up" */
	@Test
	public void updateTest() {
		String actual = inputSuggester.getSuggestedInput("up");
		String expected = "update <task number> [description] [(-/+)time-group time(s)] [(-)@location] [(-)#tag]";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "re" */
	@Test
	public void redoAndReserveTest() {
		String actual1 = inputSuggester.getSuggestedInput("r");
		String actual2 = inputSuggester.getSuggestedInput("re");
		String expected = "redo // res/reserve <description> <from start date(s) to end date(s)>";
		assertEquals(actual1, actual2, expected);
	}

	/* Partition for commands that start with "red" */
	@Test
	public void redoTest() {
		String actual = inputSuggester.getSuggestedInput("red");
		String expected = "redo";
		assertEquals(actual, expected);
	}

	/* Partition for commands that start with "res" */
	@Test
	public void reserveTest() {
		String actual = inputSuggester.getSuggestedInput("res");
		String expected = "res/reserve <description> <from start date(s) to end date(s)>";
		assertEquals(actual, expected);
	}

}