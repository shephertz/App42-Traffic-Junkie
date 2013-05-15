App42-Traffic-Junkie
===========================

# Runnnig Sample

This is a sample J2ME social gaming app made using App42 backened platform. It uses social, user, storage and gaming APIs of App42 platform. Here are the few easy steps to run this sample app.


1. [Register] (https://apphq.shephertz.com/register) with App42 platform
2. Create an app once you are on Quickstart page after registeration.
3. Goto dashboard and create a new game TrafficJunkie (Click on Business service manager->game service->add game->)
4. If you are already registered, login to [AppHQ] (http://apphq.shephertz.com) console and create an app from App Manager tab and do step #3 to create a game.
5. Download the eclipse project from this repo and import it in the same.
6. Open App42ServiceHandler.java in sample app and give the value of app42APIkey app42SecretKey that you have received in step 2 or 4
7. You can also modify your fbAppId and fbAppSecret variable to pass your own facebook app credentials. Read our blog post for more help about creating facebook app and its configuration.
7. Build and Run 



# Design Details:

1. Initilize Services

      try{
  		  ServiceAPI sp = new ServiceAPI(app42APIkey,app42SecretKey);
	    	this.scoreBoardService = sp.buildScoreBoardService();
	    	this.storageService = sp.buildStorageService();
	    	this.socialService = sp.buildSocialService();
    	}catch(Exception e){
    		e.printStackTrace();
    	}

2. Get Facebook Access Token:

  String token = socialService.doFBOAuthAndGetToken(startMIDlet, fbAppId,null);
  
  
3. Fetch Facebook Profile From Access Token:

  Social social = socialService.getFacebookProfile(oauthToken);
  UserContext.MyUserName = social.getFacebookProfile().getId();
  UserContext.MyDisplayName = social.getFacebookProfile().getName();
  UserContext.MyPicUrl = social.getFacebookProfile().getPicture();
  
4. Store User Profile:

   JSONObject userProfile = new JSONObject();
    try {
        userProfile.put("UserName", UserContext.MyUserName);
        userProfile.put("DisplayName", UserContext.MyDisplayName);
        userProfile.put("PicUrl", UserContext.MyPicUrl);
        storageService.insertJSONDocument(Constants.ZAPAK_DB_NAME, Constants.USER_PROFILE_COLLECTION, userProfile.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }

5. Save Score:

   Game game = scoreBoardService.saveUserScore(gameName, name, score);
  
6. Get LeaderBoard: 
   
   Game game = scoreBoardService.getTopNRankers(currentGameName, new Integer(20));

7. Get Facebook Friends: 

    final Social social = socialService.getFacebookFriendsFromAccessToken(oauthToken);
    social.getFriendList();

8. For friends Leaderboard: 

   Game game = scoreBoardService.getTopNRankersByGroup(currentGameName, group);
   here group is a vector for facebook ids of your friends. 

