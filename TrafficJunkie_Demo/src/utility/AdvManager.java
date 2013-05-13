package utility;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import app.GameScreen;
import vInAppAdEngine.VservAd;
import vInAppAdEngine.VservAdListener;
import vInAppAdEngine.VservManager;

public class AdvManager implements VservAdListener {

	
	private VservAd vservAd;
    private int WIDTH;
    private int HEIGHT;
    private int topYY;
    private int botYY;
    private Image topAdvImage;
    private Image botAdvImage;
    private String advText;
    private boolean isAdvReceived= false;
    private MIDlet midlet;
    public static final byte TOP_ACTIVE = 1;
    public static final byte BOTH_ACTIVE = 2;
    private static AdvManager advManager;
    private Timer timer;// adv timer
    private final int advUpdateTime = 20 ;
    private Canvas canvas;
    private int advCounter = 0;
    private Image appwarpImage;
    private Image app42Image;
    private boolean isApp42 = true;
    private final String app42URL= "http://api.shephertz.com";
    private final String appwarpURL= "http://appwarp.shephertz.com";
    private AdvManager() {
    	try {
    		topAdvImage = appwarpImage = Image.createImage("/216x36_Banner.jpg");
    		botAdvImage = app42Image = Image.createImage("/216x36_Banner42.jpg");
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static AdvManager instanse(){
    	if(advManager==null){
    		advManager = new AdvManager();
    	}
    	return advManager;
    }
    public void asyncInitSetup(final MIDlet midlet){
    	this.midlet = midlet;
    	new Thread(new Runnable() {
			public void run() {
				initSetUp(midlet);
			}
		}).start();
    }
	public void initSetUp(MIDlet midlet){
		
		Hashtable vservConfigTable=new Hashtable();
        vservConfigTable.put("appId","9499");
        VservManager vservManager=new VservManager(midlet,vservConfigTable);
        vservAd=new VservAd(this);
        startTimer();
    }
	public void setCoordinate(int width,int height,int topY,int botY,Canvas canvas){
		this.WIDTH = width;
		this.HEIGHT = height;
		this.topYY = topY;
		this.botYY = botY;
		this.canvas = canvas;
	}
	public void drawAdv(Graphics g,int status){
		if(status == TOP_ACTIVE){
			drawTopAd(g);
		}
		if(status == BOTH_ACTIVE){
			drawBottomAd(g);
		}
	}
	private void drawTopAd(Graphics g){
		if(isAdvReceived){
			if(topAdvImage!=null){
				g.drawImage(topAdvImage,WIDTH/2,topYY,Graphics.TOP|Graphics.HCENTER);
			}else if( advText!=null && advText.length()>0 ){
				g.setColor(0XFFFFFF);
				g.fillRect(0, topYY, WIDTH, StringDB.FONT_MEDIUM.getHeight());
				g.setColor(0X000000);
				g.setFont(StringDB.FONT_MEDIUM);
				g.drawString(advText,WIDTH/2,topYY,Graphics.TOP|Graphics.HCENTER);
			}
		}else{
			g.drawImage(topAdvImage,WIDTH/2,topYY,Graphics.TOP|Graphics.HCENTER);
		}
	}
	private void drawBottomAd(Graphics g){
		if(isAdvReceived){
			if(topAdvImage!=null){
				g.drawImage(botAdvImage,WIDTH/2,botYY,Graphics.BOTTOM|Graphics.HCENTER);
			}else if( advText!=null && advText.length()>0 ){
				g.setColor(0XFFFFFF);
				g.fillRect(0, botYY-StringDB.FONT_MEDIUM.getHeight(), WIDTH, botYY);
				g.setColor(0X000000);
				g.setFont(StringDB.FONT_MEDIUM);
				g.drawString(advText,WIDTH/2-StringDB.FONT_MEDIUM.getHeight()/2,botYY,Graphics.TOP|Graphics.HCENTER);
			}
		}else{
			g.drawImage(botAdvImage,WIDTH/2,botYY,Graphics.BOTTOM|Graphics.HCENTER);
		}
	}
	public void asyncRequestAdv(){
		advCounter++;
		if( advCounter>advUpdateTime ){
			advCounter = 0;
			new Thread(new Runnable() {
				public void run() {
					requestAdv();
				}
			}).start();	
		}
	}
	public void requestAdv(){
//		System.out.println("send request for adv..");
		vservAd.requestAd();
		isAdvReceived = false;
	}

	public void vservAdReceived(Object obj)
	{
		
		topAdvImage = null;
		botAdvImage = null;
		advText = "";
		isAdvReceived = true;
        if(obj==vservAd)
        {
//        	System.out.println("Adv received: "+((VservAd)obj).getAdType());
            if(((VservAd)obj).getAdType().equals(VservAd.AD_TYPE_IMAGE))
            {
            	Image imageAd=(Image)((VservAd)obj).getAd();
                if(imageAd!=null){
                	topAdvImage = imageAd;
                	botAdvImage = imageAd;
                }
//                System.out.println("got images");
                // got image
            }
            else if(((VservAd)obj).getAdType().equals(VservAd.AD_TYPE_TEXT))
            {
            	String textAd=(String)((VservAd)obj).getAd();
            	if(textAd!= null && textAd.length()>0){
            		advText = textAd;
            	}
//            	System.out.println("got text");
               // got text
            }
        }
        canvas.repaint();
    }
	public void vservAdFailed(Object obj) {
		isAdvReceived = false;
		if(isApp42){
			isApp42 = false;
			topAdvImage = botAdvImage = app42Image;
		}else{
			isApp42 = true;
			topAdvImage = botAdvImage = appwarpImage;
		}
		canvas.repaint();
	}
	public void pointerReleased(int x,int y, int status){
		
		if(status == TOP_ACTIVE){
			if(x>WIDTH/2-topAdvImage.getWidth()/2 && x<WIDTH/2+topAdvImage.getWidth()/2 
					&& y>topYY && y<topYY+topAdvImage.getHeight()){
				handleClick();
			}
		}
		else if(status == BOTH_ACTIVE){
			if(x>WIDTH/2-botAdvImage.getWidth()/2 && x<WIDTH/2+botAdvImage.getWidth()/2 
					&& y>botYY-botAdvImage.getHeight() && y<botYY){
				handleClick();
			}
		}
		else if(x>WIDTH/2-botAdvImage.getWidth()/2 && x<WIDTH/2+botAdvImage.getWidth()/2 
				&& y>botYY-botAdvImage.getHeight() && y<botYY){
			handleClick();
		}
	}
	private void handleClick(){
		if(isAdvReceived){
		    if(vservAd.hasAction){
	            vservAd.handleAdAction();
	        }
	    }else{
	    	if(isApp42){
	    		openURL(app42URL);
	    	}else{
	    		openURL(appwarpURL);
	    	}
	    }
    }
	private void openURL(String URL){
		try {
			System.out.println("midlet: "+midlet);
			midlet.platformRequest(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startTimer(){
		if(timer==null){
			timer=new Timer();
			timer.schedule(new AdvReloadTask(this), 100, 1000);
		}
	}
	public void endTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}
	
}
class AdvReloadTask extends TimerTask{
	private AdvManager advManager;
	AdvReloadTask(AdvManager advManager){
		this.advManager = advManager;
	}
	public void run() {
		advManager.asyncRequestAdv();
	}
}
