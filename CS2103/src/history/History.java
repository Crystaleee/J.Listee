/*
 * @@author A0139995E
 */
package history;

/**
 * This class stores the "states" of the display for
 * undo/redo command
 */
import java.util.ArrayList;

import entity.Display;

public class History {

    private static ArrayList<Display> _oldDisplays = new ArrayList<Display>();
    private static int _oldDisplaysIndex = -1;

    /*
     * removes any unwanted states before 
     * saving a new display state.
     */
    public static void saveDisplay(Display display) {
        if (_oldDisplaysIndex < (_oldDisplays.size() - 1)) {
            for (int i = (_oldDisplays.size() - 1); i > _oldDisplaysIndex; i--) {
                _oldDisplays.remove(i);
            }
        }
        _oldDisplays.add(display);
        _oldDisplaysIndex++;
    }

    public static boolean atLastState() {
        return (_oldDisplaysIndex == (_oldDisplays.size() - 1));
    }

    public static boolean atFirstState() {
        return (_oldDisplaysIndex == 0);
    }

    /*
     * Gets the display state specified by the offset
     */
    public static Display getDisplay(int offset) {
        if (offset > 0) {
            if (atLastState()) {
                return null;
            }
            _oldDisplaysIndex += offset;
            if (indexOutOfRange()) {
                _oldDisplaysIndex = _oldDisplays.size() - 1;
            }
        } else if (offset < 0) {
            if (atFirstState()) {
                return null;
            }
            _oldDisplaysIndex += offset;
            if (_oldDisplaysIndex < 0) {
                _oldDisplaysIndex = 0;
            }
        }
        return _oldDisplays.get(_oldDisplaysIndex);
    }

    private static boolean indexOutOfRange() {
        return _oldDisplaysIndex > (_oldDisplays.size() - 1);
    }

}
