/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
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
    public TodoItemDTO[] getTodoItemsArray(long todoListId){
        try {
            
            JSONObject obj=(data=XML.toJSONObject(getUrl("/todo_lists/"+todoListId+"/todo_items.xml")));
            JSONArray jarr=obj.getJSONObject("todo-items").getJSONArray("todo-item");
            ArrayList<TodoItemDTO> larr=new ArrayList<TodoItemDTO>(){{add(new TodoItemDTO(0,"**** Seleccione ****"));}};
            for(int i=0; i<jarr.length();i++){
                TodoItemDTO p=new TodoItemDTO(
                        jarr.getJSONObject(i).getJSONObject("id").getLong("content"),
                        jarr.getJSONObject(i).getString("content"));
                larr.add(p);                
            }
            TodoItemDTO[] parr=new TodoItemDTO[larr.size()];
            parr=larr.toArray(parr);
            Arrays.sort(parr, new Comparator<TodoItemDTO>(){
                public int compare(TodoItemDTO o1, TodoItemDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }            
            });
            return parr;
        } catch (JSONException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean newTimeEntry(long todoItemId, TimeEntryDTO entry){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String xml="<time-entry>"
                    + "     <person-id>"+entry.getPersonId()+"</person-id>"
                    + "     <date>"+ sdf.format(entry.getDate()) +"</date>"
                    + "     <hours>"+entry.getHours()+"</hours>"
                    + "     <description>"+entry.getDescription()+"</description>"
                    + "</time-entry>";
            JSONObject resp=XML.toJSONObject(postUrl("/todo_items/"+todoItemId+"/time_entries.xml",xml));
            
            if(resp.has("status") && "ok".equals(resp.getString("status"))){
                return true;
            }                
        } catch (JSONException ex) {
            Logger.getLogger(ToDoItems.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
