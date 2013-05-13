package utility.leaderboard;

import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Graphics;
import org.json.jme.JSONObject;
import utility.StringDB;
import utility.TextList;
import utility.Text;
import utility.leaderboard.App42ServiceHandler.App42LeaderBoardListener;
import utility.leaderboard.App42ServiceHandler.FacebookFriendListListener;
import app.ImageLoader;

import com.shephertz.app42.paas.sdk.jme.social.Social.Friends;

public class LeaderBoard implements App42LeaderBoardListener,FacebookFriendListListener{
	
	private int WIDTH;
	private int HEIGHT;
	private TextList leaderBoardTextList;
	private LeaderBoardTouchList leaderBoardList;
	private int temp;
	private App42Canvas myCanvas;
	private Vector global;
	private Vector friends;
	private String ranks[] ;
	private String names[] ;
	private String points[] ;
	private int selectedIndex=-1;
	private int list_height=180;
	private int obj_height=20;
	
	public LeaderBoard(App42Canvas myCanvas,int width,int height) {
		this.WIDTH=width;
		this.HEIGHT=height;
		this.myCanvas=myCanvas;
		leaderBoardTextList = new TextList();
		leaderBoardList = new LeaderBoardTouchList(leaderBoardTextList);
		createLeaderBoard();
		leaderBoardTextList.setListHandle(true);
	}
	private void createLeaderBoard(){
		int startY=App42Canvas.startY;
		int temp=startY;
		temp=temp+StringDB.FONT_SMALL.getHeight();
		leaderBoardTextList.addElement(0, "friends", new Text(StringDB.frinds,0XFFFFFF,0XFFFFFF,0XE4513D,0XCF3A26,StringDB.FONT_SMALL,WIDTH/4,temp,true));
		leaderBoardTextList.addElement(1, "global", new Text(StringDB.global,0XFFFFFF,0XFFFFFF,0XE4513D,0XCF3A26,StringDB.FONT_SMALL,(WIDTH*3)/4,temp,true));
		temp=temp+StringDB.FONT_SMALL.getHeight()+5;
		leaderBoardTextList.addElement(-2, "top_score", new Text(StringDB.top_scores,0X000000,0X000000,0XA4A9F7,0X8C93F7,StringDB.FONT_SMALL,(WIDTH)/2,temp,false));
		temp=temp+StringDB.FONT_MEDIUM.getHeight();
		this.temp=temp+10;
		temp=temp+list_height;
		leaderBoardTextList.addElement(2, "close", new Text(StringDB.close_back,0X656262,0X000000,0XA4A9F7,0X8C93F7, StringDB.FONT_SMALL_BOLD,WIDTH/2, App42Canvas.startY+App42Canvas.SCREEN_HEIGHT,true));
		leaderBoardTextList.createList();
		leaderBoardTextList.setBgImage("close", ImageLoader.select, ImageLoader.unselect);
	}
	public void loadData() {
		myCanvas.showProgress();
		tloadData();
	}
	private void tloadData(){
		new Thread(new Runnable() {
			public void run() {
				loadAllListData();
			}
		}).start();
	}
	private void loadAllListData(){
		App42ServiceHandler.instance().getTopRankings(App42Canvas.currentGameName, this);
		App42ServiceHandler.instance().getMyFriends(this);
	}
	private void createList(Vector scoreItemList){
		ranks =new String [scoreItemList.size()];
		names =new String [scoreItemList.size()];
		points =new String [scoreItemList.size()];
		for(int i=0;i<scoreItemList.size();i++){
			JSONObject item=(JSONObject)scoreItemList.elementAt(i);
			ranks[i]=String.valueOf(i+1);
			names[i]=item.opt("DisplayName").toString();
			points[i]=item.opt("Score").toString();
			if(points[i]!=null && points[i].indexOf('.')!=-1 ){
				points[i]=points[i].substring(0, points[i].indexOf('.'));
			}
		}
		leaderBoardList.createList(WIDTH, HEIGHT, temp, temp+list_height, StringDB.color_list_select, StringDB.color_list_unselect, ranks, names, points, App42Canvas.SCREEN_WIDTH, obj_height, false);
	}
	public void draw(Graphics g) {
		leaderBoardTextList.draw(g);
		leaderBoardList.draw(g);
	}

	public void pointerPressed(int x, int y) {
		leaderBoardTextList.pointerPressed(x, y);
		leaderBoardList.pointerPressed(x, y);
	}

	public void pointerReleased(int x, int y) {
		handleOk(leaderBoardTextList.getSelected());
		handleOkList();
		leaderBoardTextList.pointerReleased();
		leaderBoardList.pointerReleased(x, y);
	}

	public void pointerDragged(int x, int y) {
		leaderBoardList.pointerDragged(x, y);
	}

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case utility.Constants.UP_KEY:
		case utility.Constants.TWO_KEY:
			if(leaderBoardList.getSelectedId()>=0){
				leaderBoardList.processUpKey();
				return;
			}
			if( leaderBoardTextList.getMinTouch()==false){
				leaderBoardList.setSelectedId(-1);
				leaderBoardTextList.processUpKey();
			}
			if(leaderBoardTextList.getMinTouch()){
				leaderBoardTextList.setSelectedID(-1);
				leaderBoardList.processUpKey();
			}
		break;
		case utility.Constants.DOWN_KEY:
		case utility.Constants.EIGHT_KEY:
			if(leaderBoardTextList.getMaxTouch()==false){
				leaderBoardList.setSelectedId(-1);
				leaderBoardTextList.processDownKey();
			}
			if(leaderBoardTextList.getMaxTouch()){
				leaderBoardTextList.setSelectedID(-1);
				leaderBoardList.processDownKey();
			}
		break;
		case utility.Constants.OK_KEY:
		case utility.Constants.FIVE_KEY:
			if( leaderBoardTextList.getSelected().length()>0 ){
				handleOk(leaderBoardTextList.getSelected());
			}else if( leaderBoardList.getSelectedIndex()>=0 ){
				handleOkList();
			}
		break;
		}
	}

	public void handleOk(String str_selected) {
		if(str_selected.equals("friends")){
			if(friends!=null && friends.size()>0){
				createList(friends);
			}
		}else if(str_selected.equals("global")){
			if(global!=null && global.size()>0){
				createList(global);
			}
		}else if(str_selected.equals("close")){
			myCanvas.backToCallingDisplayable();
		}
	}
	private void handleOkList(){
		if(leaderBoardList.getSelectedIndex() >= 0){
			selectedIndex = leaderBoardList.getSelectedIndex();
			showDescription(ranks[selectedIndex], names[selectedIndex], points[selectedIndex]);
		}
	}
	public void onGetTopRankings(Vector scoreDataList) {
		this.global=scoreDataList;
		createList(scoreDataList);
	}
	public void onGetLastActivityOfUser(String gameName, String points,
			String time, int reqID) {
		
		
	}
	public void onGetTopRankingsToday(Vector scoreDataList) {
		
		
	}
	public void onGetTopRankingsInGroup(Vector scoreDataList) {
		this.friends=scoreDataList;
		myCanvas.stopProgress();
		myCanvas.refresh();
	}
	public void onGetUserGameRanking(int ranking) {
		
	}
	public void onGetFacebookFriendsList(Vector itemList) {
		
		if( itemList!=null ){
			Vector vId= new Vector();
			for(int i=0;i<itemList.size();i++){
				Friends friendObj = (Friends)itemList.elementAt(i);
				vId.addElement(friendObj.getId().toString());
			}
			if(UserContext.MyUserName.length()>0){
				vId.addElement(UserContext.MyUserName);
			}
			App42ServiceHandler.instance().getTopRankingsInGroup(App42Canvas.currentGameName, vId, this);
		}
	}
	
	private void showDescription(String rank,String name,String points){
		String strMsg = "Rank: \n"+rank+"\nName: \n"+name+"\nPoints: \n"+points;
		Alert alert = new Alert("Note:",strMsg,null,AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		myCanvas.getDisplay().setCurrent(alert);
	}

}
