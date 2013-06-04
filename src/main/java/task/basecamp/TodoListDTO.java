/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

/**
 *
 * @author Hugo
 */
public class TodoListDTO extends DTOAbstract{
    public TodoListDTO(long id, String name){
        super(id,name);
    }
    @Override
    public String toString(){
        return getName();
    }
}
