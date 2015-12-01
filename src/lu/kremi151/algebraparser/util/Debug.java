package lu.kremi151.algebraparser.util;

public class Debug {

	private static boolean debugMode = false;
	
	public static void println(String line){
		if(debugMode)System.out.println("[algebraparser] " + line);
	}
	
	public static void setDebugMode(boolean v){
		debugMode = v;
	}
	
	public static boolean isInDebugMode(){
		return debugMode;
	}
}
