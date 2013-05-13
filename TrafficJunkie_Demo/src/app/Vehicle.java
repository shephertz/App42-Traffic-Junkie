package app;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Vehicle {
	private Sprite vehicleSprite;
	private int x;
	private int y;
	private int width;
	private int height;
	private byte dir;
	private boolean isFinish;
	private int steps=1;
	private byte counter=0;
	public static final byte LEFT = 1;
	public static final byte RIGHT = 2;
	private int upperY;
	private int lowerY;
	private boolean isMoving = true;
	
	public Vehicle(int initX,int initY,int uppery,int lowery){
		this.width = Constant.WIDTH;
		this.height = Constant.HEIGHT;
		this.x = initX;
		this.y = initY;
		this.upperY = uppery;
		this.lowerY = lowery;
	}
	public void reset(int frame,byte dir){
		if(vehicleSprite == null){
			vehicleSprite = new Sprite(ImageLoader.vehicleSprite,ImageLoader.vehicleSprite.getWidth()/15,ImageLoader.vehicleSprite.getHeight());
			vehicleSprite.setFrame(frame);
		}else{
			vehicleSprite.setFrame(frame);
		}
		int diff = lowerY - upperY;
		int loc = Constant.getRandom(diff);
		y = upperY+loc;
		this.dir = dir;
		if(dir==Vehicle.LEFT){
			x=width+vehicleSprite.getWidth()+5;
			vehicleSprite.setTransform(Sprite.TRANS_MIRROR);
		}
		else if(dir == Vehicle.RIGHT){
			x=-vehicleSprite.getWidth()-5;
		}
		isFinish = false;
		setSpeed(frame);
	}
	private void setSpeed(int frame){
		if(frame == 3 || frame == 6 || frame == 10 || frame == 11){
			steps = 1;
		}else if(frame == 12 || frame == 13 || frame == 14 ){
			steps = 2;
		}else {
			steps = 3;
		}
	}
	public void draw(Graphics g){
		if(vehicleSprite!=null){
			if(isMoving){
				move();
			}
			vehicleSprite.setPosition(x, y);
			vehicleSprite.paint(g);
		}
	}
	public boolean isFinish(){
		return isFinish;
	}
	public Sprite getSprite(){
		return vehicleSprite;
	}
	public void setMove(boolean status){
		this.isMoving = status;
	}
	public void move(){
		if(dir==LEFT){
			x-=steps;
			if(x<-vehicleSprite.getWidth()-5){
				isFinish = true;
			}
		}else if(dir==RIGHT){
			x+=steps;
			if(x>width){
				isFinish = true;
			}
		}
	}
}
