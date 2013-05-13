package app;

import javax.microedition.lcdui.Image;

public class ImageLoader {
	public static Image select;
	public static Image unselect;
	public static Image gameBG;
	public static Image trans;
	public static Image levelComplete;
	public static Image gameOver;
	// icons 
	public static Image icon_diamond;
	public static Image icon_heart;
	public static Image icon_user;
	
	// sprite
	public static Image user1;
	public static Image user2;
	public static Image user3;
	public static Image user4;
	public static Image diamond;
	public static Image roadSprite;
	public static Image vehicleSprite;
	public ImageLoader() {
		try{
			
			select = Image.createImage("/game/buttonS.png");
			unselect = Image.createImage("/game/button.png");
			levelComplete = Image.createImage("/game/levelcomplete.png");
			gameOver = Image.createImage("/game/gameover.png");
			// icons
			icon_diamond = Image.createImage("/game/diamond-icon.png");
			icon_heart = Image.createImage("/game/Heart-red-icon.png");
			icon_user = Image.createImage("/game/Users.png");
			// sprite
			user1 = Image.createImage("/sprite/BaurnMoosader.png");
			user2 = Image.createImage("/sprite/CoaMoosader.png");
			user3 = Image.createImage("/sprite/DelphineMoosader.png");
			user4 = Image.createImage("/sprite/ElliotMoosader.png");
			diamond = Image.createImage("/sprite/MulticolorGems.png");
			roadSprite = Image.createImage("/sprite/160x60_Sprite.png");
			vehicleSprite = Image.createImage("/sprite/Vehicles_Sprite.png");
			gameBG = Image.createImage("/game/320x240_Splash.jpg");
			trans = Image.createImage("/game/greenbox1.png");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
