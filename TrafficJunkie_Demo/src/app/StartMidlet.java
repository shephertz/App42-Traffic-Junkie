package app;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import utility.AdvManager;
import utility.AppInfo;

public class StartMidlet extends MIDlet{

	
	private Display display;
	private WelcomeCanvas welcomeCanvas;
	
	public StartMidlet() {
		display=Display.getDisplay(this);
		String appNAme=getAppProperty("Shephertz-Detail");
		System.out.println("appNAme: "+appNAme);
		initAppData();
	}
	private void initAppData(){
		AppInfo.APP_NAME = getAppProperty("MIDlet-Name");
		AppInfo.APP_VERSION = getAppProperty("MIDlet-Version");
		AppInfo.APP_VENDOR = getAppProperty("MIDlet-Vendor");
		AppInfo.APP_DETAIL = getAppProperty("Shephertz-Detail");
	}
	protected void startApp() throws MIDletStateChangeException {
		new ImageLoader();  // load all images
		welcomeCanvas = new WelcomeCanvas(this);
		display.setCurrent(welcomeCanvas);
	}
	protected void destroyApp(boolean b) throws MIDletStateChangeException {
		notifyDestroyed();
		AdvManager.instanse().endTimer();
	}

	protected void pauseApp() {
		
	}
	public Display getDisplay(){
		return display;
	}
}
