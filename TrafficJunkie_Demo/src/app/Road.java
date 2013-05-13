package app;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Road {
	private int LEVEL=1;
	private Vehicle[] vehicles;
	private int width;
	private int height;
	private int XX;
	private int YY;
	private int counter=0;
	private int delayCounter=0;
	private byte userCounter=0;
	private byte dir;
	private int upperY;
	private int lowerY;
	
	public Road(int width,int height,int initX,int initY,int uppery,int lowery,byte dir) {
		this.width = width;
		this.height = height;
		this.XX = initX;
		this.YY = initY;
		this.upperY = uppery;
		this.lowerY = lowery;
		this.dir = dir;
		createStack(1);
		delayCounter = Constant.DELAY_LEVEL1+Constant.getRandom(Constant.DELAY_LEVEL1); 
	}
	private void createStack(int level){
		this.LEVEL = level;
		vehicles = new Vehicle[level];
		for(int i=0;i<vehicles.length;i++){
			vehicles[i] = new Vehicle(this.XX,this.YY,upperY,lowerY);
		}
	}
	private void genVehicle(){
		counter++;
		if(counter%delayCounter == 0){
			for(int i=0;i<vehicles.length;i++){
				if(vehicles[i].getSprite()==null){
					vehicles[i].reset(getVehicleNumber(),dir);
					break;
				}else if(vehicles[i].isFinish()){
					vehicles[i].reset(getVehicleNumber(),dir);
					break;
				}
				
			}
		}
	}
	public Vehicle[] getVehicles(){
		return vehicles;
	}
	private int getVehicleNumber(){
		return Constant.getRandom(15);
	}
	public void stop(){
		for(int i=0;i<vehicles.length;i++){
			vehicles[i].setMove(false);
		}
	}
	public void start(){
		for(int i=0;i<vehicles.length;i++){
			vehicles[i].setMove(true);
		}
	}
	public void paint(Graphics g){
		genVehicle();
		for(int i=0;i<vehicles.length;i++){
			vehicles[i].draw(g);
		}
	}
}
