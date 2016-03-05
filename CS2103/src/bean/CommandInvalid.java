/*
 * Written by Boh Tuang Hwee, Jehiel (A0139995E)
 * Last updated: 3/5/2016, 8:00pm
 * CS2103
 */

package bean;
public class CommandInvalid extends Command{
    private final String MESSAGE_INVALID_COMMAND = "Pls enter a valid command";
    
    public CommandInvalid(){
    }
    
    public Display execute(Display display){
        display.setMessage(MESSAGE_INVALID_COMMAND);;
        return display;
    }
}
