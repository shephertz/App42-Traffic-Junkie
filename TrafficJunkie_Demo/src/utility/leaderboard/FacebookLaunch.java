package utility.leaderboard;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Graphics;
import utility.StringDB;
import utility.Text;
import utility.TextList;
import utility.leaderboard.App42ServiceHandler.FacebookAccessTokenListener;
import utility.leaderboard.App42ServiceHandler.FacebookUserProfileListener;


import com.shephertz.app42.paas.sdk.jme.social.Social;



public class FacebookLaunch implements FacebookUserProfileListener,FacebookAccessTokenListener {

	private int WIDTH;
	private int HEIGHT;
	private App42Canvas app42Canvas;
	private TextList launchTextList;
	
	public FacebookLaunch(App42Canvas app42Canvas, int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.app42Canvas = app42Canvas;
		launchTextList = new TextList();
		createLaunchScreen();
		if(RMS.getInstance().token.length()>0){
			App42ServiceHandler.instance().setToken(RMS.getInstance().token);
		}
	}
	public void loadData(){
		if(UserContext.MyUserName.length()>0){
			app42Canvas.leaderBoard.loadData();
			app42Canvas.changeStatus(App42Canvas.STATUS_LEADER_BOARD);
		}
	}
	private void loadProfileData() {
		if (RMS.getInstance().token.length() > 0) {
			app42Canvas.showProgress();
			tLoadProfileData();
		}
	}
	private void tLoadProfileData() {
		new Thread(new Runnable() {
			public void run() {
				fetchFacebookProfile();
			}
		}).start();
	}

	private void fetchFacebookProfile() {
		try {
			if (UserContext.MyUserName.trim().length() == 0) {
				App42ServiceHandler.instance().fetchFacebookProfile(this);
			}
		} catch (Exception e) {
			app42Canvas.stopProgress();
			app42Canvas.refresh();
			RMS.getInstance().writeInFacebookDB("");
			App42ServiceHandler.oauthToken="";
			e.printStackTrace();
		}
	}

	private void createLaunchScreen() {
		int noOfElement = 5;
		int gap = 30 ;
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		launchTextList.addElement(0, "facebook", new Text("", 0XFFFFFF,0XFFFFFF, 0XA4A9F7, 0X8C93F7, StringDB.FONT_MEDIUM, WIDTH / 2,temp, true));
		temp = temp + gap;
		launchTextList.addElement(-2, "signin", new Text(StringDB.sign_in_and_win, 0XFFFFFF, 0XFFFFFF, 0XA4A9F7,0X8C93F7, StringDB.FONT_SMALL, WIDTH / 2, temp, false));
		temp = temp + gap;
		launchTextList.addElement(1, "whatis", new Text("LeaderBoard ?",0XEE7F72, 0XEE5A49, 0XFF0000, 0X8C93F7,StringDB.FONT_SMALL_BOLD, WIDTH / 2, temp, false));
		temp = temp + gap;
		launchTextList.addElement(-2, "every", new Text(StringDB.every_play,0XFFFFFF, 0XFFFFFF, 0XA4A9F7, 0X8C93F7, StringDB.FONT_SMALL,WIDTH / 2, temp, false));
		temp = temp + gap;
		launchTextList.addElement(2, "close",new Text("Back to Game", 0X656262, 0X000000, 0X00FF00,	0X8C93F7, StringDB.FONT_SMALL_BOLD, WIDTH / 2,temp,false));
		launchTextList.createList();
		launchTextList.setBgImage("facebook", ImageLoader.facebookS,ImageLoader.facebookU);
		launchTextList.setBgImage("whatis", app.ImageLoader.select,app.ImageLoader.unselect);
		launchTextList.setBgImage("close", app.ImageLoader.select,app.ImageLoader.unselect);
	}

	public void draw(Graphics g) {
		launchTextList.draw(g);
	}

	public void pointerPressed(int x, int y) {
		launchTextList.pointerPressed(x, y);
	}

	public void pointerReleased(int x, int y) {
		handleOk(launchTextList.getSelected().trim());
		launchTextList.pointerReleased();
	}

	public void pointerDragged(int x, int y) {

	}

	public void keyPressed(int keyCode) {
		launchTextList.keyPressed(keyCode);
		if (keyCode == utility.Constants.OK_KEY
				|| keyCode == utility.Constants.FIVE_KEY) {
			if (launchTextList.getSelected().length() > 0) {
				handleOk(launchTextList.getSelected());
			}
		}
	}

	public void handleOk(String str_selected) {
		if (str_selected.equals("facebook")) {
			if (RMS.getInstance().token.length() == 0) {
				App42ServiceHandler.instance().launchFacebook(this);
			}
		} else if (str_selected.equals("whatis")) {
			showLeaderboardDescription();
		} else if (str_selected.equals("close")) {
			app42Canvas.backToCallingDisplayable();
		}
	}

	private void showLeaderboardDescription() {
		String str = "This is a Social Leaderboard contain option to check " +
				"your rank among friends and global.";
		Alert alert = new Alert("Note", str, null, AlertType.INFO);
		app42Canvas.getDisplay().setCurrent(alert);
	}

	
	public void onGetFacebookUserProfile(Social social) {
		UserContext.MyUserName = social.getFacebookProfile().getId();
		UserContext.MyDisplayName = social.getFacebookProfile().getName();
		UserContext.MyPicUrl = social.getFacebookProfile().getPicture();
		App42ServiceHandler.instance().storeUserProfile();
		RMS.getInstance().writeInUserprofileDB(UserContext.MyUserName, UserContext.MyDisplayName, UserContext.MyPicUrl);
		loadData();
	}

	public void onGetFacebookAccessToken(String token) {
		if (token!=null && token.length()>0) {
			loadProfileData();
		}
	}
}
