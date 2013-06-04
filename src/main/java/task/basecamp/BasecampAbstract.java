/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;
import task.Parameters;

/**
 *
 * @author Hugo
 */
public abstract class BasecampAbstract {
    
    public static String basecampURL=null;
    protected JSONObject data;
    public static String username;
    public static String password;
    
    public BasecampAbstract(){
        if(basecampURL==null){
            basecampURL=Parameters.getInstance().getString("basecamp.urlbase");
        }
    }
    
    protected String getUrl(String comando){
        try {

            URL url=new URL(basecampURL+comando);
            Logger.getLogger(this.getClass().toString()).log(Level.FINE,basecampURL+comando);
            HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/xml");
            conn.setRequestProperty("Content-Type", "application/xml");
            String auth=new BASE64Encoder().encode((username+":"+password).getBytes());
            conn.setRequestProperty("Authorization", "Basic "+auth);
            
            StringBuffer sb=new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
                sb.append(inputLine);
            in.close();
            
            return sb.toString();
            
        } catch (Exception ex) {
            Logger.getLogger(BasecampAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected String postUrl(String comando, String data){
        try {

            URL url=new URL(basecampURL+comando);            
            Logger.getLogger(this.getClass().toString()).log(Level.FINE,basecampURL+comando);
            HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/xml");
            conn.setRequestProperty("Content-Type", "application/xml");
            String auth=new BASE64Encoder().encode((username+":"+password).getBytes());
            conn.setRequestProperty("Authorization", "Basic "+auth);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            //** post content
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(data);
            bw.flush();
            bw.close();
            
            if(conn.getResponseCode()==201){
                return "{\"status\":\"ok\"}";
            }
            //** get response
            StringBuffer sb=new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                    conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
                sb.append(inputLine);
            in.close();
            
            return sb.toString();
            
        } catch (Exception ex) {
            Logger.getLogger(BasecampAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * @return the properties
     */
    public JSONObject getData() {
        return data;
    }
}
