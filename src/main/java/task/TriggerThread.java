package task;

import java.awt.TrayIcon;
import java.util.Date;
	
public class TriggerThread extends Thread {
	public  final long STEP_TIME; 
	private long timestamp=new Date().getTime();
	private static boolean exit=false;
	private static boolean isDisplay;
	
	public TriggerThread(){
		STEP_TIME=3600000l; //60m x 60s x 1000ms;
	}
	public TriggerThread(long step){
		STEP_TIME=step;
	}
	public static void setExit(boolean exitState){
		exit=exitState;
	}
	public synchronized void run(){
		InputFrame frame=new InputFrame();
                
		while(!exit){
			if(new Date().getTime()> timestamp+STEP_TIME && !isDisplay){
				System.out.println("Solicitando información de la tarea");
				frame.setVisible(true);
				timestamp=new Date().getTime();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// continue...
			}
		}
		frame.dispose();
		System.out.println("Ejecución terminada");
                System.exit(0);
	}
	public static boolean isDisplay() {
		return isDisplay;
	}
	public static void setDisplay(boolean isDisplay) {
		TriggerThread.isDisplay = isDisplay;
	}
	
	
}
