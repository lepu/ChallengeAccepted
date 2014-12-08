package fi.metropolia.challengedemo;


import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mike on 18/11/2014.
 */
public class TabChallenge extends Fragment implements OnItemSelectedListener  {
    LinearLayout myView, challengeLayout;
    TableLayout tableLayout;
    Button btnChallenge;
    
    
    
    int userToChallenge, challengeToSend;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = (LinearLayout) inflater.inflate(R.layout.tab_challenges, container, false);
        
        readUsersFromDataBase();
        readCategories();
        

        
        Spinner spinner = (Spinner) myView.findViewById(R.id.user_spinner);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, readUsersFromDataBase());
        
        
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        
        return myView;
    }
    
    
    
    public void issueChallenge(int uId, int challId, String challName, String challDesc){
        Intent intent = new Intent(getActivity(), ChallengeActivity.class);
        intent.putExtra("userID", uId);
        intent.putExtra("challID", challId);
        intent.putExtra("challName", challName);
        intent.putExtra("challDesc", challDesc);
        int challenger = ((MainActivity)getActivity()).getuId();
        intent.putExtra("challengerId", challenger);
        startActivity(intent);
    	getActivity().finish();
    }
    
    
 
    
    
    public List<String> readUsersFromDataBase(){

       // ADDS USERS TO SPINNER LIST FROM DATABASE FETCHED LIST        

        
       int numRows = ((MainActivity)getActivity()).getUsersList().size();
  
       List<String> userSpinnerArray = new ArrayList<String>();
       int currentUser = ((MainActivity)getActivity()).getuId();
       for(int i=0; i< numRows; i++)
    
       {
    	   final String dbUser = ((MainActivity)getActivity()).getUsersList().get(i).getName();
    	   
    	   if(currentUser != i){
    	   userSpinnerArray.add(dbUser);
       }
       }
       
       return userSpinnerArray;
       
       
    






        }
    
    public void readCategories(){
    	 // FINDS TABLE LAYOUT TO PLACE CHALLENGES IN
    	Context context = getActivity();
    	challengeLayout = (LinearLayout) myView.findViewById(R.id.category_selecter);
	
       
		
    	
    	 String[] categoryList = getResources().getStringArray(R.array.categories);
    	
    	
    	
    	
       int numRows = categoryList.length;
  
       
       
        
       
        // outer for loop
        for (int i=0; i< numRows; i++) {
        	
        	
            

            // Challenge Name COLUMN
            final ImageButton tv1 = new ImageButton(context);
            
            tv1.setPadding(25, 0, 25, 0);
            tv1.setImageDrawable(getResources().getDrawable(R.drawable.one));
            tv1.setBackgroundColor(getResources().getColor(R.color.white));

             
            


            final String dbCat = categoryList[i];
            		
            challengeLayout.addView(tv1);
            
            tv1.setOnClickListener(new View.OnClickListener() {
            	Boolean isActive = false;  
            	@Override
                public void onClick(View v) {
                	
                	  ((MainActivity)getActivity()).setCategory(dbCat);
                	  
                	  isActive = !isActive;
                	  
                	  if(isActive){
                		  tv1.setBackgroundColor(getResources().getColor(R.color.blue));
                		  readChallengesFromDataBase(dbCat);
                		 
                		  
                	  }
                	  else{
                		  tv1.setBackgroundColor(getResources().getColor(R.color.white));
                		  
                		  
                		  
                		  
                		  clearChallengesFromTable();
                		  
                	  }
                    
                } }
            )
            ;
            

            
        }






        }
    
    public void clearChallengesFromTable(){
    	tableLayout =  (TableLayout) myView.findViewById(R.id.challenge_list);
    	tableLayout.removeAllViews();
    }
    

    
    public void readChallengesFromDataBase(String category){

    	
    	   	
    	 // FINDS TABLE LAYOUT TO PLACE CHALLENGES IN
        tableLayout = (TableLayout) myView.findViewById(R.id.challenge_list);

        
       int numRows = ((MainActivity)getActivity()).getChallengesList().size();

    
       
  
        int matchedRows = 0;
       
        // outer for loop
        for (int i=0; i< numRows; i++) {
        	Context context = getActivity();
        	final String dbCat = ((MainActivity)getActivity()).getChallengesList().get(i).getCategory();
        	if(dbCat.contentEquals(category)){
        		
        	
        	final TableRow row = new TableRow(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            
            row.setLayoutParams(params);
            row.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            
            row.setPadding(1, 1, 1, 1);

            // Challenge Name COLUMN
            TextView tv1 = new TextView(context);
            tv1.setTextSize(40);
            tv1.setPadding(25, 0, 25, 0);
            
            // Points COLUMN
            TextView tv2 = new TextView(context);
            tv2.setTextSize(40);
            tv2.setPadding(25, 0, 25, 0);
                 
            
            final String dbChall = ((MainActivity)getActivity()).getChallengesList().get(i).getName();

            final String dbChallDesc = ((MainActivity)getActivity()).getChallengesList().get(i).getDescription();
            tv1.setText(dbChall);
            
            final String points = Integer.toString(((MainActivity)getActivity()).getChallengesList().get(i).getPoints());
          tv2.setText(points);
            final int dbchallId = ((MainActivity)getActivity()).getChallengesList().get(i).getId();
            
           
            row.addView(tv1);
            row.addView(tv2);
             
            row.setOnClickListener(new View.OnClickListener() {
                  
            	Boolean challActive = false;
            	@Override
                public void onClick(View v) {
                	  setChallengeToSend(dbchallId);
                	  
                	  Log.d("CHALL CHOOSER BEFORE FLIP", "challActive: "+Boolean.toString(challActive));
                	  challActive = !challActive;
                	  if(challActive){
                		  row.setBackgroundColor(getResources().getColor(R.color.blue));
                		  issueChallenge(getUserToChallenge(), dbchallId, dbChall, dbChallDesc);
                		  Log.d("CHALL CHOOSER AFTER FLIP TRUE", "challActive: "+Boolean.toString(challActive));  
                		  
                	  }
                	  else{
                		  row.setBackgroundColor(getResources().getColor(R.color.white));
                		  Log.d("CHALL CHOOSER AFTER FLIP FALSE", "challActive: "+Boolean.toString(challActive));
                	  }
                	  
                	  
                
                    
                } }
            )
            ;
            

            TableRow rowGap = new TableRow(context);

            rowGap.setLayoutParams(params);
            rowGap.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            rowGap.setBackgroundColor(getResources().getColor(R.color.white));
            rowGap.setPadding(1, 1, 1, 1);

            tableLayout.addView(row);
            tableLayout.addView(rowGap);
            
            matchedRows++;
        }

        	
        }
        if(matchedRows == 0){
        	Toast.makeText(myView.getContext(), "No challenges in "+category, Toast.LENGTH_LONG).show();
        	
        }
    






        }



	public int getUserToChallenge() {
		return userToChallenge;
	}



	public void setUserToChallenge(int userToChallenge) {
		this.userToChallenge = userToChallenge;
	}



	public int getChallengeToSend() {
		return challengeToSend;
	}



	public void setChallengeToSend(int challengeToSend) {
		this.challengeToSend = challengeToSend;
	}





	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// 
		String selectedUser =  parent.getItemAtPosition(position).toString();
		int numRows = ((MainActivity)getActivity()).getUsersList().size();
		for(int i = 0; i < numRows; i++){
			String dbName =  ((MainActivity)getActivity()).getUsersList().get(i).getName();
			int userId = ((MainActivity)getActivity()).getUsersList().get(i).getId();
			if(selectedUser.contentEquals(dbName)){
				setUserToChallenge(userId);
			}
		}
		
	}





	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}



    
	
    
}



