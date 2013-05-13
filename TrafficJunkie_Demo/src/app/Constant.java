package app;

import java.util.Random;

public class Constant {
	
	public static int WIDTH = 240;
	public static int HEIGHT = 320;
	
	public static final int DELAY_LEVEL1=50;
	public static final int DELAY_LEVEL2=50;
	public static final int VEHICLE_HEIGHT=25;
	
	private static Random random;
	
	public static final int MAX_USER = 10;
	public static final int DIAMOND_LEVEL1 = 10;
	public static final int DIAMOND_LEVEL2 = 15;
	public static final int DIAMOND_LEVEL3 = 20;
	public static final int DIAMOND_LEVEL4 = 25;
	public static final int DIAMOND_LEVEL5 = 30;
	
	
	public static final int DIAMOND_POINTS = 5;
	public static final int USER_POINTS = 10;
	
	public static final String alertHelp[]={"Collect Diamonds to","complete level","Help users to ","cross the road"};
	public static final String alertExit[]={"Do you really","want to exit?","your current ","score will not save"};
	
	
	public static int getRandom(int num){
		if(random== null){
			random = new Random();
		}
		return random.nextInt(num);
	}
}
