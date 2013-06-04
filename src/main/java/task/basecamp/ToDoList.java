/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import task.Parameters;

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
    public TodoListDTO[] getTodoListArray(long projectId){
        try {
            
            JSONObject obj=(data=XML.toJSONObject(getUrl("/projects/"+projectId+"/todo_lists.xml")));
            JSONArray jarr=obj.getJSONObject("todo-lists").getJSONArray("todo-list");
            ArrayList<TodoListDTO> larr=new ArrayList<TodoListDTO>(){{add(new TodoListDTO(0,"**** Seleccione ****"));}};
            for(int i=0; i<jarr.length();i++){
                TodoListDTO p=new TodoListDTO(
                        jarr.getJSONObject(i).getJSONObject("id").getLong("content"),
                        jarr.getJSONObject(i).getString("name"));
                larr.add(p);                
            }
            TodoListDTO[] parr=new TodoListDTO[larr.size()];
            parr=larr.toArray(parr);
            Arrays.sort(parr, new Comparator<TodoListDTO>(){
                public int compare(TodoListDTO o1, TodoListDTO o2) {
                    return o1.getName().compareTo(o2.getName());
                }            
            });
            return parr;
        } catch (JSONException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
