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
public class Account extends BasecampAbstract{
    private JSONObject projects;
    public JSONObject getAccountInfo(){
        try {
            return (data=XML.toJSONObject(getUrl("/me.xml")));
        } catch (JSONException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void setProjects(JSONObject projects){
        this.projects=projects;
    }
}
