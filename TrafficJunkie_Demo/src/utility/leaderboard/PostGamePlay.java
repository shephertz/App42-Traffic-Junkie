package utility.leaderboard;

import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Graphics;
import utility.StringDB;
import utility.Text;
import utility.TextList;
import utility.leaderboard.App42ServiceHandler.App42LeaderBoardListener;
import utility.leaderboard.App42ServiceHandler.FacebookAccessTokenListener;
import utility.leaderboard.App42ServiceHandler.FacebookUserProfileListener;
import com.shephertz.app42.paas.sdk.jme.social.Social;

public class PostGamePlay implements App42LeaderBoardListener,ZapakResultHandler,FacebookUserProfileListener,FacebookAccessTokenListener{
	private int WIDTH;
	private int HEIGHT;
	private App42Canvas app42Canvas;
	private TextList preFBTextList;
	private TextList postFBTextList;
	private boolean isPreFb=true;
	private long score=0;
	private int rank=0;
	
	public PostGamePlay(App42Canvas app42Canvas,int width,int height) {
		WIDTH=width;
		HEIGHT=height;
		this.app42Canvas=app42Canvas;
		preFBTextList = new TextList();
		postFBTextList = new TextList();
		createPreFBList();
		createPostFBList();
	}
	
	private void createPreFBList(){
		int noOfElement = 5;
		int gap = 30 ;
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		preFBTextList.addElement(-2, "1", new Text("Sign in to Check Rank",0XFFFFFF,0XFFFFFF,StringDB.color_list_top,StringDB.color_list_top,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp=temp+StringDB.FONT_SMALL.getHeight()*2;
		preFBTextList.addElement(0, "fb", new Text("",0XFFFFFF,0XFFFFFF,0XE4513D,0XCF3A26,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp=temp+StringDB.FONT_SMALL.getHeight()*2;
		preFBTextList.addElement(-2, "score", new Text("You scored: ",0XFFFFFF,0XFFFFFF,0XE4513D,0XCF3A26,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp=temp+StringDB.FONT_SMALL.getHeight()*2;
		preFBTextList.addElement(1, "close", new Text("Back to Game",0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL_BOLD,WIDTH/2, temp,false));
		preFBTextList.createList();
		preFBTextList.setBgImage("fb", ImageLoader.facebookS, ImageLoader.facebookU);
		preFBTextList.setBgImage("close", app.ImageLoader.select, app.ImageLoader.unselect);
	}
	public void asyncLoadData(){
		if(UserContext.MyUserName.length()>0){
			app42Canvas.showProgress();
			new Thread(new Runnable() {
				public void run() {
					loadData();
				}
			}).start();
		}
	}
	private void loadData(){
		if(UserContext.MyUserName.length()>0){
			isPreFb = false;
			App42ServiceHandler.instance().saveScore(UserContext.MyUserName, score, App42Canvas.currentGameName, this);
		}
	}
	public void loadFacebookProfile() {
		if(RMS.getInstance().token.length()>0){//if already logged in
			isPreFb=false;
			tLoadData();
		}else{
			isPreFb=true;
		}
	}
	private void tLoadData(){
		app42Canvas.showProgress();
		new Thread(new Runnable() {
			public void run() {
				loadRank();
			}
		}).start();
	}
	private void loadRank(){
		try{
		if(UserContext.MyUserName.trim().length()==0){
			App42ServiceHandler.instance().fetchFacebookProfile(this);
		}
		}catch(Exception e){
			app42Canvas.stopProgress();
			app42Canvas.refresh();
			RMS.getInstance().writeInFacebookDB("");
			App42ServiceHandler.oauthToken="";
			e.printStackTrace();
		}
		
	}
	public void setData(long score){
		this.score=score;
		preFBTextList.setText("score", "Yor Scored: "+score);
		postFBTextList.setText("score", "Score:"+score);
	}
	private void createPostFBList(){
		int noOfElement = 5;
		int gap = 30 ;
		int temp  = HEIGHT/2-(noOfElement*gap)/2 + gap/2;
		postFBTextList.addElement(-2, "game", new Text("",0X000000,0X000000,StringDB.color_list_top,StringDB.color_list_top,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp = temp + ImageLoader.gameComp.getHeight();
		postFBTextList.addElement(-2, "score", new Text("Score:",0XFFFFFF,0XFFFFFF,StringDB.color_list_top,StringDB.color_list_top,StringDB.FONT_SMALL,WIDTH/2,temp-StringDB.FONT_SMALL.getHeight()/2,false));
		postFBTextList.addElement(-2, "rank", new Text("Rank:",0XFFFFFF,0XFFFFFF,StringDB.color_list_top,StringDB.color_list_top,StringDB.FONT_SMALL,WIDTH/2,temp+StringDB.FONT_SMALL.getHeight()/2,false));
		temp=temp+StringDB.FONT_SMALL.getHeight()*2;
		postFBTextList.addElement(0, "leaderboard", new Text("Leader Board",0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,WIDTH/2,temp,false));
		temp=temp+gap*2;
		postFBTextList.addElement(1, "close", new Text("Back to Game",0X656262,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL_BOLD,WIDTH/2,temp,false));
		postFBTextList.createList();
		postFBTextList.setBgImage("game", ImageLoader.gameComp, ImageLoader.gameComp);
		postFBTextList.setBgImage("leaderboard", app.ImageLoader.select, app.ImageLoader.unselect);
		postFBTextList.setBgImage("close", app.ImageLoader.select, app.ImageLoader.unselect);
	}
	

	public void draw(Graphics g) {
		if(isPreFb){
			preFBTextList.draw(g);
		}else{
			postFBTextList.draw(g);
		}
	}

	public void pointerPressed(int x, int y) {
		if(isPreFb){
			preFBTextList.pointerPressed(x, y);
		}else{
			postFBTextList.pointerPressed(x, y);
		}
	}

	public void pointerReleased(int x, int y) {
		if(isPreFb){
			handleOk(preFBTextList.getSelected());
			preFBTextList.pointerReleased();
		}else{
			handleOk(postFBTextList.getSelected());
			postFBTextList.pointerReleased();
		}
	}

	public void pointerDragged(int x, int y) {
		
	}

	public void keyPressed(int keyCode) {
		if(isPreFb){
			preFBTextList.keyPressed(keyCode);
			if(keyCode== utility.Constants.OK_KEY || keyCode == utility.Constants.FIVE_KEY){
				if(preFBTextList.getSelected().length()>0){
					handleOk(preFBTextList.getSelected());
				}
			}
		}else{
			postFBTextList.keyPressed(keyCode);
			if(keyCode== utility.Constants.OK_KEY || keyCode == utility.Constants.FIVE_KEY){
				if(postFBTextList.getSelected().length()>0){
					handleOk(postFBTextList.getSelected());
				}
			}
		}
	}

	public void handleOk(String str_selected) {
		if(str_selected.equals("fb")){
			if(RMS.getInstance().token.length()==0){
				App42ServiceHandler.instance().launchFacebook(this);
			}
		}else if(str_selected.equals("leaderboard")){
			app42Canvas.showProgress();
			app42Canvas.leaderBoard.loadData();
			app42Canvas.changeStatus(App42Canvas.STATUS_LEADER_BOARD);
		}else if(str_selected.equals("close")){
			app42Canvas.backToCallingDisplayable();
		}
		
	}
	public void onSubmitScore(int result) {
		if(result == ZapakResultHandler.SUCCESS){
			App42ServiceHandler.instance().getUserGameRanking(UserContext.MyUserName, this);
		}
		else{
			app42Canvas.stopProgress();
			showAlert("Request not completed");
		}
		
	}
	public void onGetTopRankings(Vector scoreDataList) {
		
	}
	public void onGetLastActivityOfUser(String gameName, String points,
			String time, int reqID) {
		
	}
	public void onGetTopRankingsToday(Vector scoreDataList) {
		
	}
	public void onGetTopRankingsInGroup(Vector scoreDataList) {
		
	}
	public void onGetUserGameRanking(int ranking) {
		app42Canvas.stopProgress();
		rank = ranking;
		postFBTextList.setText("rank","Rank: "+ranking+"");
		app42Canvas.refresh();
	}
	private void showAlert(String str){
		Alert alert =new Alert("Note",str,null,AlertType.INFO);
		app42Canvas.getDisplay().setCurrent(alert);
	}
	public void onGetFacebookUserProfile(Social social) {
		UserContext.MyUserName=social.getFacebookProfile().getId();
		UserContext.MyDisplayName=social.getFacebookProfile().getName();
		UserContext.MyPicUrl=social.getFacebookProfile().getPicture();
		App42ServiceHandler.instance().storeUserProfile();
		RMS.getInstance().writeInUserprofileDB(UserContext.MyUserName, UserContext.MyDisplayName, UserContext.MyPicUrl);
		asyncLoadData();
	}
	
	public void onGetUserZapperRewarGames(Vector rewardList) {
		
	}
	public void onGetFriendsRewards(Vector rewardList) {
		
	}

	public void onGetFacebookAccessToken(String token) {
		if (token!=null && token.length()>0) {
			loadFacebookProfile();
		}
	}

}
