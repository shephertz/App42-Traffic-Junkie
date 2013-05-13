package app;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import utility.AdvManager;
import utility.Constants;
import utility.StringDB;
import utility.Text;
import utility.TextList;
import utility.leaderboard.App42Canvas;


public class MainMenuCanvas extends Canvas{
	
	private String[] menuItems={"Start","Sound Off","Help","Score","About","Exit"};
	private TextList menuTextList;
	private int WIDTH;
	private int HEIGHT;
	private StartMidlet midlet;
	public GameScreen gameScreen;
	private HelpForm helpForm;
	private AboutForm about;
	private App42Canvas app42Canvas;
	public static boolean isSound = true;
	public MainMenuCanvas(StartMidlet midlet) {
		setFullScreenMode(true);
		this.midlet = midlet;
		menuTextList = new TextList();
		this.WIDTH = getWidth();
		this.HEIGHT = getHeight();
		helpForm = new HelpForm(midlet.getDisplay(), this);
		about = new AboutForm(midlet.getDisplay(), this);
		gameScreen = new GameScreen(true,this,midlet.getDisplay());
		app42Canvas = new App42Canvas(midlet,midlet.getDisplay());
		createMainMenu();
		AdvManager.instanse().asyncInitSetup(midlet);
		AdvManager.instanse().setCoordinate(WIDTH, HEIGHT, 10, HEIGHT-10,this);
		
	}
	private void createMainMenu(){
		int noOfElement = 6;
		int gap = ImageLoader.select.getHeight()+2;
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		menuTextList.addElement(0, "1", new Text(menuItems[0],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		menuTextList.addElement(1, "2", new Text(menuItems[1],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		menuTextList.addElement(2, "3", new Text(menuItems[2],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		menuTextList.addElement(3, "4", new Text(menuItems[3],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		menuTextList.addElement(4, "5", new Text(menuItems[4],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		menuTextList.addElement(5, "6", new Text(menuItems[5],0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		menuTextList.createList();
		menuTextList.setBgImage("1", ImageLoader.select, ImageLoader.unselect);
		menuTextList.setBgImage("2", ImageLoader.select, ImageLoader.unselect);
		menuTextList.setBgImage("3", ImageLoader.select, ImageLoader.unselect);
		menuTextList.setBgImage("4", ImageLoader.select, ImageLoader.unselect);
		menuTextList.setBgImage("5", ImageLoader.select, ImageLoader.unselect);
		menuTextList.setBgImage("6", ImageLoader.select, ImageLoader.unselect);
	}
	protected void paint(Graphics g) {
		g.setColor(0XFFFFFF);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(ImageLoader.gameBG, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);
		g.drawImage(ImageLoader.trans, getWidth()/2, getHeight()/2, Graphics.HCENTER|Graphics.VCENTER);
		menuTextList.draw(g);
		AdvManager.instanse().drawAdv(g, AdvManager.TOP_ACTIVE);
	}
	public void keyPressed(int keyCode){
		menuTextList.keyPressed(keyCode);
		if(keyCode == Constants.FIVE_KEY || keyCode == Constants.OK_KEY){
			handleOk(Integer.parseInt(menuTextList.getSelected()));
		}
		repaint();
	}
	public void pointerPressed(int x, int y){
		menuTextList.pointerPressed(x, y);
		repaint();
	}
	public void pointerReleased(int x, int y){
		if(menuTextList.getSelected().length()>0){
			handleOk(Integer.parseInt(menuTextList.getSelected()));
		}
		AdvManager.instanse().pointerReleased(x, y,AdvManager.TOP_ACTIVE);
		menuTextList.pointerReleased();
		repaint();
	}
	public void pointerDragged(int x, int y){
		
		repaint();
	}
	public void submitScore(int score){
		app42Canvas.submitScore(this, score, "TrafficJunkie");
	}
	private void handleOk(int index){
		switch (index) {
		case 1:
			gameScreen.initGame();
			midlet.getDisplay().setCurrent(gameScreen);
		break;
		case 2:
			if(isSound){
				isSound = false;
				menuTextList.setText("2", "Sound On");
			}else{
				isSound = true;
				menuTextList.setText("2", "Sound Off");
			}
		break;
		case 3:
			midlet.getDisplay().setCurrent(helpForm);
		break;
		case 4:
			app42Canvas.launchApp42LeaderBoard(this, "TrafficJunkie");
		break;
		case 5:
			midlet.getDisplay().setCurrent(about);
		break;
		case 6:
			try{
				midlet.destroyApp(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		break;
		}
	}
}

