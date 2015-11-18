App42-Traffic-Junkie
===========================

# Runnnig Sample

This is a sample J2ME social gaming app made using App42 backened platform. It uses social, user, storage and gaming APIs of App42 platform. Here are the few easy steps to run this sample app.


1. [Register] (https://apphq.shephertz.com/register) with App42 platform
2. Create an app once you are on Quick-start page after registration.
3. Goto dashboard and create a new game TrafficJunkie (Click on Gaming->Games->Add Game->)
4. If you are already registered, login to [AppHQ] (http://apphq.shephertz.com) console and create an app from App Manager tab and do step #3 to create a game.
5. Download the eclipse project from this repo and import it in the same.
6. Open App42ServiceHandler.java in sample app and give the value of app42APIkey app42SecretKey that you have received in step 2 or 4
7. You can also modify your fbAppId and fbAppSecret variable to pass your own facebook app credentials. Read our [blog post] (http://blogs.shephertz.com/2013/05/13/doing-facebook-oauth-wit-nokia-s40-devices-using-app42-platform/) for more help about creating facebook app and its configuration.
7. Build and Run 



# Design Details:

__Initialize Services:__

Initialization has been done in App42ServiceHandler.java

```
      try{
	  	ServiceAPI sp = new ServiceAPI(app42APIkey,app42SecretKey);
	    	this.scoreBoardService = sp.buildScoreBoardService();
	    	this.storageService = sp.buildStorageService();
	    	this.socialService = sp.buildSocialService();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
```

__Get Facebook Access Token:__

This call is in LeaderBoard.java/PostGamePlay.java 

```
  String token = socialService.doFBOAuthAndGetToken(startMIDlet, fbAppId,null);
```  
  
__Fetch Facebook Profile From Access Token:__

This call is in LeaderBoard.java/PostGamePlay.java

```
  Social social = socialService.getFacebookProfile(oauthToken);
  UserContext.MyUserName = social.getFacebookProfile().getId();
  UserContext.MyDisplayName = social.getFacebookProfile().getName();
  UserContext.MyPicUrl = social.getFacebookProfile().getPicture();
```  
  
__Store User Profile:__ 
This is to srore User Profile(Facebook Id, Display Name, PicURL etc.) 
By saving user profile we can retrive display name for facebook id(Used to create friends leaderboard)

This call is done in App42ServiceHandler.java

```
    JSONObject userProfile = new JSONObject();
    try {
        userProfile.put("UserName", UserContext.MyUserName);
        userProfile.put("DisplayName", UserContext.MyDisplayName);
        userProfile.put("PicUrl", UserContext.MyPicUrl);
        storageService.insertJSONDocument(Constants.ZAPAK_DB_NAME, Constants.USER_PROFILE_COLLECTION, userProfile.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }
```
__Save Score:__

This call is in PostGamePlay.java

```
   Game game = scoreBoardService.saveUserScore(gameName, name, score);
```

__Get LeaderBoard:__ 

This call is in LeaderBoard.java 

```
   Game game = scoreBoardService.getTopNRankers(currentGameName, new Integer(20));
```

__Get Facebook Friends:__ 

This call is in LeaderBoard.java 

```
    final Social social = socialService.getFacebookFriendsFromAccessToken(oauthToken);
    social.getFriendList();
```

__For friends Leaderboard:__ 

```
   Game game = scoreBoardService.getTopNRankersByGroup(currentGameName, group);
```   
   here group is a vector for facebook ids of your friends. Now we retive display name 
   for facebook ids from Storage Service that we used earlier.
  

