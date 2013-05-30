/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Hugo
 */
public class ToDoList extends BasecampAbstract{
    public JSONObject getTodoList(long projectId){
        try {
            return (data=XML.toJSONObject(getUrl("/projects/"+projectId+"/todo_lists.xml")));
        } catch (JSONException ex) {
            Logger.getLogger(ToDoList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
