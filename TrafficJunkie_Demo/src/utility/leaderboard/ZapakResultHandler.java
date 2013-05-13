package utility.leaderboard;

public interface ZapakResultHandler {

	public static final int SUCCESS = 0;
	public static final int AUTH_ERROR = 1;
	public static final int CONNECTION_ERROR = 2;
	public static final int UNKNOWN_ERROR = 3;
	public void onSubmitScore(int result);
}
