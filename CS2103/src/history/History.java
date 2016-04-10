/*
 * @@author A0139995E
 */
package history;

import java.util.ArrayList;

import entity.Display;

public class History {

    //private static ArrayList<String> userInputs = new ArrayList<String>();
    private static ArrayList<Display> oldDisplays = new ArrayList<Display>();
    private static int oldDisplaysIndex = -1;

    public static void saveDisplay(Display display) {
        if (oldDisplaysIndex < (oldDisplays.size() - 1)) {
            for (int i = (oldDisplays.size() - 1); i > oldDisplaysIndex; i--) {
                oldDisplays.remove(i);
            }
        }
        oldDisplays.add(display);
        oldDisplaysIndex++;
    }

    public static boolean atLastState() {
        return (oldDisplaysIndex == (oldDisplays.size() - 1));
    }

    public static boolean atFirstState() {
        return (oldDisplaysIndex == 0);
    }

    public static Display getDisplay(int offset) {
        if (offset > 0) {
            if (atLastState()) {
                return null;
            }
            oldDisplaysIndex += offset;
            if (indexOutOfRange()) {
                oldDisplaysIndex = oldDisplays.size() - 1;
            }
        } else if (offset < 0) {
            if (atFirstState()) {
                return null;
            }
            oldDisplaysIndex += offset;
            if (oldDisplaysIndex < 0) {
                oldDisplaysIndex = 0;
            }
        }
        return oldDisplays.get(oldDisplaysIndex);
    }

    private static boolean indexOutOfRange() {
        return oldDisplaysIndex > (oldDisplays.size() - 1);
    }

}
