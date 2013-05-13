package utility;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class TextList {
	
	private Vector vectorID;
	private Vector vectorNAME;
	private Vector vectorText;
	
	
	private int[] arrID;
	private String[] arrNAME;
	private Text[] arrText;
	
	
	private int XX=-10;
	private int YY=-10;
	
	private int selectedIndex=-1;
	private int selectedId=0;
	private String selectedText="";
	private int maxIndex=0;
	private boolean isMinTouch=false;
	private boolean isMaxTouch=false;
	private boolean isListHandle=false;
	
	public TextList() {
		vectorID = new Vector();
		vectorNAME = new Vector();
		vectorText = new Vector();
	}
	
	private void clear(){
		XX=-10;
		YY=-10;
		vectorID.removeAllElements();
		vectorNAME.removeAllElements();
		vectorText.removeAllElements();
	}
	public void addElement(int id,String name,Text text){
		if(id>=0){
			maxIndex=id;
		}
		vectorID.addElement(String.valueOf(id));
		vectorNAME.addElement(name);
		vectorText.addElement(text);
	}
	public void setBgImage(String text,Image select,Image unselect){
		for(int i=0;i<arrNAME.length;i++){
			if(arrNAME[i].equals(text)){
				arrText[i].setBgImage(select, unselect);
				break;
			}
		}
	}
	public void setText(String name,String text){
		for(int i=0;i<arrNAME.length;i++){
			if(arrNAME[i].equals(name)){
				arrText[i].setText(text);
				break;
			}
		}
	}
	
	public void setWidthHeight(String text,int width,int height){
		for(int i=0;i<arrText.length;i++){
			if(arrText[i].getText().equals(text)){
				arrText[i].setWidthHeight(width, height);
			}
		}
	}
	public void createList(){
		int maxSize=vectorID.size();
		arrID=new int[maxSize];
		arrNAME=new String[maxSize];
		arrText=new Text[maxSize];
		
		
		for(int i=0;i<vectorID.size();i++){
			arrID[i]=Integer.parseInt(vectorID.elementAt(i).toString());
			arrNAME[i]=vectorNAME.elementAt(i).toString();
			arrText[i]=(Text)vectorText.elementAt(i);
		}
		clear();
	}
	public void draw(Graphics g){
		for(int i=0;i<arrID.length;i++){//selectedIndex==arrID[i]
				if( selectedId == arrID[i] || ( XX>arrText[i].getX()-arrText[i].getObjectWidth()/2 && XX<arrText[i].getX()+arrText[i].getObjectWidth()/2 &&
						YY>arrText[i].getY()-arrText[i].getObjectHeight()/2 && YY<arrText[i].getY()+arrText[i].getObjectHeight()/2)){
							selectedIndex=i;
							selectedText=arrNAME[i];
							g.setColor(0);
							if(arrText[i].getAlgnment()==Text.ALIGN_CENTER){
								g.drawRoundRect(arrText[i].getX()-arrText[i].getObjectWidth()/2-2, arrText[i].getY()-arrText[i].getObjectHeight()/2-2, arrText[i].getObjectWidth()+2, arrText[i].getObjectHeight()+2, 5, 5);
							}else{
								g.drawRoundRect(arrText[i].getX()-2, arrText[i].getY()-arrText[i].getObjectHeight()/2-2, arrText[i].getObjectWidth()+2, arrText[i].getObjectHeight()+2, 5, 5);
							}
							if(arrText[i].getBGStatus()){
								g.setColor(arrText[i].getBgColorSelect());
								g.fillRoundRect(arrText[i].getX()-arrText[i].getObjectWidth()/2, arrText[i].getY()-arrText[i].getObjectHeight()/2, arrText[i].getObjectWidth(), arrText[i].getObjectHeight(), 5, 5);
							}
							if(arrText[i].getBGImageStatus()){
								if(arrText[i].getAlgnment()==Text.ALIGN_CENTER){
									g.drawImage(arrText[i].getSelectImage(), arrText[i].getX(), arrText[i].getY(), Graphics.HCENTER|Graphics.VCENTER);
								}else{
									g.drawImage(arrText[i].getSelectImage(), arrText[i].getX(), arrText[i].getY(), Graphics.LEFT|Graphics.VCENTER);
								}
							}
							g.setColor(arrText[i].getTextColorSelect());
							
				}else{
					if(arrText[i].getBGStatus()){
						g.setColor(arrText[i].getBgColorUnselect());
						g.fillRoundRect(arrText[i].getX()-arrText[i].getObjectWidth()/2, arrText[i].getY()-arrText[i].getObjectHeight()/2, arrText[i].getObjectWidth(), arrText[i].getObjectHeight(), 5, 5);
					}
					if(arrText[i].getBGImageStatus()){
						if(arrText[i].getBGImageStatus()){
							if(arrText[i].getAlgnment()==Text.ALIGN_CENTER){
								g.drawImage(arrText[i].getUnselectImage(), arrText[i].getX(), arrText[i].getY(), Graphics.HCENTER|Graphics.VCENTER);
							}else{
								g.drawImage(arrText[i].getUnselectImage(), arrText[i].getX(), arrText[i].getY(), Graphics.LEFT|Graphics.VCENTER);
							}
						}
					}
					g.setColor(arrText[i].getTextColorUnselect());
				}
			g.setFont(arrText[i].getFont());
//			g.setColor(0);
//			g.fillRect(10, 10, 100, 100);
			if(arrText[i].getAlgnment()==Text.ALIGN_CENTER){
				g.drawString(arrText[i].getText(), arrText[i].getX(), arrText[i].getY()-arrText[i].getFont().getHeight()/2, Graphics.TOP|Graphics.HCENTER);
			}else{
				g.drawString(arrText[i].getText(), arrText[i].getX(), arrText[i].getY()-arrText[i].getFont().getHeight()/2, Graphics.TOP|Graphics.LEFT);
			}
		}
	}
	public void pointerPressed(int x,int y){
		this.selectedId=-1;
		selectedText="";
		this.XX=x;
		this.YY=y;
	}
	public void pointerReleased(){
		this.XX=-10;
		this.YY=-10;
		selectedIndex=-1;
		selectedText="";
	}
	public String getSelected(){
		return selectedText;
	}
	public int getSelectedIndex(){
		return selectedIndex;
	}
	public void keyPressed(int keyCode){
		switch (keyCode) {
		case Constants.UP_KEY:
		case Constants.TWO_KEY:
			processUpKey();
		break;
		case Constants.DOWN_KEY:
		case Constants.EIGHT_KEY:
			processDownKey();
		break;
		case Constants.OK_KEY:
		case Constants.FIVE_KEY:
			
		break;
		}
		
	}
	public void processUpKey(){
		if(isListHandle){
			if(isMinTouch==false){
				selectedId--;
				if(selectedId<0){
					selectedText="";
					isMinTouch=true;
					selectedId=-1;
				}
			}
		}else{
			selectedId--;
			if(selectedId<0){
				selectedId=maxIndex;
			}
		}
	}
	public void processDownKey(){
		if(isListHandle){
			if(isMaxTouch==false){
				selectedId++;
				if(selectedId>maxIndex){
					selectedText="";
					isMaxTouch=true;
					selectedId=-1;
				}
			}
		}else{
			selectedId++;
			if(selectedId>maxIndex){
				selectedId=0;
			}
		}
		
	}
	public boolean getMaxTouch(){
		return isMaxTouch;
	}
	public boolean getMinTouch(){
		return isMinTouch;
	}
	public void setMaxTouch(boolean max){
		this.isMaxTouch = max;
	}
	public void setMinTouch(boolean min){
		this.isMinTouch = min;
	}
	public void setListHandle(boolean isListHandle){
		this.isListHandle = isListHandle;
	}
	public void setSelectedID(int id){
		this.selectedId=id;
	}
}
