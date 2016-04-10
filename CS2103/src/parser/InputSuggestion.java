//@@author A0149063E
package parser;

import java.util.ArrayList;

import entity.Display;
import logic.Logic;

public class InputSuggestion {

	private static final String SUGGESTION_ADD = "add <description> [\"at/from/due/etc\"] [start time] [\"to\" end time] [@location] [#tag]";

	private static final String SUGGESTION_DEL = "del <task number(s)>";
	private static final String SUGGESTION_DELETE = "delete <task number(s)>";

	private static final String SUGGESTION_UPDATE = "update <task number> [description] [(-)/time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_UPDATE_FLOAT = "update <task number> [new description] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_UPDATE_DEADLINE = "update <task number> [new description] [(-)/end [time]] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_UPDATE_EVENT = "update <task number> [description] [(-)/time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_UPDATE_RESERVED = "<task #> [reserved block #] [description] [/time-group time(s)] [(-)@location] [(-)#tag]";

	private static final String SUGGESTION_EDIT = "edit <task number> [description] [(-)/time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT_FLOAT = "edit <task number> [new description] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT_DEADLINE = "edit <task number> [new description] [(-)/end [time]] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT_EVENT = "edit <task number> [description] [(-)/time-group time(s)] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_EDIT_RESERVED = "<task #> [reserved block #] [description] [/time-group time(s)] [(-)@location] [(-)#tag]";

	private static final String SUGGESTION_RES = "res <description> <\"from\" start date(s) \"to\" end date(s)> [@location] [#tag]";
	private static final String SUGGESTION_RESERVE = "reserve <description> <\"from\" start date(s) \"to\" end date(s)> [@location] [#tag]";

	private static final String SUGGESTION_CFM = "cfm <task number> <reserved block number>";
	private static final String SUGGESTION_CONFIRM = "confirm <task number> <reserved block number>";

	private static final String SUGGESTION_DONE = "done  <task number(s)>";
	private static final String SUGGESTION_UNDONE = "undone  <task number(s)>";

	private static final String SUGGESTION_SEARCH = "search [/task-group] [keyword] [@location] [#tag]";
	private static final String SUGGESTION_SHOW = "show [/task-group] [keyword] [@location] [#tag]";

	private static final String SUGGESTION_POSTPONE = "postpone <task number> <time to postpone by>";
	private static final String SUGGESTION_PP = "pp <task number> <time to postpone by>";

	private static final String SUGGESTION_UNDO = "undo";
	private static final String SUGGESTION_REDO = "redo";

	private static final String SUGGESTION_FILEPATH = "change filepath";

	private static final String SUGGESTION_HELP = "help";
	private static final String SUGGESTION_EXIT = "exit";

	private static final String SUGGESTION_INVALID_COMMAND = "Invalid command!";
	private static final String SUGGESTION_INVALID_TASK = "Invalid task number!";

	private static final String SUGGESTION_EDIT_EXIT = "edit <task number> // exit";
	private static final String SUGGESTION_CONFIRM_FILEPATH = "cfm/confirm <task number(s)> <reserved block number(s)> // change filepath";
	private static final String SUGGESTION_DELETE_DEL = "del/delete <task number(s)>";
	private static final String SUGGESTION_DELETE_DONE = "del/delete <task number(s)> // done <task number(s)>";
	private static final String SUGGESTION_POSTPONE_PP = "pp/postpone <task number(s)> <time to postpone by>";
	private static final String SUGGESTION_SHOW_SEARCH = "show/search [/task-group] [keyword] [@location] [#tag]";
	private static final String SUGGESTION_UNDO_UNDONE = "undo // undone <task number(s)>";
	private static final String SUGGESTION_UNDO_UNDONE_UPDATE = "update <task number> // undo // undone <task number(s)>";
	private static final String SUGGESTION_REDO_RESERVE = "redo // reserve <description> <start dates to end dates>";

	private static final String COMMAND_ADD = "add ";

	private static final String COMMAND_DELETE = "delete ";
	private static final String COMMAND_DEL = "del ";

	private static final String COMMAND_UPDATE = "update ";
	private static final String COMMAND_EDIT = "edit ";

	private static final String COMMAND_RESERVE = "reserve ";
	private static final String COMMAND_RES = "res ";

	private static final String COMMAND_CONFIRM = "confirm ";
	private static final String COMMAND_CFM = "cfm ";

	private static final String COMMAND_DONE = "done ";
	private static final String COMMAND_UNDONE = "undone ";

	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SHOW = "show ";

	private static final String COMMAND_POSTPONE = "postpone ";
	private static final String COMMAND_PP = "pp ";

	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";

	private static final String COMMAND_FILEPATH = "change filepath";

	private static final String COMMAND_HELP = "help";
	private static final String COMMAND_EXIT = "exit";

	private static InputSuggestion inputSuggester;
	private static Display display = Logic.getDisplay();

	private static boolean isFloatTask;
	private static boolean isDeadlineTask;
	private static boolean isEvent;
	private static boolean isReservationTask;
	private static boolean isInvalidTask;

	private boolean hasInvalidTaskNumber(int taskNumber) {
		int numberOfTasks = display.getVisibleDeadlineTasks().size() + display.getVisibleEvents().size()
				+ display.getVisibleFloatTasks().size() + display.getVisibleReservedTasks().size();
		return ((taskNumber > numberOfTasks) || (taskNumber < 1));
	}

	private void determineTaskType(String userInput) {
		String[] splitInput = userInput.split("\\s+");
		if (splitInput.length >= 2) {
			Integer taskNumber = Integer.parseInt(splitInput[1]);
			isFloatTask = false;
			isDeadlineTask = false;
			isEvent = false;
			isReservationTask = false;
			isInvalidTask = false;

			if (!hasInvalidTaskNumber(taskNumber)) {
				if (taskNumber <= display.getVisibleDeadlineTasks().size()) {
					isDeadlineTask = true;
				} else {
					taskNumber -= display.getVisibleDeadlineTasks().size();
					if (taskNumber <= display.getVisibleEvents().size()) {
						isEvent = true;
					} else {
						taskNumber -= display.getVisibleEvents().size();
						if (taskNumber <= display.getVisibleFloatTasks().size()) {
							isFloatTask = true;
						} else {
							taskNumber -= display.getVisibleFloatTasks().size();
							isReservationTask = true;
						}
					}
				}
			} else {
				isInvalidTask = true;
			}
		}
	}

	public static InputSuggestion getInstance() {
		if (inputSuggester == null) {
			return new InputSuggestion();
		}
		return inputSuggester;
	}

	public String getSuggestedInput(String currentInput) {
		currentInput = currentInput.toLowerCase();
		return getSuggestionBeforeTypingCommand(currentInput);
	}

	private String getSuggestionBeforeTypingCommand(String currentInput) {
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

	private String getSuggestionWhileTypingCommand(String currentInput) {
		ArrayList<String> suggestions = consolidateSuggestions();
		for (String suggestion : suggestions) {
			if (suggestion.startsWith(currentInput)) {
				return suggestion;
			}
		}
		return getSuggestionAfterTypingCommand(currentInput);
	}

	private String getSuggestionAfterTypingCommand(String currentInput) {
		if (currentInput.startsWith(COMMAND_ADD)) {
			return SUGGESTION_ADD;
		} else if (currentInput.startsWith(COMMAND_DEL)) {
			return SUGGESTION_DEL;
		} else if (currentInput.startsWith(COMMAND_DELETE)) {
			return SUGGESTION_DELETE;
		} else if (currentInput.startsWith(COMMAND_UPDATE)) {
			determineTaskType(currentInput);
			if (isFloatTask) {
				return SUGGESTION_UPDATE_FLOAT;
			} else if (isDeadlineTask) {
				return SUGGESTION_UPDATE_DEADLINE;
			} else if (isEvent) {
				return SUGGESTION_UPDATE_EVENT;
			} else if (isReservationTask) {
				return SUGGESTION_UPDATE_RESERVED;
			} else if (isInvalidTask) {
				return SUGGESTION_INVALID_TASK;
			} else {
				return SUGGESTION_UPDATE;
			}
		} else if (currentInput.startsWith(COMMAND_EDIT)) {
			determineTaskType(currentInput);
			if (isFloatTask) {
				return SUGGESTION_EDIT_FLOAT;
			} else if (isDeadlineTask) {
				return SUGGESTION_EDIT_DEADLINE;
			} else if (isEvent) {
				return SUGGESTION_EDIT_EVENT;
			} else if (isReservationTask) {
				return SUGGESTION_EDIT_RESERVED;
			} else if (isInvalidTask) {
				return SUGGESTION_INVALID_TASK;
			} else {
				return SUGGESTION_UPDATE;
			}
		} else if (currentInput.startsWith(COMMAND_RESERVE)) {
			return SUGGESTION_RESERVE;
		} else if (currentInput.startsWith(COMMAND_RES)) {
			return SUGGESTION_RES;
		} else if (currentInput.startsWith(COMMAND_CONFIRM)) {
			determineTaskType(currentInput);
			if (!isReservationTask) {
				return SUGGESTION_INVALID_TASK;
			} else {
				return SUGGESTION_CONFIRM;
			}
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
		} else if (currentInput.equals(COMMAND_FILEPATH)) {
			return SUGGESTION_FILEPATH;
		} else if (currentInput.equals(COMMAND_HELP)) {
			return SUGGESTION_HELP;
		} else if (currentInput.equals(COMMAND_EXIT)) {
			return SUGGESTION_EXIT;
		} else {
			return SUGGESTION_INVALID_COMMAND;
		}
	}

	private ArrayList<String> consolidateSuggestions() {
		ArrayList<String> suggestions = new ArrayList<String>();

		suggestions.add(SUGGESTION_ADD);
		suggestions.add(SUGGESTION_RESERVE);
		suggestions.add(SUGGESTION_DEL);
		suggestions.add(SUGGESTION_DELETE);
		suggestions.add(SUGGESTION_UPDATE);
		suggestions.add(SUGGESTION_EDIT);
		suggestions.add(SUGGESTION_SHOW);
		suggestions.add(SUGGESTION_SEARCH);
		suggestions.add(SUGGESTION_CONFIRM);
		suggestions.add(SUGGESTION_CFM);
		suggestions.add(SUGGESTION_DONE);
		suggestions.add(SUGGESTION_UNDONE);
		suggestions.add(SUGGESTION_UNDO);
		suggestions.add(SUGGESTION_REDO);
		suggestions.add(SUGGESTION_FILEPATH);
		suggestions.add(SUGGESTION_POSTPONE);
		suggestions.add(SUGGESTION_PP);
		suggestions.add(SUGGESTION_HELP);
		suggestions.add(SUGGESTION_EXIT);

		return suggestions;
	}

}
