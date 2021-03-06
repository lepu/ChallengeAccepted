/*
 * THIS CLASS ACTS AS THE CONTROLLER FOR ALL THE FRAGMENTS
 * 
 *  BY INITIATING ALL THE VARIOUS OBJECTS AND VARIABLES NEEDED BY ALL THE FRAGMENTS HERE
 *  IT ALLOWS THE USE OF ALL THE OBJECTS AND VARIABLES IN ALL FRAGMENTS
 *  THIS AVOIDS LOADING THE SAME ARRAYS MORE THAN ONCE
 *  
 *  
 * 
 */

package fi.metropolia.challengedemo;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mike on 18/11/2014.
 */
public class MainActivity extends Activity implements AsyncResponse {

	/*
	 * HERE THE VARIOUS ASYNC TASKS THAT GET DATA FROM THE BACKEND ARE DECLARED
	 * AND INITIALISED
	 */
	AsyncGetChallenges asyncChallengeGrab = new AsyncGetChallenges();
	List<Challenges> challengesList;

	AsyncGetUsers asyncUserGrab = new AsyncGetUsers();
	List<Users> usersList;

	AsyncGetScoreboard asyncScoreGrab = new AsyncGetScoreboard();
	List<Users> scoreboard;

	TypedArray ta;

	String category, navFrom;

	int uId, challId, numIcons;
	int[] iconArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		/*
		 * THE BELOW GRABS ALL EXTRA ITEMS ADDED TO THE INTENT AND ALLOWS
		 * PRIMITIVES TO BE CARRIED FROM ACTIVITY TO ACTIVITY EASILY
		 */
		int userSelected;
		String welcomeMessage, uName;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				Log.d("NO USERS!", "NO USERS SELECTED!");
			} else {
				userSelected = extras.getInt("userID");
				welcomeMessage = extras.getString("nav");

				setuId(userSelected);
				setNavFrom(welcomeMessage);

			}
		} else {
			userSelected = (Integer) savedInstanceState
					.getSerializable("userID");
			welcomeMessage = (String) savedInstanceState.getSerializable("nav");

			setuId(userSelected);
			setNavFrom(welcomeMessage);

		}

		/*
		 * HERE THE ICONS ARE GATHERED FOR USE IN THE TABCHALLENGE FRAGMENT
		 */

		ta = getResources().obtainTypedArray(R.array.icons);
		numIcons = ta.length();

		iconArray = new int[numIcons];
		grabIcons();

		/*
		 * HERE THE VARIOUS ASYNC TASKS THAT GET DATA FROM THE BACKEND ARE
		 * ASSIGNED LISTENERS AND EXECUTED
		 */

		asyncChallengeGrab.listener = this;
		asyncChallengeGrab.execute("allChallenges");
		challengesList = new ArrayList<Challenges>();

		asyncUserGrab.listener = this;
		asyncUserGrab.execute("allUsers");
		usersList = new ArrayList<Users>();

		asyncScoreGrab.listener = this;
		asyncScoreGrab.execute("allUsers");
		scoreboard = new ArrayList<Users>();

		/*
		 * THE FRAGMENTS ARE CREATED HERE
		 */
		ActionBar actionBar = getActionBar();

		String label1 = getResources().getString(R.string.tab_lbl_welcome);

		Fragment TF_welcome = new TabWelcome();
		Tab welcomeTab = actionBar.newTab();
		welcomeTab.setText(label1);
		TListener t_welc = new TListener(TF_welcome);
		welcomeTab.setTabListener(t_welc);
		actionBar.addTab(welcomeTab);

		label1 = getResources().getString(R.string.tab_lbl_scoreboard);

		Fragment TF_score = new TabScoreboard();
		Tab scoreBoardTab = actionBar.newTab();
		scoreBoardTab.setText(label1);
		TListener t2 = new TListener(TF_score);
		scoreBoardTab.setTabListener(t2);
		actionBar.addTab(scoreBoardTab);

		label1 = getResources().getString(R.string.tab_lbl_my_challenges);

		Fragment TF_myC = new TabMyChallenges();
		Tab myChallengeTab = actionBar.newTab();
		myChallengeTab.setText(label1);
		TListener t1 = new TListener(TF_myC);
		myChallengeTab.setTabListener(t1);
		actionBar.addTab(myChallengeTab);

		label1 = getResources().getString(R.string.tab_lbl_create_challenge);

		Fragment TF_allChall = new TabChallenge();
		Tab challengeTab = actionBar.newTab();
		challengeTab.setText(label1);
		TListener t3 = new TListener(TF_allChall);
		challengeTab.setTabListener(t3);
		actionBar.addTab(challengeTab);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	/*
	 * BELOW FUNCTIONS ALLOW THE FRAGMENTS TO GET INFORMATION ABOUT A USER
	 */

	public Boolean checkIfChallenged(int userEntry) {
		Boolean isActive = usersList.get(userEntry).isChallengeActive();
		return isActive;
	}

	public Boolean checkIfNotified(int userEntry) {
		Boolean isNotified = usersList.get(userEntry).isNotified();
		return isNotified;
	}

	public int checkPoints(int userEntry) {
		int challengeId = usersList.get(userEntry).getCurrentChallenge();
		int challengePoints = challengesList.get(challengeId - 1).getPoints();

		return challengePoints;
	}

	/*
	 * THIS FUNCTIONS TELLS THE FRAGMENT TabMyChallenges WHICH ICON TO ASSIGN TO
	 * THE CHALLENGE THAT THE USER HAS BEEN ASSIGNED
	 */

	public int getIcon(int userEntry) {
		int challengeId = usersList.get(userEntry).getCurrentChallenge();

		// CHALLENGE ID IS STARTING AT 1, ENTRIES TO ARRAY START AT 0 SO MUST
		// DEDUCT 1 FROM FINDER ID
		String category = challengesList.get(challengeId - 1).getCategory();
		Log.d("CATEGORY FOR ICON", "CATEGORY FOUND: " + category);
		String[] categoryList = getResources().getStringArray(
				R.array.categories);
		int iconPath = 0;
		for (int i = 0; i < categoryList.length; i++) {
			if (categoryList[i].contentEquals(category)) {
				iconPath = iconArray[i];
				Log.d("category checkec TRUE", "CAT: " + categoryList[i]);
			} else {
				Log.d("category checkec FALSE", "CAT: " + categoryList[i]);
			}

		}

		return iconPath;
	}

	/*
	 * BELOW FUNCTIONS ALLOW THE FRAGMENTS TO GET INFORMATION ABOUT A CHALLENGE
	 */

	public String checkChallName(int userEntry) {
		int challengeId = usersList.get(userEntry).getCurrentChallenge();
		String challengeName = challengesList.get(challengeId - 1).getName();
		return challengeName;
	}

	public String checkChallDesc(int userEntry) {
		int challengeId = usersList.get(userEntry).getCurrentChallenge();
		String challengeDesc = challengesList.get(challengeId - 1)
				.getDescription();
		return challengeDesc;
	}

	/*
	 * HERE THE FUNCTIONS ALLOW CERTAIN ASYNC TASKS TO BE EXECUTED DEPENDING ON
	 * THE NEED
	 */
	public void completeChallenge(int userEntry) {
		int userToSend = usersList.get(userEntry).getId();
		new AsyncCompleteChallenge().execute(userToSend);

	}

	public void challengeResponse(int userEntry, int response) {
		int userToSend = usersList.get(userEntry).getId();
		new AsyncChallengeResponse().execute(userToSend, response);

	}

	// THIS FUNCTIONS FILLS THE ICON ARRAY WITH ID NUMBERS OF THE IMAGES IN
	// DRAWABLE

	public void grabIcons() {

		// FINDS ICONS

		// INNER LOOP GRABS ICONS NEEDED
		for (int j = 0; j < numIcons; j++) {
			iconArray[j] = ta.getResourceId(j, 0);

		}

	}

	public int[] getIconArray() {
		return iconArray;
	}

	public void setIconArray(int[] iconArray) {
		this.iconArray = iconArray;
	}

	public int getChallId() {
		return challId;
	}

	public void setChallId(int challId) {
		this.challId = challId;
	}

	public List<Challenges> getChallengesList() {
		return challengesList;
	}

	// FROM INTERFACE
	@Override
	public void challengeGrabber(List<Challenges> output) {

		challengesList = output;

	}

	// FROM INTERFACE
	@Override
	public void userGrabber(List<Users> output) {
		// TODO Auto-generated method stub

		usersList = output;

	}

	// FROM INTERFACE
	@Override
	public void scoreboardGrabber(List<Users> output) {
		// TODO Auto-generated method stub

		scoreboard = output;

	}

	@Override
	public void loadComplete() {
		// TODO Auto-generated method stub

	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNavFrom() {
		Log.d("NAVFROM IS: ", navFrom);
		return navFrom;
	}

	public void setNavFrom(String navFrom) {
		this.navFrom = navFrom;
	}

	public List<Users> getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(List<Users> scoreboard) {
		this.scoreboard = scoreboard;
	}

	public List<Users> getUsersList() {

		return usersList;
	}

}
