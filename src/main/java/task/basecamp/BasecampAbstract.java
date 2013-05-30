/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

/**
 *
 * @author Hugo
 */
public abstract class BasecampAbstract {
    
    public static final String basecampURL="https://jwmpro.basecamphq.com";
    protected JSONObject data;
    public static String username;
    public static String password;
    
    protected String getUrl(String comando){
        try {

            URL url=new URL(basecampURL+comando);
            
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
        return null;
    }
    /**
     * @return the properties
     */
    public JSONObject getData() {
        return data;
    }
}
