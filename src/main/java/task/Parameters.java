package task;

import java.util.ResourceBundle;

public class Parameters {
	
	private ResourceBundle properties;
	
	private static Parameters instance;
	private Parameters(){
		properties=ResourceBundle.getBundle("parameters");		
	}
	public static Parameters getInstance(){
		if(instance==null){
			instance=new Parameters();
		}
		return instance;
	}
	public static String getString(String key){
		return getInstance().properties.getString(key);
	}
}
