//@@author Chloe Odquier Fortuna (A0149063E)
package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import parser.InputSuggestion;

public class InputSuggestionTest {

	InputSuggestion inputSuggester = InputSuggestion.getInstance();

	@Test
	public void deleteAndDoneTest() {
		String actual = inputSuggester.getSuggestedInput("d");
		String expected = "delete <task number(s)> / done <task number(s)>";
		assertEquals(actual, expected);
	}

	@Test
	public void deleteTest() {
		String actual = inputSuggester.getSuggestedInput("de");
		String expected = "delete <task number(s)>";
		assertEquals(actual, expected);
	}

	@Test
	public void doneTest() {
		String actual = inputSuggester.getSuggestedInput("do");
		String expected = "done <task number(s)>";
		assertEquals(actual, expected);
	}

	@Test
	public void undoUndoneAndUpdateTest() {
		String actual = inputSuggester.getSuggestedInput("u");
		String expected = "undo / undone <task number(s)> / update <task number>";
		assertEquals(actual, expected);
	}

	@Test
	public void undoAndUndoneTest() {
		String actual1 = inputSuggester.getSuggestedInput("un");
		String actual2 = inputSuggester.getSuggestedInput("undo");
		String expected = "undo / undone <task number(s)>";
		assertEquals(actual1, actual2, expected);
	}

	@Test
	public void updateTest() {
		String actual = inputSuggester.getSuggestedInput("up");
		String expected = "update <task number> [new description] [-delete time(s)] [new time(s)] [-@delete location] [@new location] [-#delete tag] [#new tag]";
		assertEquals(actual, expected);
	}

	@Test
	public void redoAndReserveTest() {
		String actual1 = inputSuggester.getSuggestedInput("r");
		String actual2 = inputSuggester.getSuggestedInput("re");
		String expected = "redo / reserve <description> <start dates to end dates>";
		assertEquals(actual1, actual2, expected);
	}

	@Test
	public void redoTest() {
		String actual = inputSuggester.getSuggestedInput("red");
		String expected = "redo";
		assertEquals(actual, expected);
	}

	@Test
	public void reserveTest() {
		String actual = inputSuggester.getSuggestedInput("res");
		String expected = "reserve <description> <start dates to end dates> [@location] [#tag]";
		assertEquals(actual, expected);
	}

}
