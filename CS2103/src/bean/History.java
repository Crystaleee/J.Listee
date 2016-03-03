/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/4/2016, 4:30AM
 * CS2103
 */
 package bean;
 
import java.util.ArrayList;

public class History {
    
    private static ArrayList<String> userInputs;
    private static ArrayList<ArrayList<Task>> oldTasks;
    private static int oldTasksIndex;
    
    public static void saveList(ArrayList<Task> tasks) {
        if(oldTasksIndex < (oldTasks.size() -1)){
            for(int i = (oldTasks.size() - 1); i > oldTasksIndex; i--){
                oldTasks.remove(i);
            }
        }
        oldTasks.add(tasks);
        oldTasksIndex++;
    }
    
    public static void saveUserInput(String userInput) {
        userInputs.add(userInput);
    }
    
    public static boolean atLastState() {
        return (oldTasksIndex == (oldTasks.size() -1));
    }
    
    public static boolean atFirstState() {
        return (oldTasksIndex == 0);
    }

    public static void decrementIndex() {
        oldTasksIndex--;
    }

    public static void incrementIndex() {
        oldTasksIndex++;
    }

    public static ArrayList<Task> getTaskList(int offset) {
        return oldTasks.get(oldTasksIndex + offset);
    }

}
