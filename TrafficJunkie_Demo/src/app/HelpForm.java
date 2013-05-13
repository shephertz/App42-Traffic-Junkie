package app;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

public class HelpForm extends Form implements CommandListener{
	
	private Command cmdBack;
	private Display display;
	private Displayable prevDisplayable;
	private String helpText="" +
			"1. Help Jucnky to cross road.\n" +
			"2. Every level require 10 user to reach their home.\n" +
			"3. Collect diamond to complete level.\n" +
			"Diamond Required" +
			"\n1. Level 1: 10" +
			"\n2. Level 2: 15" +
			"\n3. Level 3: 20" +
			"\n4. Level 4: 25" +
			"\n5. Level 5: 30" +
			"\nControls:" +
			"\nkeys 4 and 6 make you run sideways." +
			"\nuse 2 and 4 key to run vertically";
	
	public HelpForm(Display display,Displayable prevDisplayable) {
		super("Help");
		this.display = display;
		this.prevDisplayable = prevDisplayable;
		append(helpText);
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
