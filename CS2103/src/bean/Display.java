package bean;

import java.util.ArrayList;

public class Display{

    private String message;
    private ArrayList<Task> taskList;

    public Display(){
        message = "";
        taskList = null;
    }
    
    public Display(String message, ArrayList<Task> taskList){
        this.message = message;
        this.taskList = taskList;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setList(ArrayList<Task> taskList){
        this.taskList = taskList;
    }
    
    public ArrayList<Task> getList(){
        return taskList;
    }
}