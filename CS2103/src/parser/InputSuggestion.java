//@@author Chloe Odquier Fortuna (A0149063E)
package parser;

import java.util.ArrayList;

public class InputSuggestion {

	private static final String SUGGESTION_ADD = "add <description> at/from/due/etc [time] [@location] [#tag]";
	private static final String SUGGESTION_DELETE = "delete <task number(s)>";
	private static final String SUGGESTION_UPDATE = "update <task number> [reservation index] [new description] [(-)start/(-)end] [(-)@location] [(-)#tag]";
	private static final String SUGGESTION_RESERVE = "reserve <description> <start dates to end dates> [@location] [#tag]";
	private static final String SUGGESTION_CONFIRM = "confirm <task number(s)>";
	private static final String SUGGESTION_DONE = "done <task number(s)>";
	private static final String SUGGESTION_UNDONE = "undone <task number(s)>";
	private static final String SUGGESTION_SHOW = "show [/task-group] [keyword]";
	private static final String SUGGESTION_UNDO = "undo";
	private static final String SUGGESTION_REDO = "redo";
	private static final String SUGGESTION_HELP = "help";
	private static final String SUGGESTION_EXIT = "exit";
	private static final String SUGGESTION_INVALID = "INVALID COMMAND!";

	private static final String SUGGESTION_DELETE_DONE = "delete <task number(s)> // done <task number(s)>";
	private static final String SUGGESTION_UNDO_UNDONE = "undo / undone <task number(s)>";
	private static final String SUGGESTION_UNDO_UNDONE_UPDATE = "undo / undone <task number(s)> / update <task number>";
	private static final String SUGGESTION_REDO_RESERVE = "redo / reserve <description> <start dates to end dates>";

	private static final String COMMAND_ADD = "add ";
	private static final String COMMAND_DELETE = "delete ";
	private static final String COMMAND_UPDATE = "update ";
	private static final String COMMAND_RESERVE = "reserve ";
	private static final String COMMAND_CONFIRM = "confirm ";
	private static final String COMMAND_DONE = "done ";
	private static final String COMMAND_UNDONE = "undone ";
	private static final String COMMAND_SHOW = "show ";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_HELP = "help";
	private static final String COMMAND_EXIT = "exit";

	private static InputSuggestion inputSuggester;

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
		} else if ("d".startsWith(currentInput)) {
			return SUGGESTION_DELETE_DONE;
		} else if ("re".startsWith(currentInput)) {
			return SUGGESTION_REDO_RESERVE;
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
		} else if (currentInput.startsWith(COMMAND_DELETE)) {
			return SUGGESTION_DELETE;
		} else if (currentInput.startsWith(COMMAND_UPDATE)) {
			return SUGGESTION_UPDATE;
		} else if (currentInput.startsWith(COMMAND_RESERVE)) {
			return SUGGESTION_RESERVE;
		} else if (currentInput.startsWith(COMMAND_CONFIRM)) {
			return SUGGESTION_CONFIRM;
		} else if (currentInput.startsWith(COMMAND_DONE)) {
			return SUGGESTION_DONE;
		} else if (currentInput.startsWith(COMMAND_UNDONE)) {
			return SUGGESTION_UNDONE;
		} else if (currentInput.startsWith(COMMAND_SHOW)) {
			return SUGGESTION_SHOW;
		} else if (currentInput.equals(COMMAND_UNDO)) {
			return SUGGESTION_UNDO;
		} else if (currentInput.equals(COMMAND_REDO)) {
			return SUGGESTION_REDO;
		} else if (currentInput.equals(COMMAND_HELP)) {
			return SUGGESTION_HELP;
		} else if (currentInput.equals(COMMAND_EXIT)) {
			return SUGGESTION_EXIT;
		} else {
			return SUGGESTION_INVALID;
		}
	}

	private ArrayList<String> consolidateSuggestions() {
		ArrayList<String> suggestions = new ArrayList<String>();

		suggestions.add(SUGGESTION_ADD);
		suggestions.add(SUGGESTION_RESERVE);
		suggestions.add(SUGGESTION_DELETE);
		suggestions.add(SUGGESTION_UPDATE);
		suggestions.add(SUGGESTION_SHOW);
		suggestions.add(SUGGESTION_CONFIRM);
		suggestions.add(SUGGESTION_DONE);
		suggestions.add(SUGGESTION_UNDONE);
		suggestions.add(SUGGESTION_UNDO);
		suggestions.add(SUGGESTION_REDO);
		suggestions.add(SUGGESTION_HELP);
		suggestions.add(SUGGESTION_EXIT);

		return suggestions;
	}

}

