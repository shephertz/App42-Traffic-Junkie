package utility.leaderboard;


import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;

import org.json.jme.JSONException;
import org.json.jme.JSONObject;

import com.shephertz.app42.paas.sdk.jme.App42Log;
import com.shephertz.app42.paas.sdk.jme.App42NotFoundException;
import com.shephertz.app42.paas.sdk.jme.ServiceAPI;
import com.shephertz.app42.paas.sdk.jme.customcode.CustomCodeService;
import com.shephertz.app42.paas.sdk.jme.game.Game;
import com.shephertz.app42.paas.sdk.jme.game.Game.Score;
import com.shephertz.app42.paas.sdk.jme.game.RewardService;
import com.shephertz.app42.paas.sdk.jme.game.ScoreBoardService;
import com.shephertz.app42.paas.sdk.jme.social.Social;
import com.shephertz.app42.paas.sdk.jme.social.SocialService;
import com.shephertz.app42.paas.sdk.jme.storage.Query;
import com.shephertz.app42.paas.sdk.jme.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.jme.storage.QueryBuilder.Operator;
import com.shephertz.app42.paas.sdk.jme.storage.Storage;
import com.shephertz.app42.paas.sdk.jme.storage.StorageService;


public class App42ServiceHandler {
	
	private String app42APIkey = "70a297a94b33916b677e68f3b0fb1b1dc2e83f9899ee2f98123246e70d27c27d";
	private String app42SecretKey= "b44c52d95f5872f286a4f774560b954138826eaa7a997b7bf77c30588e135421";
	
	private String fbAppId = "145726228932172";
	private String fbAppSecret = "48cf90a9ea0ee5da8a66fed8c97bcf0b";
	
	private ScoreBoardService scoreBoardService = null;
	private StorageService storageService = null;
	private SocialService socialService = null;
	
	private MIDlet startMIDlet;	
	private static App42ServiceHandler mInstance = null;

	public static  String oauthToken = "";
	public static App42ServiceHandler instance()
	{
		if(mInstance == null)
		{
//			App42Log.setDebug(true);
			mInstance = new App42ServiceHandler();
		}
		return mInstance;
	}
	
	private App42ServiceHandler(){
		try{
			ServiceAPI sp = new ServiceAPI(app42APIkey,app42SecretKey);
	    	this.scoreBoardService = sp.buildScoreBoardService();
	    	this.storageService = sp.buildStorageService();
	    	this.socialService = sp.buildSocialService();
    	}catch(Exception e){// invalid credential
    		System.out.println("========== App42ServiceHandler =========");
    		e.printStackTrace();
    	}
	}	
	public void setMidlet(MIDlet midlet){
		this.startMIDlet=midlet;
	}
	public void setToken(String str){
		oauthToken=str;
	}
	public void saveScore(final String name, final long score, final String gameName, final ZapakResultHandler callBack){

		try{
			Game game = scoreBoardService.saveUserScore(gameName, name, score);
			System.out.println("game.getScoreList(): >>>"+game.getScoreList().toString());
			for(int i=0;i<game.getScoreList().size();i++){
				Score score1 = (Score)game.getScoreList().elementAt(i);
				System.out.println("score1.getUserName(): "+score1.getUserName());
			}
			if(callBack != null){
				callBack.onSubmitScore(ZapakResultHandler.SUCCESS);
			}
		}  
		catch (Exception ex) {
			ex.printStackTrace();
			if(callBack != null){
				callBack.onSubmitScore(ZapakResultHandler.UNKNOWN_ERROR);
			}
		}		
	}
	
	public void getTopRankingsToday(final String currentGameName, final App42LeaderBoardListener callBack) {
		
        new Thread(){
            public void run() {
                try {  
                	Date today = new Date();
                	Date yesterday = new Date(today.getTime() - 1000*60*60*24);
                	final Game game = scoreBoardService.getTopNRankers(currentGameName, today, yesterday, new Integer(20));            
                	Vector topScoresOfTheDay = game.getScoreList();
                	final Vector scoreDataList = buildScoreDataList(topScoresOfTheDay);
            		if(callBack != null){
            			callBack.onGetTopRankingsToday(scoreDataList);                 
            		}
                }  
            	catch (Exception ex) {
            		System.out.println(ex.toString());    	
        			callBack.onGetTopRankingsToday(null);
            	}
            }
        }.start();
	}
	
	public void getTopRankings(final String currentGameName, final App42LeaderBoardListener callBack) {
        try {  
        	final Game game = scoreBoardService.getTopNRankers(currentGameName, new Integer(20));
        	Vector topScores = game.getScoreList();
        	final Vector scoreDataList = buildScoreDataList(topScores);
    		if(callBack != null){
    			callBack.onGetTopRankings(scoreDataList);                 
    		}
        }  
    	catch (Exception ex) {
    		System.out.println(ex.toString());    	
			callBack.onGetTopRankings(null);
    	}
	}

	public void getTopRankingsInGroup(final String currentGameName, final Vector group, final App42LeaderBoardListener callBack) {
        try {      	
        	final Game game = scoreBoardService.getTopNRankersByGroup(currentGameName, group);
        	Vector topScores = game.getScoreList();
        	final Vector scoreDataList = buildScoreDataList(topScores);
        	callBack.onGetTopRankingsInGroup(scoreDataList);
        }  
    	catch (Exception ex) {
    		System.out.println(ex.toString());    	
			callBack.onGetTopRankingsInGroup(null);
    	}
    }
        
	
	
	public void getUserGameRanking(final String username, final App42LeaderBoardListener callBack) {
        try {  
        	Game userRanking = scoreBoardService.getUserRanking(App42Canvas.currentGameName, username);
        	Score score=(Score)userRanking.getScoreList().elementAt(0);
        	final String rank = score.rank;
    		if(callBack != null){
    			callBack.onGetUserGameRanking(Integer.parseInt(rank));                
    		}
        }  
    	catch (Exception ex) {
    		System.out.println(ex.toString());  
    		callBack.onGetUserGameRanking(0);
    	}
	}	
	
	
	protected Vector buildScoreDataList(Vector topScores) throws JSONException {
		// build an array of json so that each has DisplayName and Score keys
		
		Vector usernames = new Vector();
		Vector queries = new Vector();
		for(int i=0; i<topScores.size(); i++){
			Score score = (Score)topScores.elementAt(i);
			usernames.addElement(score.getUserName().toString());
			Query q = QueryBuilder.build("UserName",score.getUserName(), Operator.EQUALS);
			queries.addElement(q);
		}
		
		// build a query for user profiles
		Query finalQuery = (Query)queries.elementAt(0);
		for(int i=1; i<queries.size(); i++){
			Query q = (Query)queries.elementAt(i);
			finalQuery = QueryBuilder.compoundOperator(q, Operator.OR, finalQuery);
		}
		
		Storage storageObj = storageService.findDocumentsByQuery(Constants.ZAPAK_DB_NAME, Constants.USER_PROFILE_COLLECTION, finalQuery);
		System.out.println("storageObj: "+storageObj);
		Vector list = storageObj.getJsonDocList();//ArrayList<Storage.JSONDocument>
		
		// construct the profile map
		
		Hashtable profileDocMap = new Hashtable();
		for(int i=0; i<list.size(); i++){
			Storage.JSONDocument doc = (Storage.JSONDocument)list.elementAt(i);
			JSONObject obj = new JSONObject(doc.getJsonDoc());
			profileDocMap.put(obj.optString("UserName"), obj);
		}
		
		// Now build the final JSONObject ArrayList to be returned
		Vector retList = new Vector();
		for(int i=0; i<topScores.size(); i++){
			Score score = (Score)topScores.elementAt(i);
			String username = score.getUserName();
			String points = (score.getValue()).toString();
			JSONObject profile = (JSONObject)profileDocMap.get(username);
			if(profile == null){
				continue;
			}
			JSONObject retListObj = new JSONObject(profile.toString());
			retListObj.put("Score", points);
			retList.addElement(retListObj);
		}
		return retList;
	}
	
	public void launchFacebook(final FacebookAccessTokenListener callback){
		new Thread(new Runnable() {
			public void run() {
				startFB(callback);
			}
		}).start();
	}
    public void startFB(FacebookAccessTokenListener callback){
		try{
			String token = socialService.doFBOAuthAndGetToken(startMIDlet, fbAppId,null);
			System.out.println( "  Response : " + token);
			String AccessToken = 	socialService.getAccessTokenFromCode(token, fbAppId, fbAppSecret);
			System.out.println( " Token : " + AccessToken);
			oauthToken = AccessToken;
			if(oauthToken.length()>0){
				RMS.getInstance().writeInFacebookDB(AccessToken);
			}
			callback.onGetFacebookAccessToken(oauthToken);
		}catch(Exception e){
			callback.onGetFacebookAccessToken(null);
		}
	}
    public void getMyFriends(final FacebookFriendListListener callBack)// getting friends from access token
    {
    	if(oauthToken.length() <= 0){
    		return ;
    	}
    	final Social social = socialService.getFacebookFriendsFromAccessToken(oauthToken);
    	System.out.println(" social "+social);
    	callBack.onGetFacebookFriendsList(social.getFriendList());
    }
    
    public void fetchFacebookProfile(final FacebookUserProfileListener callBack){
    	// fetch display name, profile pic url and id
    	// and fill UserContext with the values.
    	if(oauthToken.length() <= 0){
    		return ;
    	}
    	System.out.println("oauthToken  "+oauthToken);
		final Social social = socialService.getFacebookProfile(oauthToken);
		callBack.onGetFacebookUserProfile(social);
		
    }
	// callback interfaces
	static interface App42LeaderBoardListener {
		public void onGetTopRankings(Vector scoreDataList);

		public void onGetLastActivityOfUser(String gameName, String points, String time, int reqID);

		public void onGetTopRankingsToday(Vector scoreDataList);

		public void onGetTopRankingsInGroup(Vector scoreDataList);
		
		public void onGetUserGameRanking(int ranking);
	}
	
	static interface StorageListener {
		public void onSubmitGiftRedeemRequest(boolean success);
		public void onGetGiftsRedeemed(Vector list);
	}
	// added by saurav
	static interface FacebookFriendListListener {
		public void onGetFacebookFriendsList(Vector itemList);
	}
	static interface FacebookUserProfileListener {
		public void onGetFacebookUserProfile(Social social);
	}
	public void storeUserProfile() {
        new Thread(){
            public void run() {
                // check if it already exists
                try{
                    if(UserContext.MyUserName.length() <= 0){
                        throw new App42NotFoundException();
                    }
                    storageService.findDocumentByKeyValue(Constants.ZAPAK_DB_NAME, Constants.USER_PROFILE_COLLECTION, "UserName", UserContext.MyUserName);
                }
                catch(App42NotFoundException e){
                    JSONObject userProfile = new JSONObject();
                    try {
                        userProfile.put("UserName", UserContext.MyUserName);
                        userProfile.put("DisplayName", UserContext.MyDisplayName);
                        userProfile.put("PicUrl", UserContext.MyPicUrl);
                        storageService.insertJSONDocument(Constants.ZAPAK_DB_NAME, Constants.USER_PROFILE_COLLECTION, userProfile.toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }    
                }    
            }
        }.start();
    }
	static interface FacebookAccessTokenListener {
        public void onGetFacebookAccessToken(String token);
    }
}
