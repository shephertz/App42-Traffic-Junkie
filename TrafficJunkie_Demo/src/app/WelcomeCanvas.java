package app;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import utility.StringDB;

public class WelcomeCanvas extends Canvas{
	private Timer timer;
	private int counter=0;
	private StartMidlet midlet;
	private MainMenuCanvas mainMenuCanvas;
	public WelcomeCanvas(StartMidlet midlet) {
		setFullScreenMode(true);
		this.midlet = midlet;
		mainMenuCanvas = new MainMenuCanvas(midlet);
	}
	protected void paint(Graphics g) {
		g.drawImage(ImageLoader.gameBG, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);
		g.setColor(0X000000);
//		g.setFont(StringDB.FONT_MEDIUM_BOLD);
//		g.drawString("Shephertz Present...",getWidth()/2,getHeight()/2,Graphics.HCENTER|Graphics.TOP);
		counter++;
		if(counter>4){
			goToNextScreen();
		}
	}
	private void goToNextScreen(){
		midlet.getDisplay().setCurrent(mainMenuCanvas);
	}
	private void startTimer(){
		if(timer==null){
			timer=new Timer();
			timer.schedule(new WelcomeTask(this), 100, 1000);
		}
	}
	private void endTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}
	public void showNotify(){
		startTimer();
	}
	public void hideNotify(){
		endTimer();
	}
}
class WelcomeTask extends TimerTask{
	private WelcomeCanvas welcomeCanvas;
	WelcomeTask(WelcomeCanvas welcomeCanvas){
		this.welcomeCanvas=welcomeCanvas;
	}
	public void run() {
		welcomeCanvas.repaint();
		welcomeCanvas.serviceRepaints();
	}
}
