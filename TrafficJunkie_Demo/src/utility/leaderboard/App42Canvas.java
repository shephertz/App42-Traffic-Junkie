package utility.leaderboard;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import utility.Constants;
import utility.StringDB;

public class App42Canvas extends Canvas{
	private Timer timer;
	public static String currentGameName = "TrafficJunkie";
	private Displayable prevDisplayable;
	private MIDlet midlet;
	private Display display;
	private boolean isSearch = false;
	public static int startY;
	public static int startX;
	public static int SCREEN_HEIGHT = 295;
	public static int SCREEN_WIDTH = 200;
	public int HEIGHT;
	public int WIDTH;
	private byte STATUS=-1;
	private int progressCounter=0;
	
	public static final byte STATUS_LAUNCH = 1;
	public static final byte STATUS_LEADER_BOARD = 2;
	public static final byte STATUS_POST_GAME_PLAY = 3;
	
	public FacebookLaunch facebookLaunch;
	public LeaderBoard leaderBoard;
	public PostGamePlay postGamePlay;
		
	public App42Canvas(MIDlet startMIDlet,Display disp){
		setFullScreenMode(true);
		this.midlet = startMIDlet;
		this.display = disp;
		this.WIDTH = getWidth();
		this.HEIGHT = getHeight();
		new ImageLoader();
		facebookLaunch = new FacebookLaunch(this, WIDTH, HEIGHT);
		postGamePlay = new PostGamePlay(this, WIDTH, HEIGHT);
		leaderBoard = new LeaderBoard(this, WIDTH, HEIGHT);
	}
	protected void paint(Graphics g) {
		g.setColor(0XFFFFFF);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(app.ImageLoader.gameBG, WIDTH/2, HEIGHT/2, Graphics.HCENTER|Graphics.VCENTER);
		g.drawImage(app.ImageLoader.trans, WIDTH/2, HEIGHT/2, Graphics.HCENTER|Graphics.VCENTER);
		if(STATUS == STATUS_LAUNCH){
			facebookLaunch.draw(g);
		}else if(STATUS == STATUS_LEADER_BOARD){
			leaderBoard.draw(g);
		}else if(STATUS == STATUS_POST_GAME_PLAY){
			postGamePlay.draw(g);
		}
		if(isSearch){
			g.setClip(0, 0, WIDTH, HEIGHT);
			g.setColor(0);
			g.fillRoundRect(50, HEIGHT/2-20, WIDTH-100, 40, 10, 10);
			g.setFont(StringDB.FONT_SMALL_ITALIC);
			g.setColor(0XFFFFFF);
			g.drawString(StringDB.fetching_data+StringDB.progress[progressCounter], WIDTH/2-StringDB.FONT_SMALL_ITALIC.stringWidth(StringDB.fetching_data)/2, HEIGHT/2-StringDB.FONT_SMALL.getHeight()/2, Graphics.TOP|Graphics.LEFT);
			progressCounter++;
			if(progressCounter>4){
				progressCounter=0;
			}
		}
	}
	public void launchApp42LeaderBoard(Displayable callerDisplayable,String gameName){
		App42ServiceHandler.instance().setMidlet(midlet);
		this.prevDisplayable=callerDisplayable;
		changeStatus(STATUS_LAUNCH);
		facebookLaunch.loadData();
		display.setCurrent(this);
		currentGameName = gameName;
	}
	
	public void submitScore(Displayable callerDisplayable, long score, String gameName){
		App42ServiceHandler.instance().setMidlet(midlet);
		prevDisplayable=callerDisplayable;
		currentGameName = gameName;
		postGamePlay.setData(score);
		postGamePlay.asyncLoadData();
		changeStatus(STATUS_POST_GAME_PLAY);
		display.setCurrent(this);
	}
	public void keyPressed(int keyCode){
		if(!isSearch){
			if(STATUS == STATUS_LAUNCH){
				facebookLaunch.keyPressed(keyCode);
			}else if(STATUS == STATUS_LEADER_BOARD){
				leaderBoard.keyPressed(keyCode);
			}else if(STATUS == STATUS_POST_GAME_PLAY){
				postGamePlay.keyPressed(keyCode);
			}
		}
		repaint();
	}
	public void pointerPressed(int x,int y){
		if(!isSearch){
			if(STATUS == STATUS_LAUNCH){
				facebookLaunch.pointerPressed(x, y);
			}else if(STATUS == STATUS_LEADER_BOARD){
				leaderBoard.pointerPressed(x, y);
			}else if(STATUS == STATUS_POST_GAME_PLAY){
				postGamePlay.pointerPressed(x, y);
			}
		}
		repaint();
	}
	public void pointerReleased(int x,int y){
		if(!isSearch){
			if(STATUS == STATUS_LAUNCH){
				facebookLaunch.pointerReleased(x, y);
			}else if(STATUS == STATUS_LEADER_BOARD){
				leaderBoard.pointerReleased(x, y);
			}else if(STATUS == STATUS_POST_GAME_PLAY){
				postGamePlay.pointerReleased(x, y);
			}
		}
		repaint();
	}
	public void pointerDragged(int x,int y){
		repaint();
	}
	public Display getDisplay(){
		return display;
	}
	protected void showNotify(){
		startTimer();
	}
	protected void hideNotify(){
		endTimer();
	}
	public void refresh(){
		repaint();
		serviceRepaints();
	}
	public void changeStatus(byte status){
		this.STATUS=status;
		refresh();
	}
	public void backToCallingDisplayable(){
		display.setCurrent(prevDisplayable);
	}
	private void startTimer(){
		if(timer==null){
			timer=new Timer();
			timer.schedule(new ProgressBarTask(this), 100, 100);
		}
	}
	private void endTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}
	public void showProgress(){
		isSearch=true;
		startTimer();
	}
	public void stopProgress(){
		isSearch=false;
		endTimer();
		refresh();
	}

}
class ProgressBarTask extends TimerTask{
	private App42Canvas app42Canvas;
	ProgressBarTask(App42Canvas app42Canvas){
		this.app42Canvas = app42Canvas;
	}
	public void run() {
		app42Canvas.repaint();
		app42Canvas.serviceRepaints();
	}
}
