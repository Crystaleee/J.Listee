/*
 * @@author Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */
package History;

import java.util.ArrayList;

import bean.Display;

public class History {

	private static ArrayList<String> userInputs = new ArrayList<String>();
	private static ArrayList<Display> oldDisplays = new ArrayList<Display>();
	private static int oldDisplaysIndex = -1;

	public static void saveDisplay(Display display) {
		if (oldDisplaysIndex < (oldDisplays.size() - 1)) {
			for (int i = (oldDisplays.size() - 1); i > oldDisplaysIndex; i--) {
				oldDisplays.remove(i);
			}
		}
		
        System.out.println("Fs " + display.getFloatTasks().size());
		oldDisplays.add(display);
		oldDisplaysIndex++;
		for(int i =0; i<oldDisplaysIndex+1; i++){
            for(int j =0; j<oldDisplays.get(i).getFloatTasks().size(); j++){
                System.out.println(oldDisplays.get(i).getFloatTasks().get(j).getDescription());
            }
        }
		System.out.println();
	}

	public static void saveUserInput(String userInput) {
		userInputs.add(userInput);
	}

	public static boolean atLastState() {
		return (oldDisplaysIndex == (oldDisplays.size() - 1));
	}

	public static boolean atFirstState() {
		return (oldDisplaysIndex == 0);
	}

	public static Display getDisplay(int offset) {
		oldDisplaysIndex += offset;
		System.out.println(oldDisplays.get(oldDisplaysIndex).getFloatTasks().size());
		return oldDisplays.get(oldDisplaysIndex);
	}

}
