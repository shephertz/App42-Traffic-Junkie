package app;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class User {
	private Sprite userSprite;
	private int XX;
	private int YY;
	private int WIDTH;
	private int  HEIGHT;
	private Image spriteImage;
	private int spriteWidth;
	private int spriteHeight;
	private final int steps = 2 ;
	private int frameCounter;
	private int speedCounter=0;
	private int speedController=10;
	public User(int width,int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		spriteImage = ImageLoader.user1;
		spriteWidth = spriteImage.getWidth()/3;
		spriteHeight = spriteImage.getHeight()/4;
		XX = width/2;
		YY = height - ImageLoader.user1.getHeight()/4;
		userSprite = new Sprite(spriteImage,spriteWidth,spriteHeight);
	}
	public void init(){
		int num = Constant.getRandom(4);
		switch (num) {
		case 0:
			spriteImage = ImageLoader.user1;
		break;
		case 1:
			spriteImage = ImageLoader.user2;
		break;
		case 2:
			spriteImage = ImageLoader.user3;
		break;
		case 3:
			spriteImage = ImageLoader.user4;
		break;
		}
		userSprite = null;
		userSprite = new Sprite(spriteImage,spriteWidth,spriteHeight);
		XX = WIDTH/2;
		YY = HEIGHT - spriteHeight;
	}
	public void paint(Graphics g){
		userSprite.setPosition(XX, YY);
		userSprite.setFrame(frameCounter);
		userSprite.paint(g);
		
	}
	public Sprite getUserSprite(){
		return userSprite;
	}
	public void keyPressed(int keyCode){
		if(keyCode == GameCanvas.UP_PRESSED){
			move();
			if(frameCounter<3 || frameCounter>5){
				frameCounter = 3;
			}
			YY-=steps;
			if(YY<0){
				YY = 0;
			}
		}else if(keyCode == GameCanvas.DOWN_PRESSED){
			move();
			if(frameCounter>2){
				frameCounter = 0;
			}
			YY+=steps;
			if(YY>HEIGHT-spriteHeight){
				YY = HEIGHT-spriteHeight;
			}
		}else if(keyCode == GameCanvas.LEFT_PRESSED){
			move();
			if(frameCounter<6 || frameCounter>8){
				frameCounter = 6;
			}
			XX-=steps;
			if(XX<0){
				XX = 0;
			}
		}else if(keyCode == GameCanvas.RIGHT_PRESSED){
			move();
			if(frameCounter<8 || frameCounter>11){
				frameCounter = 9;
			}
			XX+=steps;
			if(XX>WIDTH-spriteWidth){
				XX = WIDTH-spriteWidth;
			}
		}
	}
	private void move(){
		speedCounter++;
		if(speedCounter%speedController ==0){
			frameCounter++;
		}
	}
	public int getX(){
		return XX;
	}
	public int getY(){
		return YY;
	}
}
