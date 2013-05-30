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
public class Projects extends BasecampAbstract{
    public JSONObject getProjects(){
        try {
            return (data=XML.toJSONObject(getUrl("/projects.xml")));
        } catch (JSONException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
