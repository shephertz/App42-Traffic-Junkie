package utility;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Util {
	public static String[] split(String str,String seprator){
		Vector v = new Vector();
		return process(v, str, seprator);
	}
	private static String[] process(Vector v,String str,String seprator){
		String sStart = null;
		if(str.indexOf(seprator)!=-1){
			sStart= str.substring(0, str.indexOf(seprator));
			v.addElement(sStart);
			String strRem=str.substring(str.indexOf(seprator)+seprator.length(), str.length());
			process(v, strRem, seprator);
		}else{
			v.addElement(str);
		}
		String[] strArray = new String[v.size()];
		for(int i=0;i<strArray.length;i++){
			strArray[i] = v.elementAt(i).toString();
		}
		return strArray;
	}
	public static Image getThumbnail(Image image,int thumbWidth, int thumbHeight)
	{
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		Image thumb = Image.createImage(thumbWidth, thumbHeight);
		
		Graphics g = thumb.getGraphics();
		
		for (int y = 0; y < thumbHeight; y++)
		{
		for (int x = 0; x < thumbWidth; x++)
		{
		g.setClip(x, y, 1, 1);
		int dx = x * sourceWidth / thumbWidth;
		int dy = y * sourceHeight / thumbHeight;
		g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
		}
		}
		Image immutableThumb = Image.createImage(thumb);
		return immutableThumb;
	} 
	
}
