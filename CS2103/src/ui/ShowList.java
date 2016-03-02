package ui;

import java.util.List;

import javax.swing.JFileChooser;

import bean.Task;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserFunction;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

/**
 * @author  Zhu Bingjing
 * @date 2016年3月2日 上午12:06:39 
 * @version 1.0 
 */
public class ShowList  extends Browser {
	BrowserView browserView;
	int index=1;

	public ShowList(final List<Task> taskList) {
		browserView = new BrowserView(this);
		
		this.loadURL(View.programPath + "\\src\\html\\list.html");

		this.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                DOMDocument document = event.getBrowser().getDocument();
                DOMNode tasklist_timed = document.findElement(By.id("tasklist_timed"));
                for(Task task:taskList){
                	DOMElement taskdiv = document.createElement("div");
                    taskdiv.setAttribute("class", "task");
                    taskdiv.setInnerHTML("<section class='task_number'>"
                    		+ index
                    		+"</section>"
                    		+ "<section class='task_middle'>"
                    		+ "<div class='task_components task_description'>"
                    		+ task.getTaskDescription()
                    		+"</div>"
                    		+ "<section class='task_components task_location'>"
                    		+ task.getTaskLocation()
                    		+"</section>"
                    		+ "<section class='task_components task_tags' id='task_tags'>"
                    		+ "</section>"
                    		+ "</section>"
                    		+ "<section class='task_right'>"
                    		+ "<div class='task_components time task_uppertime'>"
                    		+ task.getStartTime()
                    		+"</div>"
                    		+ " <div class='task_components time task_lowertime'>"
                    		+ task.getEndTime()
                    		+"</div>"
                    		+ "</section>");                   
                    
                    DOMNode task_tags=taskdiv.findElement(By.id("task_tags"));
                    for(String tag: task.getTaskTags()){
                    	DOMElement tagspan = document.createElement("span");
                    	tagspan.setInnerText(tag);
                    	task_tags.appendChild(tagspan);
                    }
                    tasklist_timed.appendChild(taskdiv);
                    index++;
                }                 
            }
        });
	}
}
