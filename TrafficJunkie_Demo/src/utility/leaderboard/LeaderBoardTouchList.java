package utility.leaderboard;



import javax.microedition.lcdui.Graphics;

import utility.StringDB;
import utility.TextList;


public class LeaderBoardTouchList {

	private int selColor;
	private int unselColor;
	
	private String[] item1;
	private String[] item2;
	private String[] item3;
	
	private int x;
	private int y;
	private int XX;
	private int YY;
	private int sXX;
	private int sYY;
	private int scrollController=20;
	private int WIDTH;
	private int screen_width=200;
	private int HEIGHT;
	private int startY;
	private int endY;
	private int gap=5;
	private int selectedIndex=-1;
	private int selectedId=-1;
	private int startIndex=-1;
	private boolean isImageList;
	private int totalHeight;
	private int screenHeight;
	private int scrollHeight;
	private int objectWidth;
	private int objectHeight;
	private int noOfVisibleItem;
	private int maxItem;
	private int pressedXX;
	private int pressedYY;
	private int part[]={50,100,50};
	private String name[]={"Rank","Name","Points"};
	private int l1,l2,l3;
	private int noOfChar=0;
	
	private TextList leaderBoardTextList;
	public LeaderBoardTouchList(TextList leaderBoardTextList) {
		this.leaderBoardTextList=leaderBoardTextList;
		l1=StringDB.FONT_SMALL.stringWidth("Rank")+5;
		l3=StringDB.FONT_SMALL.stringWidth("Points")+5;
		l2=App42Canvas.SCREEN_WIDTH-(l1+l3);
		part[0]=l1;
		part[1]=l2;
		part[2]=l3;
		noOfChar=l2/StringDB.FONT_SMALL.charWidth('A');
		screen_width = App42Canvas.SCREEN_WIDTH;
	}
	
	public void createList(int width,int height,int startY,int endY,int selColor,int unselColor,String[] item1 ,String[] item2,String[] item3,int objWidth,int objHeight,boolean isRoundRect){
		this.isImageList=false;
		this.WIDTH=width;
		this.HEIGHT=height;
		this.startY=this.y=startY;
		this.endY=endY;
		this.unselColor=unselColor;
		this.item1=item1;
		this.item2=item2;
		this.item3=item3;
		this.objectWidth=objWidth;
		this.objectHeight=objHeight+gap;
		calculateListData();
	}
	private void calculateListData(){
		screenHeight=endY-startY;
		totalHeight=item1.length*objectHeight;
		scrollHeight=totalHeight-screenHeight;
		noOfVisibleItem=screenHeight/objectHeight;
		this.x=WIDTH/2-screen_width/2;
		maxItem = item1.length;
	}
	public void draw(Graphics g){
		g.setFont(StringDB.FONT_SMALL);
		drawHeader(g);
		g.setClip(x, startY, WIDTH, screenHeight);
		if(item1!=null && item1.length>0){
			for(int i=0;i<item1.length;i++){
				if(selectedId == i || (XX>x && XX<x+objectWidth && YY>y+(i*objectHeight)+sYY && YY<y+objectHeight+(i*objectHeight)+sYY)){
					selectedIndex=i;
					
						g.setColor(selColor);
						g.setFont(StringDB.FONT_SMALL_BOLD);
						g.setColor(0XFFFFFF);
						g.drawString(item1[i], x+l1/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						if(item2!=null && item2.length>0){
							String str = item2[i];
							if(str.length()>noOfChar){
								str=str.substring(0, noOfChar)+"..";
							}
							g.drawString(str, x+l1+l2/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						}
						if(item3!=null && item3.length>0){
							g.drawString(item3[i], x+l1+l2+l3/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						}
					
				}else{
						g.setFont(StringDB.FONT_SMALL);
						g.setColor(unselColor);
						g.setColor(0XFFFFFF);
						g.drawString(item1[i], x+l1/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						if(item2!=null && item2.length>0){
							String str = item2[i];
							if(str.length()>noOfChar){
								str=str.substring(0, noOfChar)+"..";
							}
							g.drawString(str, x+l1+l2/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						}
						if(item3!=null && item3.length>0){
							g.drawString(item3[i], x+l1+l2+l3/2, y+((objectHeight)*i)+sYY, Graphics.TOP|Graphics.HCENTER);
						}
					
				}
				g.setColor(0);
				g.drawLine(x,  y+((objectHeight)*i)+sYY+objectHeight-1, x+screen_width, y+((objectHeight)*i)+sYY+objectHeight-1);
			}
		}else{
			g.fillRoundRect(50, HEIGHT/2-20, WIDTH-100, 40, 10, 10);
			g.setFont(StringDB.FONT_SMALL_ITALIC);
			g.setColor(0XFFFFFF);
			g.drawString("No Item", WIDTH/2-StringDB.FONT_SMALL_ITALIC.stringWidth(StringDB.fetching_data)/2, HEIGHT/2-StringDB.FONT_SMALL.getHeight()/2, Graphics.TOP|Graphics.LEFT);
		}
	}
	private void drawHeader(Graphics g){
		g.setFont(StringDB.FONT_SMALL);
		int initY=0;
		g.setClip(0, 0, WIDTH, HEIGHT);
		for(int i=0;i<name.length;i++){
			g.setColor(0XFFBF00);
			g.fillRect(x+initY, startY-objectHeight, part[i], objectHeight);
			g.setColor(0XF8ECE0);
			g.drawRect(x+initY, startY-objectHeight, part[i], objectHeight);
			g.setColor(0);
			g.drawString(name[i], x+initY+part[i]/2, startY-objectHeight/2-StringDB.FONT_SMALL.getHeight()/2, Graphics.TOP|Graphics.HCENTER);
			initY+=part[i];
		}
	}
	public void pointerPressed(int x,int y){
		selectedId=-1;
		if(y>startY && y<endY){
			XX=pressedXX=x;
			YY=pressedYY=y;
		}
	}
	public void pointerDragged(int x,int y){
		selectedId=-1;
		if(item1!=null && item1.length>0){
			if( x<XX-objectWidth/2 || x>XX+objectWidth/2 || y<YY-objectHeight/2 || y>YY+objectHeight/2 ){
				selectedIndex=-1;
				this.XX=-10;
				this.YY=-10;
	
				if(item1.length>noOfVisibleItem && (y<YY-objectHeight/2 || y>YY+objectHeight/2) ){
					if(pressedYY>y){
						if(Math.abs(sYY)<scrollHeight){
							sYY=sYY+(YY-y)/scrollController;// scroll for bottom item or scroll up
						}
					}else{
						if(sYY<0){
							sYY=sYY+(y-YY)/scrollController;// scroll for upper item or scroll down
						}
					}
				}
			}
		}
	}
	public void pointerReleased(int x,int y){
		this.XX=-10;
		this.YY=-10;
		selectedId=-1;
		selectedIndex=-1;
		pressedXX=-10;
		pressedYY=-10;
	}
	public void keyPressed(int keyCode){
		switch (keyCode) {
		case utility.Constants.UP_KEY:
		case utility.Constants.TWO_KEY:
			processUpKey();
		break;
		case utility.Constants.DOWN_KEY:
		case utility.Constants.EIGHT_KEY:
			processDownKey();
		break;
		case utility.Constants.OK_KEY:
		case utility.Constants.FIVE_KEY:
			
		break;
		}
	}
	public void processUpKey(){
		if( selectedId == -1 ){
			if(maxItem>noOfVisibleItem){
				selectedId=maxItem-1;
				sYY=-(maxItem-noOfVisibleItem)*objectHeight;
				startIndex=maxItem-noOfVisibleItem;
			}else{
				selectedId=maxItem-1;
				startIndex=maxItem-noOfVisibleItem;
			}
			return;
		}
		selectedId--;
		if(selectedId<0){
			sYY=0;
			startIndex=0;
			leaderBoardTextList.setSelectedID(0);
			leaderBoardTextList.setMinTouch(false);
		}
		if(startIndex>0 && selectedId<maxItem-noOfVisibleItem){
			startIndex--;
			sYY+=objectHeight;
		}
	}
	public void processDownKey(){
		if( selectedId == -1 ){
			sYY=0;
			startIndex=0;
		}
		selectedId++;
		if(selectedId >= noOfVisibleItem && (startIndex<maxItem-noOfVisibleItem)){
			sYY-=objectHeight;
			startIndex++;
		}
		if(selectedId >= maxItem){
			selectedId=-1;
			sYY=0;
			startIndex=0;
			leaderBoardTextList.setSelectedID(0);
			leaderBoardTextList.setMaxTouch(false);
		}
	}
	public int getSelectedIndex(){
		return selectedIndex;
	}
	public int getSelectedId(){
		return selectedId;
	}
	public void setSelectedId(int index){
		this.selectedId = index;
	}
	
}

