/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
public class Projects extends BasecampAbstract{
    public JSONObject getProjects(){
        try {
            return (data=XML.toJSONObject(getUrl("/projects.xml")));
        } catch (JSONException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public ProjectDTO[] getProjectList(){
        try {
            String onlyActive=Parameters.getInstance().getString("basecamp.project.onlyActive");
            
            JSONObject obj=(data=XML.toJSONObject(getUrl("/projects.xml")));
            JSONArray jarr=obj.getJSONObject("projects").getJSONArray("project");
            ArrayList<ProjectDTO> larr=new ArrayList<ProjectDTO>(){{add(new ProjectDTO(0,"**** Seleccione ****"));}};
            for(int i=0; i<jarr.length();i++){
                if(("true".equals(onlyActive) && jarr.getJSONObject(i).getString("status").equals("active"))
                        || !"true".equals(onlyActive)){
                    ProjectDTO p=new ProjectDTO(
                            jarr.getJSONObject(i).getJSONObject("id").getLong("content"),
                            jarr.getJSONObject(i).getString("name"));
                    larr.add(p);
                }
            }
            ProjectDTO[] parr=new ProjectDTO[larr.size()];
            parr=larr.toArray(parr);
            Arrays.sort(parr, new Comparator<ProjectDTO>(){
                public int compare(ProjectDTO o1, ProjectDTO o2) {
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
