/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

/**
 *
 * @author Hugo
 */
public class DTOAbstract {
    private long id;
    private String name;

    public DTOAbstract(long id, String name){
        this.id=id;
        this.name=name;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
}
