package utility.leaderboard;

import javax.microedition.lcdui.Image;

public class ImageLoader {
	public static Image facebookU;
	public static Image facebookS;
	public static Image score_clout;
	public static Image gameCompBG;
	public static Image gameComp;
	public static Image gameCompSeprator;
	public ImageLoader() {
		try {
			facebookU = Image.createImage("/zapper/facebook_login_btn.png");
			facebookS = Image.createImage("/zapper/facebook_s.png");
			score_clout = Image.createImage("/zapper/full_score_clout_big.png");
			gameCompBG = Image.createImage("/zapper/main_doted_bg.png");
			gameComp = Image.createImage("/zapper/game_over_message_btn.png");
			gameCompSeprator = Image.createImage("/zapper/vertical_seprator.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
