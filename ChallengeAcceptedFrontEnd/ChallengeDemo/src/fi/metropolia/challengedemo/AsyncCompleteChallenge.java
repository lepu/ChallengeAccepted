/*THIS TASK ALLOWS A CHALLENGE TO BE COMPLETED
 * 
 * THE METHOD IS SENT TO THE BACKEND AND INFORMS THE BACKEND WHAT DATA TO EXPECT AND WHAT TO DO WITH IT
 * 
 * IN THIS CASE THE BACKEND EXPECTS ONLY A USER ID AS ALL THE OTHER DATA IS ALREADY STORED IN THE BACKEND
 * 
 * */



package fi.metropolia.challengedemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class AsyncCompleteChallenge extends AsyncTask<Integer, Integer, String> {
    public AsyncResponse delegate=null;
    AsyncResponse listener;
    private int userId;
    
 

    @Override
    protected String doInBackground(Integer... params) {
        // TODO Auto-generated method stub
    	setUserId(params[0]);
    	String method = "completeChallenge";
        postData(method);
        
        
        return null;
    }





    public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public void postData(String valueIWantToSend) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
        		 "http://10.0.2.2:8080/ChallengeAcceptedBackEnd/HandlerServlet");
        Log.d("HTTP POST", "I CREATED A POST");
        String postUser = Integer.toString(userId);
        
        Log.d("HTTP POST", postUser);
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
            		valueIWantToSend));
            
                nameValuePairs.add(new BasicNameValuePair("User", postUser));
                
               
            
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d("HTTP POST", "SENT THE POST");

            
            



        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	Log.d("POST FAIL", "NO POST SENT CLIENTPROTOCOL");
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	Log.d("POST FAIL", "NO POST SENT IOEXCEPTION");
        }






    } 
	
}
