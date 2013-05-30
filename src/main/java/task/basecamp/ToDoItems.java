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
public class ToDoItems extends BasecampAbstract{
    public JSONObject getTodoItems(long todoListId){
        try {
            return (data=XML.toJSONObject(getUrl("/todo_lists/"+todoListId+"/todo_items.xml")));
        } catch (JSONException ex) {
            Logger.getLogger(ToDoItems.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
