/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task;

import java.util.logging.Logger;
import org.json.JSONObject;
import task.basecamp.Account;
import task.basecamp.BasecampAbstract;
import org.junit.*;
import static org.junit.Assert.*;
import task.basecamp.ProjectDTO;
import task.basecamp.Projects;
import task.basecamp.ToDoItems;
import task.basecamp.ToDoList;
/**
 *
 * @author Hugo
 */
public class BasecampTest {
    @Before
    public void setup(){
      BasecampAbstract.username="usuario";
      BasecampAbstract.password="password";  
    }
    @Test
    public void testAccount() throws Exception{        
        Account acc=new Account();
        JSONObject resp=acc.getAccountInfo();
        
        assertNotNull(resp);
        assertNotNull(resp.get("person"));
        
        System.out.println(resp.toString(4));
        
    }
    
    @Test
    public void testProjects() throws Exception{ 
        Projects prj=new Projects();
        JSONObject resp=prj.getProjects();
        
        assertNotNull(resp);
        assertTrue(resp.getJSONObject("projects").getJSONArray("project").length()>0);
        
        System.out.println(resp.toString(4));        
    }
    
    @Test
    public void testProjectsList() throws Exception{ 
        Projects prj=new Projects();
        ProjectDTO[] resp=prj.getProjectList();
        
        assertNotNull(resp);
        
        System.out.println("---------------- testProjectsList"); 
        System.out.println(resp.length);        
    }
    @Test
    public void testTodoList() throws Exception{ 
        ToDoList todol=new ToDoList();
        JSONObject resp=todol.getTodoList(10813383);
        
        assertNotNull(resp);
        assertTrue(resp.getJSONObject("todo-lists").getJSONArray("todo-list").length()>0);
        
        System.out.println(resp.toString(4));        
    }
    
    
    @Test
    public void testTodoItem() throws Exception{ 
        ToDoItems todo=new ToDoItems();
        JSONObject resp=todo.getTodoItems(23457322);
        
        assertNotNull(resp);
        assertTrue(resp.getJSONObject("todo-items").getJSONArray("todo-item").length()>0);
        
        System.out.println(resp.toString(4));        
    }
}
