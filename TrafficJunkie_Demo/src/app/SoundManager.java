package app;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public class SoundManager {
	public SoundManager() {
		loadSound();
	}
	private Player playerHit;// hit
	private Player playerWin;// win
	
	public void playSound(byte type){
		try {
			if(type == 1){
				if(playerHit!=null){
					playerHit.prefetch();
					playerHit.realize();
					playerHit.start();
	            }
			}else if(type == 2 ){
				if(playerWin!=null){
					playerWin.prefetch();
					playerWin.realize();
					playerWin.start();
	            }
			}
			
      } catch (Exception e) {
    	  e.printStackTrace();
      }
	}
	private void loadSound(){
		  try {
            playerHit = Manager.createPlayer(getClass().getResourceAsStream("/Computererror.wav"), "audio/x-wav");
            playerWin = Manager.createPlayer(getClass().getResourceAsStream("/bottle.wav"), "audio/x-wav");
            playerHit.setLoopCount(1);
            playerWin.setLoopCount(1);
            System.out.println("playerHit Loaded  "+playerHit);
	      } catch (Exception e) {
	    	  System.out.println("111111111111111111");
	    	  e.printStackTrace();
	      }
	}
	public void deallocateSound(){
		if(playerWin!=null){
			if(playerWin.getState()!=Player.CLOSED){
				try {
					playerWin.stop();
					playerWin.deallocate();
					playerWin =null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		if(playerHit!=null){
			if(playerHit.getState()!=Player.CLOSED){
				try {
					playerHit.stop();
					playerHit.deallocate();
					playerHit = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
