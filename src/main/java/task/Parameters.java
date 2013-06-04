package task;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Parameters {
	
	private ResourceBundle properties;
	
	private static Parameters instance;
	private Parameters(){
		properties=ResourceBundle.getBundle("config");		
	}
	public static Parameters getInstance(){
		if(instance==null){
			instance=new Parameters();
		}
		return instance;
	}
	public String getString(String key){
            try{
                return getInstance().properties.getString(key);
            }catch(MissingResourceException e){
                return null;
            }
	}
}
