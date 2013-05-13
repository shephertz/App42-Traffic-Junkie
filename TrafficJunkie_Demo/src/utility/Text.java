package utility;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class Text {
	private String text;
	private int textColorSelect;
	private int textColorUnselect;
	private int bgColorSelect;
	private int bgColorUnselect;
	private Font font;
	private int objWidth;
	private int objHeight;
	private int x;
	private int y;
	private boolean bgEnable=false;
	private Image select,unselect;
	private boolean bgImageEnable=false;
	public static final byte ALIGN_CENTER=0;
	public static final byte ALIGN_LEFT=1;
	private byte align=0;
	public Text(String text,int textColorS,int textColorU,int bgColorS,int bgColorU,Font font,int x,int y,boolean bg) {
		this.text=text;
		this.textColorSelect=textColorS;
		this.textColorUnselect=textColorU;
		this.bgColorSelect=bgColorS;
		this.bgColorUnselect=bgColorU;
		this.font=font;
		this.x=x;
		this.y=y;
		this.bgEnable=bg;
		objWidth=font.stringWidth(text)+font.stringWidth(text)/5;
		if(bg){
			objHeight=font.getHeight()+font.getHeight()/2;
		}else{
			objHeight=font.getHeight();
		}
	}
	public Text(String text,int textColorS,int textColorU,int bgColorS,int bgColorU,Font font,int x,int y,boolean bg,byte align) {
		this.text=text;
		this.textColorSelect=textColorS;
		this.textColorUnselect=textColorU;
		this.bgColorSelect=bgColorS;
		this.bgColorUnselect=bgColorU;
		this.font=font;
		this.x=x;
		this.y=y;
		this.bgEnable=bg;
		this.align=align;
		objWidth=font.stringWidth(text)+font.stringWidth(text)/5;
		if(bg){
			objHeight=font.getHeight()+font.getHeight()/2;
		}else{
			objHeight=font.getHeight();
		}
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTextColorSelect() {
		return textColorSelect;
	}
	public void setTextColorSelect(int textColorSelect) {
		this.textColorSelect = textColorSelect;
	}
	public int getTextColorUnselect() {
		return textColorUnselect;
	}
	public void setTextColorUnselect(int textColorUnselect) {
		this.textColorUnselect = textColorUnselect;
	}
	public int getBgColorSelect() {
		return bgColorSelect;
	}
	public void setBgColorSelect(int bgColorSelect) {
		this.bgColorSelect = bgColorSelect;
	}
	public int getBgColorUnselect() {
		return bgColorUnselect;
	}
	public void setBgColorUnselect(int bgColorUnselect) {
		this.bgColorUnselect = bgColorUnselect;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getObjectWidth(){
		return objWidth;
	}
	public int getObjectHeight(){
		return objHeight;
	}
	public boolean getBGStatus(){
		return bgEnable;
	}
	public boolean getBGImageStatus(){
		return bgImageEnable;
	}
	public Image getSelectImage(){
		return select;
	}
	public Image getUnselectImage(){
		return unselect;
	}
	public byte getAlgnment(){
		return this.align;
	}
	public void setBgImage(Image select,Image unselect){
		this.bgImageEnable=true;
		this.select=select;
		this.unselect=unselect;
		objWidth=select.getWidth();
		objHeight=select.getHeight();
//		System.out.println("Working...");
	}
	public void setWidthHeight(int width,int height){
		this.objWidth=width;
		this.objHeight=height;
	}
}
