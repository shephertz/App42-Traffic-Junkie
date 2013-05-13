package app;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import utility.Constants;
import utility.StringDB;
import utility.Text;
import utility.TextList;


public class GameScreen extends GameCanvas {
	
	private Timer timer;
	private int WIDTH;
	private int  HEIGHT;
	private User user;
	private Road[] roads;
	private boolean isCrash = false;
	private int level=1;
	private int gameCounter=500;
	private int crashCounter=0;
	private final int upperLimit=30;
	private Sprite diamondSprite;
	private int rewXX,rewYY;
	private TiledLayer roadLayer;
	private int topGap;
	private int status;
	private final int STATUS_GAME = 1;
	private final int STATUS_HELP = 2;
	private final int STATUS_COMPLETE = 3;
	private final int STATUS_OVER = 4;
	private final int STATUS_PAUSE = 5;
	
	private boolean isPause = false;
	private TextList completeTextList;
	private TextList overTextList;
	
	private int life;
	private int userCounter=0;
	private int diamondCounter=0;
	private int score = 0;
	private boolean isExitAlert =  false;
	private MainMenuCanvas mainMenuCanvas;
	private Display display;
	private SoundManager soundManager;
	
	private boolean isLSK = false;
	private boolean isRSK = false;
	
	protected GameScreen(boolean suppressKeyEvents,MainMenuCanvas mainMenuCanvas,Display display) {
		super(suppressKeyEvents);
		super.setFullScreenMode(true);
		this.mainMenuCanvas = mainMenuCanvas;
		this.display = display;
		WIDTH = getWidth();
		HEIGHT = getHeight();
		System.out.println("WIDTH: "+WIDTH);
		System.out.println("HEIGHT: "+HEIGHT);
		user = new User(WIDTH,HEIGHT);
		diamondSprite = new Sprite(ImageLoader.diamond,ImageLoader.diamond.getWidth()/4,ImageLoader.diamond.getHeight()/3);
		diamondSprite.setVisible(false);
		initTiledLayer();
		initGame();
		status = STATUS_HELP;
		createCompleteMenu();
		createOverMenu();
	}
	public void initGame(){
		goToLevel(1);
		isExitAlert = false;
		status = STATUS_HELP;
	}
	private void initTiledLayer(){
		Image img = ImageLoader.roadSprite;
		topGap = ImageLoader.roadSprite.getHeight()*2;
		int rowTop=WIDTH/img.getHeight();
		if(WIDTH % img.getHeight()!=0){
			rowTop++;
		}
		int colTop=HEIGHT/img.getHeight();
		if(HEIGHT%img.getHeight()!=0){
			colTop++;
		}
		roadLayer=new TiledLayer(rowTop, colTop, img, img.getWidth()/5, img.getHeight());
		roadLayer.setPosition(0, 0);
		for(int i=0;i<colTop;i++){
			for(int j=0;j<rowTop;j++){
				if(i==0){
					if(j%2==0){
						roadLayer.setCell(j, i, 4);
					}else{
						roadLayer.setCell(j, i, 5);
					}
				}else if(i==colTop-1){
					roadLayer.setCell(j, i, 1);
				}else if(i % 2 ==0){
					roadLayer.setCell(j, i, 3);
				}else if(i % 2 !=0){
					roadLayer.setCell(j, i, 2);
				}
			}
		}
	}
	private void goToLevel(int level){
		life = 3;
		status = STATUS_GAME;
		userCounter = 0;
		diamondCounter = 0;
		score = 0;
		int totalArea = HEIGHT-2*topGap;
		if(level == 1){
			int gap = totalArea/2;
			roads = new Road[2];
			roads[0] = new Road(WIDTH, HEIGHT,0,HEIGHT/2,topGap,topGap+gap,Vehicle.LEFT);
			roads[1] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+gap,HEIGHT-topGap,Vehicle.RIGHT);
		}
		else if(level == 2){
			roads = new Road[3];
			int gap = totalArea/3;
			roads[0] = new Road(WIDTH, HEIGHT,0,HEIGHT/4,topGap,topGap+gap,Vehicle.RIGHT);
			roads[1] = new Road(WIDTH, HEIGHT,0,HEIGHT/2,topGap+gap,topGap+2*gap,Vehicle.LEFT);
			roads[2] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+2*gap,HEIGHT-topGap,Vehicle.RIGHT);
		}else if(level == 3){
			roads = new Road[4];
			int gap = totalArea/4;
			roads[0] = new Road(WIDTH, HEIGHT,0,HEIGHT/4,topGap,topGap+gap,Vehicle.RIGHT);
			roads[1] = new Road(WIDTH, HEIGHT,0,HEIGHT/2,topGap+gap,topGap+2*gap,Vehicle.LEFT);
			roads[2] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+2*gap,topGap+3*gap,Vehicle.RIGHT);
			roads[3] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+3*gap,HEIGHT-topGap,Vehicle.LEFT);
		}else if(level == 4){
			roads = new Road[4];
			int gap = totalArea/4;
			roads[0] = new Road(WIDTH, HEIGHT,0,HEIGHT/4,topGap,topGap+gap,Vehicle.RIGHT);
			roads[1] = new Road(WIDTH, HEIGHT,0,HEIGHT/2,topGap+gap,topGap+2*gap,Vehicle.LEFT);
			roads[2] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+2*gap,topGap+3*gap,Vehicle.RIGHT);
			roads[3] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+3*gap,HEIGHT-topGap,Vehicle.LEFT);
		}else if(level == 5){
			roads = new Road[5];
			int gap = totalArea/5;
			roads[0] = new Road(WIDTH, HEIGHT,0,HEIGHT/4,topGap,topGap+gap,Vehicle.RIGHT);
			roads[1] = new Road(WIDTH, HEIGHT,0,HEIGHT/2,topGap+gap,topGap+2*gap,Vehicle.LEFT);
			roads[2] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+2*gap,topGap+3*gap,Vehicle.RIGHT);
			roads[3] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+3*gap,topGap+4*gap,Vehicle.LEFT);
			roads[4] = new Road(WIDTH, HEIGHT,0,(HEIGHT*3)/4,topGap+4*gap,HEIGHT-topGap,Vehicle.LEFT);
		}
	}
	private void createCompleteMenu(){
		int noOfElement = 4;
		int gap = 30 ;
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		if(completeTextList == null){
			completeTextList = new TextList();
		}
		completeTextList.addElement(-2, "1", new Text("",0XFFFFFF,0XFFFFFF,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		completeTextList.addElement(0, "submit", new Text("Submit Score",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		temp += gap;
		completeTextList.addElement(1, "mainmenu", new Text("Main Menu",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		temp += gap;
		completeTextList.addElement(2, "next", new Text("Next Level",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		completeTextList.createList();
		completeTextList.setBgImage("1", ImageLoader.levelComplete, ImageLoader.levelComplete);
	}
	private void createOverMenu(){
		int noOfElement = 3;
		int gap = 30 ;
		if(overTextList == null){
			overTextList = new TextList();
		}
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		overTextList.addElement(-2, "1", new Text("",0XFFFFFF,0XFFFFFF,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp += gap;
		overTextList.addElement(0, "submit", new Text("Submit Score",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		temp += gap;
		overTextList.addElement(1, "playagain", new Text("Play Again?",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		temp += gap;
		overTextList.addElement(2, "mainmenu", new Text("Main Menu",0XFFFFFF,0XFFFFFF,0X6E6E6E,0X1C1C1C,StringDB.FONT_SMALL,WIDTH/2,temp,true));
		overTextList.createList();
		overTextList.setBgImage("1", ImageLoader.gameOver, ImageLoader.gameOver);
	}
	public void update(){
		Graphics g = getGraphics();
		g.setColor(0XFFFFFF);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		drawBackground(g);
		drawHeader(g);
		drawRoad(g);
		if(status == STATUS_GAME || status == STATUS_PAUSE){
			user.paint(g);
			diamondSprite.setPosition(rewXX, rewYY);
			diamondSprite.paint(g);
			checkUserLocation();
			checkCollision();
			checkUserCrash();
			checkForComplete();
			drawFooter(g,"","");
		}else if(status == STATUS_COMPLETE){
			g.drawImage(ImageLoader.trans, WIDTH/2, HEIGHT/2, Graphics.HCENTER|Graphics.VCENTER);
			completeTextList.draw(g);
		}else if(status == STATUS_OVER){
			g.drawImage(ImageLoader.trans, WIDTH/2, HEIGHT/2, Graphics.HCENTER|Graphics.VCENTER);
			overTextList.draw(g);
		} else if(status == STATUS_HELP){
			g.drawImage(ImageLoader.trans, WIDTH/2, HEIGHT/2, Graphics.HCENTER|Graphics.VCENTER);
			String[] alertHelp =null;
			if(!isExitAlert){
				alertHelp = Constant.alertHelp;
				drawFooter(g,"Ok","");
			}else{
				alertHelp = Constant.alertExit;
				drawFooter(g,"Yes","No");
			}
			g.setColor(0XFFFFFF);
			g.setFont(StringDB.FONT_SMALL);
			g.drawString(alertHelp[0], WIDTH/2, HEIGHT/2-StringDB.FONT_SMALL.getHeight()*2, Graphics.TOP | Graphics.HCENTER);
			g.drawString(alertHelp[1], WIDTH/2, HEIGHT/2-StringDB.FONT_SMALL.getHeight(), Graphics.TOP | Graphics.HCENTER);
			g.drawString(alertHelp[2], WIDTH/2, HEIGHT/2+StringDB.FONT_SMALL.getHeight(), Graphics.TOP | Graphics.HCENTER);
			g.drawString(alertHelp[3], WIDTH/2, HEIGHT/2+StringDB.FONT_SMALL.getHeight()*2, Graphics.TOP | Graphics.HCENTER);
		}
		flushGraphics();
	}
	private void drawBackground(Graphics g){
		roadLayer.paint(g);
	}
	private void drawHeader(Graphics g){
		g.setFont(StringDB.FONT_SMALL_BOLD);
		g.setColor(0X000000);
		for(int i=0;i<life;i++){
			g.drawImage(ImageLoader.icon_heart, 0+(i*ImageLoader.icon_diamond.getWidth()), 0, Graphics.TOP|Graphics.LEFT);
		}
		g.drawImage(ImageLoader.icon_user, WIDTH/2, 0, Graphics.TOP|Graphics.RIGHT);
		g.drawString(" "+userCounter,WIDTH/2,ImageLoader.icon_user.getHeight()/2-StringDB.FONT_SMALL_BOLD.getHeight()/2,Graphics.TOP|Graphics.LEFT);
		g.drawImage(ImageLoader.icon_diamond, WIDTH-StringDB.FONT_SMALL_BOLD.stringWidth(" "+diamondCounter), 0, Graphics.TOP|Graphics.RIGHT);
		g.drawString(" "+diamondCounter,WIDTH,ImageLoader.icon_diamond.getHeight()/2-StringDB.FONT_SMALL_BOLD.getHeight()/2,Graphics.TOP|Graphics.RIGHT);
		g.drawString("Score: "+score, 0, StringDB.FONT_SMALL_BOLD.getHeight(), Graphics.TOP|Graphics.LEFT );
		g.drawString("Level: "+level, WIDTH, StringDB.FONT_SMALL_BOLD.getHeight(), Graphics.TOP|Graphics.RIGHT );
	}
	private void drawFooter(Graphics g,String lsk,String rsk){
		g.setFont(StringDB.FONT_SMALL_BOLD);
		g.setColor(0X000000);
		if(status == STATUS_GAME || status == STATUS_PAUSE){
			if(isPause){
				g.drawString("Play",0,HEIGHT,Graphics.BOTTOM | Graphics.LEFT);
			}else{
				g.drawString("Pause",0,HEIGHT,Graphics.BOTTOM | Graphics.LEFT);
			}
			g.drawString("Back",WIDTH,HEIGHT,Graphics.BOTTOM|Graphics.RIGHT);
		}else if(status == STATUS_HELP){
			if(lsk.length()>0){
				g.drawString(lsk,0,HEIGHT,Graphics.BOTTOM | Graphics.LEFT);
			}
			if(rsk.length()>0){
				g.drawString(rsk,WIDTH,HEIGHT,Graphics.BOTTOM | Graphics.RIGHT);
			}
		}
	}
	private void drawRoad(Graphics g){
		for(int i=0;i<roads.length;i++){
			roads[i].paint(g);
		}
	}
	public void checkCollision(){
		for(int i=0;i<roads.length;i++){
			for(int j=0;j<roads[i].getVehicles().length;j++){
				if(roads[i].getVehicles()[j].getSprite()!=null && user.getUserSprite().collidesWith(roads[i].getVehicles()[j].getSprite(), true)){
					if(isCrash==false){
						playSound((byte)1);
					}
					isCrash=true;
					break;
				}
			}
		}
		if(user.getUserSprite().collidesWith(diamondSprite, true)){
			diamondCounter++;
			diamondSprite.setVisible(false);
			updateScore(Constant.DIAMOND_POINTS);
		}
	}
	private void startRoad(){
		for(int i=0;i<roads.length;i++){
			roads[i].start();
		}
	}
	private void stopRoad(){
		for(int i=0;i<roads.length;i++){
			roads[i].stop();
		}
	}
	private void checkUserLocation(){
		if(user.getY()<=upperLimit){
			userCounter++;
			updateScore(Constant.USER_POINTS);
			user.init();
			playSound((byte)2);
		}
	}
	public void updateScore(int update){
		this.score = this.score + update;
	}
	private void checkUserCrash(){
		if(isCrash){
			isCrash = true;
			crashCounter++;
			user.getUserSprite().setFrame(2);
			user.getUserSprite().setTransform(Sprite.TRANS_ROT90);
			stopRoad();
			if(crashCounter >= 100 ){
				life--;
				user.getUserSprite().setTransform(Sprite.TRANS_NONE);
				user.getUserSprite().setFrame(0);
				isCrash = false;
				crashCounter = 0;
				startRoad();
				user.init();
			}
		}
	}
	private void checkForComplete(){
		if(life<=0){
			doGameOver();
			return ;
		}
		if(userCounter == Constant.MAX_USER){
			switch (level) {
			case 1:
				if(diamondCounter == Constant.DIAMOND_LEVEL1){
					doLevelComplete();
				}else{
					doGameOver();
				}
			break;
			case 2:
				if(diamondCounter == Constant.DIAMOND_LEVEL2){
					doLevelComplete();
				}else{
					doGameOver();
				}
			break;
			case 3:
				if(diamondCounter == Constant.DIAMOND_LEVEL3){
					doLevelComplete();
				}else{
					doGameOver();
				}
			break;
			case 4:
				if(diamondCounter == Constant.DIAMOND_LEVEL4){
					doLevelComplete();
				}else{
					doGameOver();
				}
			break;
			case 5:
				if(diamondCounter == Constant.DIAMOND_LEVEL5){
					doLevelComplete();
				}else{
					doGameOver();
				}
			break;
			}
		}
	}
	private void doLevelComplete(){
		status = STATUS_COMPLETE;
	}
	private void doGameOver(){
		status = STATUS_OVER;
	}
	private boolean isKeyFlag = false;
	public void keyPressed(){
		int keyCode = getKeyStates();
		if(keyCode == 0){
			isKeyFlag = false;
		}
		if(status == STATUS_GAME && !isCrash && !isPause){
			user.keyPressed(keyCode);
		}
		else if(status == STATUS_COMPLETE){
			if(!isKeyFlag){
				completeTextList.keyPressed(keyCode);
				if(keyCode == Constants.OK_KEY || keyCode == Constants.FIVE_KEY || keyCode == GameCanvas.FIRE_PRESSED){
					handleLevelComplete();
				}
				isKeyFlag = true;
			}
		}else if(status == STATUS_OVER){
			if(!isKeyFlag){
				overTextList.keyPressed(keyCode);
				if(keyCode == Constants.OK_KEY || keyCode == Constants.FIVE_KEY || keyCode == GameCanvas.FIRE_PRESSED){
					handleGameOver();
				}
				isKeyFlag = true;
			}
		}
	}
	public void keyPressed(int keyCode){
		System.out.println("KeyCode: "+keyCode);
		if(keyCode == 0 ){
			keyCode = getKeyStates();
		}
		if(status == STATUS_GAME || status == STATUS_PAUSE){
			if(keyCode == Constants.LSK_KEY){
				if(isPause){
					isPause = false;
					status = STATUS_GAME;
					startRoad();
				}else if(!isPause){
					isPause = true;
					status = STATUS_PAUSE;
					stopRoad();
				}
			}else if(keyCode == Constants.RSK_KEY){
				if(score>0){
					stopRoad();
					isExitAlert = true;
					status = STATUS_HELP;
				}else{
					gotoMainMenu();
				}
				
			}
		}else if(status == STATUS_COMPLETE){
			completeTextList.keyPressed(keyCode);
			if(keyCode == Constants.OK_KEY || keyCode == Constants.FIVE_KEY){
				handleLevelComplete();
			}
		}else if(status == STATUS_OVER){
			overTextList.keyPressed(keyCode);
			if(keyCode == Constants.OK_KEY || keyCode == Constants.FIVE_KEY){
				handleGameOver();
			}
		}else if(status == STATUS_HELP){
			if(!isExitAlert){
				if(keyCode == Constants.OK_KEY || keyCode == Constants.LSK_KEY || keyCode==Constants.FIVE_KEY){
					status = STATUS_GAME;
					goToLevel(1);
				}
			}else{
				if(keyCode == Constants.LSK_KEY ){
					gotoMainMenu();
				}else if(keyCode == Constants.RSK_KEY ){
					isExitAlert = false;
					status = STATUS_PAUSE;
					isPause = true;
				}
			}
		}
	}
	public void keyReleased(int keyCode){
		isKeyFlag = false;
	}
	public void pointerPressed(int x,int y){
		if(status == STATUS_GAME || status == STATUS_PAUSE || status == STATUS_HELP){
			if(x>0 && x<WIDTH/2 && y>HEIGHT-40 && y<HEIGHT){
				isLSK = true;
				isRSK = false;
			}else if(x>WIDTH/2 && x<WIDTH && y>HEIGHT-40 && y<HEIGHT){
				isRSK = true;
				isLSK = false;
			}
		}else if(status == STATUS_COMPLETE){
			completeTextList.pointerPressed(x, y);
			
		}else if(status == STATUS_OVER){
			overTextList.pointerPressed(x, y);
			
		}
	}
	public void pointerReleased(int x,int y){
		if(status == STATUS_GAME || status == STATUS_PAUSE){
			if(isLSK){
				isLSK = false;
				if(isPause){
					isPause = false;
					status = STATUS_GAME;
					startRoad();
				}else if(!isPause){
					isPause = true;
					status = STATUS_PAUSE;
					stopRoad();
				}
			}else if(isRSK){
				isRSK = false;
				gotoMainMenu();
			}
		}else if(status == STATUS_COMPLETE){
			handleLevelComplete();
		}else if(status == STATUS_OVER){
			handleGameOver();
		}else if(status == STATUS_HELP){
			if(!isExitAlert){
				status = STATUS_GAME;
				goToLevel(1);
			}else{
				if(isLSK){
					gotoMainMenu();
				}else if(isRSK){
					isExitAlert = false;
					status = STATUS_PAUSE;
				}
			}
		}
		isLSK = false;
		isRSK = false;
	}
	
	
	public void generateDiamond(){
		if(!isCrash && !isPause){
			gameCounter++;
		}
		if(diamondSprite.isVisible()==false && gameCounter%600==0){
			diamondSprite.setVisible(true);
			diamondSprite.setFrame(Constant.getRandom(12));
			rewXX = Constant.getRandom(WIDTH-diamondSprite.getWidth());
			rewYY = Constant.getRandom(HEIGHT-topGap);
			if(rewYY<topGap){
				rewYY = topGap + rewYY/2;
			}
			gameCounter = 0;
		}else if(diamondSprite.isVisible() && gameCounter%350==0){
			gameCounter = 0;
			diamondSprite.setVisible(false);
		}
	}
	private void handleLevelComplete(){
		String str = completeTextList.getSelected();
		if (str.equals("submit")){
			submitScore();
		}else if (str.equals("mainmenu")){
			gotoMainMenu();
		}else if (str.equals("next")){
			if(level < 5){
				level++;
				gotoNextLevel(level);
			}
		}
	}
	private void handleGameOver(){
		String str = overTextList.getSelected();
		if (str.equals("submit")){
			submitScore();
		}else if (str.equals("playagain")){
			goToLevel(level);
		}else if (str.equals("mainmenu")){
			gotoMainMenu();
		}
	}
	private void submitScore(){
		if(score>0){
			mainMenuCanvas.submitScore(score);
		}
	}
	private void gotoMainMenu(){
		score = 0;
		level = 1;
		if( soundManager!=null ){
			soundManager.deallocateSound();
		}
		display.setCurrent(mainMenuCanvas);
	}
	private void gotoNextLevel(int level){
		goToLevel(level);
	}
	private void startTimer(){
		if(timer==null){
			timer=new Timer();
			timer.schedule(new ProgressBarTask(this), 100, 10 );
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
	private void playSound(byte TYPE){
		if( soundManager==null ){
			soundManager = new SoundManager();
		}
		if(MainMenuCanvas.isSound){
			soundManager.playSound(TYPE);
		}
	}
	
}
class ProgressBarTask extends TimerTask{
	private GameScreen gameScreen;
	ProgressBarTask(GameScreen gameScreen){
		this.gameScreen=gameScreen;
	}
	public void run() {
		gameScreen.generateDiamond();
		gameScreen.keyPressed();
		gameScreen.update();
	}
}
