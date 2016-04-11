//@@author A0149063E
package parser;

import java.util.ArrayList;

import entity.Display;
import logic.Logic;

public class InputSuggestion {

	/* Suggestions for each of the commands. */
	private static final String SUGGESTION_ADD = "add <description> [at/from/due/etc] [start time] [to end time] [@location] [#tag]";
	private static final String SUGGESTION_BACK = "back";
	private static final String SUGGESTION_CHANGE_FILEPATH = "change filepath";
	private static final String SUGGESTION_CFM = "cfm <task number> <timeslot number>";
	private static final String SUGGESTION_CONFIRM = "confirm <task number> <timeslot number>";
	private static final String SUGGESTION_DEL = "del <task number(s)>";
	private static final String SUGGESTION_DELETE = "delete <task number(s)>";
	private static final String SUGGESTION_DONE = "done <task number(s)>";
	private static final String SUGGESTION_UPDATE = "update <task number> [description] [(-/+)time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_UPDATE_RESERVED = "update <task number> [+del timeslot(s)] [timeslot #] [description] [+time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT = "edit <task number> [description] [(-/+)time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT_RESERVED = "edit <task number> [+del timeslot(s)] [timeslot #] [description] [+time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EXIT = "exit";
	private static final String SUGGESTION_HELP = "help";
	private static final String SUGGESTION_PP = "pp <task number> <time to postpone by>";
	private static final String SUGGESTION_POSTPONE = "postpone <task number> <time to postpone by>";
	private static final String SUGGESTION_REDO = "redo";
	private static final String SUGGESTION_RES = "res <description> <from start date(s) to end date(s)> [@location] [#tag]";
	private static final String SUGGESTION_RESERVE = "reserve <description> <from start date(s) to end date(s)> [@location] [#tag]";
	private static final String SUGGESTION_SEARCH = "search [/task-group] [keyword] [at/from/due/etc time] [@location] [#tag]";
	private static final String SUGGESTION_SHOW = "show [/task-group] [keyword] [at/from/due/etc time] [@location] [#tag]";
	private static final String SUGGESTION_UNDO = "undo";
	private static final String SUGGESTION_UNDONE = "undone  <task number(s)>";
	private static final String SUGGESTION_INVALID_COMMAND = "Invalid command!";

	/* Suggestions for commands that start with the same letter or substring. */
	private static final String SUGGESTION_EDIT_EXIT = "edit <task number> // exit";
	private static final String SUGGESTION_CONFIRM_FILEPATH = "cfm/confirm <task number> <timeslot number> // change filepath";
	private static final String SUGGESTION_DELETE_DEL = "del/delete <task number(s)>";
	private static final String SUGGESTION_DELETE_DONE = "del/delete <task number(s)> // done <task number(s)>";
	private static final String SUGGESTION_POSTPONE_PP = "pp/postpone <task number> <time to postpone by>";
	private static final String SUGGESTION_SHOW_SEARCH = "show/search [/task-group] [keyword] [at/from/due/etc time] [@location] [#tag]";
	private static final String SUGGESTION_UNDO_UNDONE = "undo // undone <task number(s)>";
	private static final String SUGGESTION_UNDO_UNDONE_UPDATE = "update <task number> // undo // undone <task number(s)>";
	private static final String SUGGESTION_REDO_RESERVE = "redo // res/reserve <description> <from start date(s) to end date(s)>";
	private static final String SUGGESTION_RES_RESERVE = "res/reserve <description> <from start date(s) to end date(s)>";

	/* The commands that the user can type in. */
	private static final String COMMAND_ADD = "add ";
	private static final String COMMAND_BACK = "back";
	private static final String COMMAND_CHANGE_FILEPATH = "change filepath";
	private static final String COMMAND_CFM = "cfm ";
	private static final String COMMAND_CONFIRM = "confirm ";
	private static final String COMMAND_DEL = "del ";
	private static final String COMMAND_DELETE = "delete ";
	private static final String COMMAND_DONE = "done ";
	private static final String COMMAND_UPDATE = "update ";
	private static final String COMMAND_EDIT = "edit ";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_HELP = "help";
	private static final String COMMAND_PP = "pp ";
	private static final String COMMAND_POSTPONE = "postpone ";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_RES = "res ";
	private static final String COMMAND_RESERVE = "reserve ";
	private static final String COMMAND_SEARCH = "search ";
	private static final String COMMAND_SHOW = "show ";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_UNDONE = "undone ";

	private static InputSuggestion inputSuggester;

	/**
	 * Get an instance of InputSuggestion for other classes to use.
	 * 
	 * @return An InputSuggestion object.
	 */
	public static InputSuggestion getInstance() {
		if (inputSuggester == null) {
			return new InputSuggestion();
		}
		return inputSuggester;
	}

	/**
	 * Gets a suggested input based on what the user is typing.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              An input suggestion.
	 */
	public String getSuggestedInput(String currentInput) {
		currentInput = currentInput.toLowerCase();
		return getSuggestionForCommandsThatStartWithSameLetter(currentInput);
	}

	/**
	 * Gets a suggested input based on the first few characters the user has
	 * typed in. These suggestions are for commands that start with the same
	 * letter.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              An input suggestion.
	 */
	private String getSuggestionForCommandsThatStartWithSameLetter(String currentInput) {
		if (currentInput.isEmpty()) {
			return null;
		} else if ("c".startsWith(currentInput)) {
			return SUGGESTION_CONFIRM_FILEPATH;
		} else if ("d".startsWith(currentInput)) {
			return SUGGESTION_DELETE_DONE;
		} else if ("del".startsWith(currentInput)) {
			return SUGGESTION_DELETE_DEL;
		} else if ("e".startsWith(currentInput)) {
			return SUGGESTION_EDIT_EXIT;
		} else if ("p".startsWith(currentInput)) {
			return SUGGESTION_POSTPONE_PP;
		} else if ("re".startsWith(currentInput)) {
			return SUGGESTION_REDO_RESERVE;
		} else if ("res".startsWith(currentInput)) {
			return SUGGESTION_RES_RESERVE;
		} else if ("s".startsWith(currentInput)) {
			return SUGGESTION_SHOW_SEARCH;
		} else if ("u".startsWith(currentInput)) {
			return SUGGESTION_UNDO_UNDONE_UPDATE;
		} else if ("undo".startsWith(currentInput)) {
			return SUGGESTION_UNDO_UNDONE;
		} else {
			return getSuggestionWhileTypingCommand(currentInput);
		}
	}

	/**
	 * Gets a suggested input for a command based on what the user is typing.
	 * This command is for when the user is in the middle of writing the
	 * command.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              A suggested input.
	 */
	private String getSuggestionWhileTypingCommand(String currentInput) {
		ArrayList<String> suggestions = consolidateSuggestions();
		for (String suggestion : suggestions) {
			if (suggestion.startsWith(currentInput)) {
				return suggestion;
			}
		}
		return getSuggestionAfterTypingCommand(currentInput);
	}

	/**
	 * Gets a suggested input after the user has typed in the command and is
	 * currently typing in the parameters for the command.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              A suggested input.
	 */
	private String getSuggestionAfterTypingCommand(String currentInput) {
		if (currentInput.startsWith(COMMAND_ADD)) {
			return SUGGESTION_ADD;
		} else if (currentInput.startsWith(COMMAND_BACK)) {
			return SUGGESTION_BACK;
		} else if (currentInput.startsWith(COMMAND_DEL)) {
			return SUGGESTION_DEL;
		} else if (currentInput.startsWith(COMMAND_DELETE)) {
			return SUGGESTION_DELETE;
		} else if (currentInput.startsWith(COMMAND_UPDATE)) {
			return getUpdateSuggestion(currentInput);
		} else if (currentInput.startsWith(COMMAND_EDIT)) {
			return getEditSuggestion(currentInput);
		} else if (currentInput.startsWith(COMMAND_RESERVE)) {
			return SUGGESTION_RESERVE;
		} else if (currentInput.startsWith(COMMAND_RES)) {
			return SUGGESTION_RES;
		} else if (currentInput.startsWith(COMMAND_CONFIRM)) {
			return SUGGESTION_CONFIRM;
		} else if (currentInput.startsWith(COMMAND_CFM)) {
			return SUGGESTION_CFM;
		} else if (currentInput.startsWith(COMMAND_DONE)) {
			return SUGGESTION_DONE;
		} else if (currentInput.startsWith(COMMAND_UNDONE)) {
			return SUGGESTION_UNDONE;
		} else if (currentInput.startsWith(COMMAND_SHOW)) {
			return SUGGESTION_SHOW;
		} else if (currentInput.startsWith(COMMAND_SEARCH)) {
			return SUGGESTION_SEARCH;
		} else if (currentInput.startsWith(COMMAND_POSTPONE)) {
			return SUGGESTION_POSTPONE;
		} else if (currentInput.startsWith(COMMAND_PP)) {
			return SUGGESTION_PP;
		} else if (currentInput.equals(COMMAND_UNDO)) {
			return SUGGESTION_UNDO;
		} else if (currentInput.equals(COMMAND_REDO)) {
			return SUGGESTION_REDO;
		} else if (currentInput.equals(COMMAND_CHANGE_FILEPATH)) {
			return SUGGESTION_CHANGE_FILEPATH;
		} else if (currentInput.equals(COMMAND_HELP)) {
			return SUGGESTION_HELP;
		} else if (currentInput.equals(COMMAND_EXIT)) {
			return SUGGESTION_EXIT;
		} else {
			return SUGGESTION_INVALID_COMMAND;
		}
	}

	/**
	 * Consolidates all of the suggestions into an ArrayList, allowing
	 * getSuggestionWhileTypingCommand() to iterate over a list while looking
	 * for a suggestions.
	 * 
	 * @return  An ArrayList of input suggestions.
	 */
	private ArrayList<String> consolidateSuggestions() {
		ArrayList<String> suggestions = new ArrayList<String>();

		suggestions.add(SUGGESTION_ADD);
		suggestions.add(SUGGESTION_BACK);
		suggestions.add(SUGGESTION_CHANGE_FILEPATH);
		suggestions.add(SUGGESTION_CFM);
		suggestions.add(SUGGESTION_CONFIRM);
		suggestions.add(SUGGESTION_DEL);
		suggestions.add(SUGGESTION_DELETE);
		suggestions.add(SUGGESTION_DONE);
		suggestions.add(SUGGESTION_UPDATE);
		suggestions.add(SUGGESTION_EDIT);
		suggestions.add(SUGGESTION_EXIT);
		suggestions.add(SUGGESTION_HELP);
		suggestions.add(SUGGESTION_PP);
		suggestions.add(SUGGESTION_POSTPONE);
		suggestions.add(SUGGESTION_REDO);
		suggestions.add(SUGGESTION_RESERVE);
		suggestions.add(SUGGESTION_SEARCH);
		suggestions.add(SUGGESTION_SHOW);
		suggestions.add(SUGGESTION_UNDONE);
		suggestions.add(SUGGESTION_UNDO);

		return suggestions;
	}

	/**
	 * Gets the input suggestion for updating a task based on whether it is a
	 * Reserved Task or not.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              An input suggestion.
	 */
	private String getUpdateSuggestion(String currentInput) {
		if (isReservedTask(currentInput)) {
			return SUGGESTION_UPDATE_RESERVED;
		} else {
			return SUGGESTION_UPDATE;
		}
	}

	/**
	 * Gets the input suggestion for editing a a task based on whether it is a
	 * Reserved Task or not.
	 * 
	 * @param currentInput  What the user has currently typed in.
	 * @return              An input suggestion.
	 */
	private String getEditSuggestion(String currentInput) {
		if (isReservedTask(currentInput)) {
			return SUGGESTION_EDIT_RESERVED;
		} else {
			return SUGGESTION_EDIT;
		}
	}

	/**
	 * Determines whether a Task is a ReservedTask or not.
	 * 
	 * @param userInput  What the user has currently typed in.
	 * @return           True if the user types in a Reserved Task. Otherwise, false.
	 */
	private boolean isReservedTask(String userInput) {
		Display display = Logic.getDisplay();
		String[] splitInput = userInput.split("\\s+");

		if (splitInput.length >= 2) {
			Integer taskNumber = Integer.parseInt(splitInput[1]);

			if (!hasInvalidTaskNumber(taskNumber)) {
				if (taskNumber > display.getVisibleDeadlineTasks().size()) {
					taskNumber -= display.getVisibleDeadlineTasks().size();
					if (taskNumber > display.getVisibleEvents().size()) {
						taskNumber -= display.getVisibleEvents().size();
						if (taskNumber > display.getVisibleFloatTasks().size()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks to see if the user inputed an invalid task.
	 * 
	 * @param taskNumber  The task number the user has typed in.
	 * @return            True if it is an invalid task number. Otherwise, false.
	 */
	private boolean hasInvalidTaskNumber(int taskNumber) {
		Display display = Logic.getDisplay();
		int numberOfTasks = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
				+ display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size();
		return ((taskNumber > numberOfTasks) || (taskNumber < 1));
	}

}