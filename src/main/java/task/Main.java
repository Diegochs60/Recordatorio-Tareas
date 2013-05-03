package task;

public class Main {
	public static void main(String... args){
		Thread t=null;
		if(args.length==1){
			long periodo=Long.parseLong(args[0]);
			t=new TriggerThread(periodo);
		}else{
			t=new TriggerThread();
		}
		t.start();
	}
}
