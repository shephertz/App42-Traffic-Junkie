package utility.leaderboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;

public class RMS {
	
	private final String rmsFBString="DB_FB_DETAIL_Traffic_Junkie_1221";//fb detail string database 
	private final String rmsUserProfile="DB_Profile_Traffic_Junkie_1221";//UserProfile
	
	public static String MyUserName = "";
	public static String MyDisplayName = "";
	public static String MyPicUrl = "";
	
	public String token="";
	private static RMS rms;
	
	private RMS() {
		if(isFacebookDBAvailable()){
			readFacebookData();
		}else{
			writeInFacebookDB("");
		}
		if(isUserprofileDBAvailable()){
			readUserprofileData();
		}else{
			writeInUserprofileDB("", "", "");
		}
	}
	public void startRMS(){
		
	}
	public static RMS getInstance(){
		if(rms==null){
			rms=new RMS();
		}
		return rms;
	}
	
	
	
	
	public boolean isFacebookDBAvailable(){
		boolean status=false;
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsFBString, true);
			if(recordStore.getNumRecords()>0){
				status=true;
			}else{
				status=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}

	public boolean isUserprofileDBAvailable(){
		boolean status=false;
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsUserProfile, true);
			if(recordStore.getNumRecords()>0){
				status=true;
			}else{
				status=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	public void writeInFacebookDB(String token){
		this.token=token;
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsFBString, true);
			ByteArrayOutputStream bos =new ByteArrayOutputStream();
			DataOutputStream dos =new DataOutputStream(bos);
			dos.writeUTF(token);
			bos.flush();
			if(recordStore.getNumRecords()>0){
				recordStore.setRecord(1, bos.toByteArray(), 0, bos.toByteArray().length);
			}else{
				recordStore.addRecord(bos.toByteArray(), 0, bos.toByteArray().length);
			}
			bos.close();
			dos.close();
			bos=null;
			dos=null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void writeInUserprofileDB(String userName, String displayName, String picURL){
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsUserProfile, true);
			ByteArrayOutputStream bos =new ByteArrayOutputStream();
			DataOutputStream dos =new DataOutputStream(bos);
			dos.writeUTF(userName);
			dos.writeUTF(displayName);
			dos.writeUTF(picURL);
			bos.flush();
			if(recordStore.getNumRecords()>0){
				recordStore.setRecord(1, bos.toByteArray(), 0, bos.toByteArray().length);
			}else{
				recordStore.addRecord(bos.toByteArray(), 0, bos.toByteArray().length);
			}
			bos.close();
			dos.close();
			bos=null;
			dos=null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readFacebookData(){
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsFBString, true);
			if(recordStore.getNumRecords()>0){
				byte[] data=recordStore.getRecord(1);
				ByteArrayInputStream bin = new ByteArrayInputStream(data);
				DataInputStream din =new DataInputStream(bin);
				token=din.readUTF();
				din.close();
				bin.close();
				din=null;
				din=null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void readUserprofileData(){
		try{
			RecordStore recordStore=RecordStore.openRecordStore(rmsUserProfile, true);
			if(recordStore.getNumRecords()>0){
				byte[] data=recordStore.getRecord(1);
				ByteArrayInputStream bin = new ByteArrayInputStream(data);
				DataInputStream din =new DataInputStream(bin);
				UserContext.MyUserName = MyUserName = din.readUTF();
				UserContext.MyDisplayName = MyDisplayName = din.readUTF();
				UserContext.MyPicUrl = MyPicUrl =din.readUTF();
				din.close();
				bin.close();
				din=null;
				din=null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void deleteDB(String dbName){
		try{
			RecordStore.deleteRecordStore(dbName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
