/*THIS TASK ALLOWS THE SCOREBOARD TO BE RETRIEVED IN A SIMPLE FASHION
 * 
 * THE METHOD IS SENT TO THE BACKEND AND INFORMS THE BACKEND WHAT DATA TO EXPECT AND WHAT TO DO WITH IT
 * 
 * IN THIS CASE THE BACKEND EXPECTS ONLY A METHOD WHICH IS PROVIDEd THROUGH THE EXECUTE STATEMENT
 * 
 * */


package fi.metropolia.challengedemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncGetScoreboard extends AsyncTask<String, Integer, List<Users>> {
    AsyncResponse listener;

    private int userId, totalPoints, currentChallenge;
    private String userName;
    private Boolean active, notified;
    List<Users> usersList;
    List<Challenges> challengesList;
	
    @Override
    protected List<Users> doInBackground(String... params) {
        // TODO Auto-generated method stub
       postData(params[0]);
       
       
       
        return usersList;
    }
    
 

    public void postData(String valueIWantToSend) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
        		 "http://10.0.2.2:8080/ChallengeAcceptedBackEnd/HandlerServlet");
        Log.d("HTTP POST", "I CREATED A POST");
        Log.d("HTTP POST", valueIWantToSend);
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
                    valueIWantToSend));
            nameValuePairs.add(new BasicNameValuePair("sort",
                    "points"));
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d("HTTP POST", "SENT THE POST");

            Header reply = response.getFirstHeader("backEnd") ;
            if(reply !=null){
                String postReply = reply.getValue();
                Log.d("HTTP RESPONSE",  postReply);


                /*
                 * HERE THE RESPONSE IS VERY IMPORTANT
                 * 
                 * THIS FOR LOOP IS EFFECTIVELY A REVERSE OF THE FOR LOOP IN THE BACK END AS IT TAKES THE
                 * 			STRINGS -> OBJECT -> LIST
                 * 
                 * 
                */
                
                if(response.containsHeader("users")){
                    usersList = new ArrayList<Users>();
                    for(int i=0; i < Integer.parseInt(response.getFirstHeader("itemCount").getValue()); i++){
                        userId = Integer.parseInt(response.getFirstHeader("uId"+Integer.toString(i)).getValue());
                        totalPoints = Integer.parseInt(response.getFirstHeader("uTotPoints"+Integer.toString(i)).getValue());
                        currentChallenge = Integer.parseInt(response.getFirstHeader("uCurrentChallenge"+Integer.toString(i)).getValue());
                        userName = response.getFirstHeader("uName"+Integer.toString(i)).getValue();
                        active = Boolean.valueOf(response.getFirstHeader("uActive"+Integer.toString(i)).getValue());
                        notified = Boolean.valueOf(response.getFirstHeader("uNotif"+Integer.toString(i)).getValue());


                        Users newUser = new Users(userId, totalPoints, currentChallenge, userName, active, notified);
                        usersList.add(newUser);
                        Log.d("SCOREBOARD",   newUser.getName() +"ID: "
                                +Integer.toString(newUser.getId()) +","
                                		+ "Total Points: "+ Integer.toString(newUser.getTotalPoints())
                                		+", CurrentChallengeID: "+ Integer.toString(newUser.getCurrentChallenge()) 
                                		+", Active? "+ Boolean.toString(newUser.isChallengeActive())
                                		+", Notified? " + Boolean.toString(newUser.isNotified()));
                    }


                    

                }

                else{
                    Log.d("HTTP RESPONSE", "NO VALID RESPONSE METHOD");
                }




            }


            else{
                Log.d("HTtP RESPONSE", "NO RESPONSE HEADERS");

            }




        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	Log.d("POST FAIL", "NO POST SENT CLIENTPROTOCOL");
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	Log.d("POST FAIL", "NO POST SENT IOEXCEPTION");
        }
    }
    
    

	/*
	 * THE LIST THAT IS COMPLETED AS PART OF THE POST RESPONSE ENDS UP HERE
	 * 
	 * THIS SENDS THE COMPLETED LIST THROUGH THE INTERFACE WHERE IT CAN THEN BE 
	 * USED BY THE OTHER ACTIVITIES
	 * 
	 * 
	*/
    
    @Override
    protected void onPostExecute(List<Users> result) {
       super.onPostExecute(result);
       listener.scoreboardGrabber(result);
       listener.loadComplete();
       
    }
}
