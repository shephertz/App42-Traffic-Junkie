package app;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

public class AboutForm extends Form implements CommandListener{
	
	private Command cmdBack;
	private Display display;
	private Displayable prevDisplayable;
	private String aboutText="The App42 Cloud API's consists of a REST based service that has a JSON and XML interface. " +
			"There are around 18+ modules with over 300+ API's e.g. User, Session Management, Storage, Recommendation, Photo Gallery, Queue / Message, Game, Geo Spatial etc. " +
			"that support developers to develop their applications irrespective of the type of App they are developing. " +
			"SDK's are provided for all popular languages and platforms that enable easy integration of the API's into the App code." +
			"With just a few lines of code, the App developer gets access to services for developing simple to complex technical as well as business services." +
			"\n350+ APIs with 18+ Modules" +
			"\nMake Interactive Games with Unity3D" +
			"\nAll platform Support" +
			"\nMobile backend SDK for BlackBerry 10" +
			"\nMonoTouch & Monodroid" +
			"\nNOSQL Storage - Store your JSON App Data" +
			"\nPush Notification - Cross Platform Solution" +
			"\nCustom Code - Your Code , Our Cloud" ;

	
	public AboutForm(Display display,Displayable prevDisplayable) {
		super("Help");
		this.display = display;
		this.prevDisplayable = prevDisplayable;
		append(aboutText);
		cmdBack = new Command("Back", Command.BACK, 1);
		addCommand(cmdBack);
		setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d) {
		if ( c== cmdBack ){
			 display.setCurrent(prevDisplayable);
		} 
		
	}
}
