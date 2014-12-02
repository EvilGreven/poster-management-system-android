//Purpose: 				DoPost handles sending and receiving information to/from the server
//						For now only handles Login/Logout
//Original Author:		Brett Jones
//Creation Date:		11/20/2013
//Modification Date:	12/04/2013
//Modification Purpose: Debugging, adding ManageUsers prototype
//Modification Date:	12/05/2013
//Modification Purpose: Adding more examples...
//Modification Date:	12/06/2013
//Modification Purpose:	forgot to send access level, fixed 
package com.example.pmsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DoPOST extends AsyncTask<String, Void, Boolean>{

	private static final String ADDR = "68.97.130.80";
	//private static final String ADDR = "yahoo.com";
	private static final String PORT = "1337";
	//private static final String PORT = "80";
	String PAGE = "";
	Context mContext = null;
	String strNameToSearch = "";
	ArrayList<NameValuePair> nameValuePairs =  null;
	//Result data
	String strNext;
	String strSession;
	JSONArray jaData;
	
	Exception exception = null;
	
	DoPOST(Context context, ArrayList<NameValuePair> submission, String page){
		mContext = context;
		nameValuePairs = submission;
		PAGE = page;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		try{

			//Add more parameters as necessary

			//Create the HTTP request
			HttpParams httpParameters = new BasicHttpParams();

			//Setup timeouts
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			HttpConnectionParams.setSoTimeout(httpParameters, 15000);			

	        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			Log.v("",nameValuePairs.toString());

	        for(int index=0; index < nameValuePairs.size(); index++) {
	        	if(nameValuePairs.get(index).getValue() == null) { //StringBody does NOT like null values.
	        		nameValuePairs.remove(index);
	        		break;
	        	}
	            if(nameValuePairs.get(index).getName().equalsIgnoreCase("file")) {
	                // If the key equals to "File", we use FileBody to transfer the data
	                mpEntity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
	            } else {
	                // Normal string data
	                mpEntity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	            }
	        }

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
		    HttpContext localContext = new BasicHttpContext();
			HttpPost httppost = new HttpPost("http://" + ADDR + ":" + PORT + "/" + PAGE);
			//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httppost.setEntity(mpEntity);
			Log.v("",httppost.getURI().toString() + httppost.getParams().toString());

			HttpResponse response = httpclient.execute(httppost, localContext);
			HttpEntity entity = response.getEntity();

			String result = EntityUtils.toString(entity);
			Log.v("",result);
							// Create a JSON object from the request response
			JSONObject jsonObject = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
			//JSONObject jsonObject = new JSONObject("{" + "\"Name\"" + ":" + "\"asdf\"" + "," + "\"Password\"" + ":" + 
			//					"\"asdf\"" + "," + "\"Access\"" + ":" + "\"Administrator\"" + "," +
			//					"\"Contact\"" + ":" + "\"nope, no contact info here nosireesss\"" + "}");
			//Retrieve the data from the JSON object
			strNext = jsonObject.getString("Next");
			strSession = jsonObject.getString("Session");
			jaData = jsonObject.getJSONArray("Data");
			goTo(strNext, strSession);
		}catch (Exception e){
			Log.e("ClientServerDemo", "Error:", e);
			exception = e;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean valid){
		//Update the UI
		if(exception != null){
			Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	void store(String filename, String content) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
	        		mContext.openFileOutput(filename, Context.MODE_PRIVATE));
	        outputStreamWriter.write(content);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    }	
	}
	void store(String filename, String content[][]) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
	        		mContext.openFileOutput(filename, Context.MODE_PRIVATE));
	        for(int i=0; i<content.length; i++)
	        	for(int j=0; j<content[i].length; j++) {
	        		Log.v("",content[i][j]);
	        		outputStreamWriter.write(content[i][j] + ",");
	        	}
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    }	
	}

	void goTo(String screen, String session) {
		Intent i = null;
		store("session.dat",session);
		if(screen.compareTo("welcomeAdmin") == 0) {
            i = new Intent(mContext, WelcomeAdmin.class);
            // sending data to new activity
            mContext.startActivity(i);
		} else if(screen.compareTo("login") == 0) {
			if(session.compareTo("error:session") == 0); //session error goes to login, notify w/ toast?
			if(session.compareTo("error:database") == 0); //database error goes to login, notify w/ toast?
			if(session.compareTo("error:namepassword") == 0); //username password error
			i = new Intent(mContext, Login.class);
            // sending data to new activity
            mContext.startActivity(i);
		} else if(screen.compareTo("welcomeUser") == 0) {
            i = new Intent(mContext, WelcomeUser.class);
            // sending data to new activity
            mContext.startActivity(i);
		} else if(screen.compareTo("register") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			//String[][] strArray = new String[jaData.length()][2]; //2 fields per row
			//validation error goes to register, notify w/ toast?
		    	for(int j=0;j<jaData.length() &&	
		    			session.compareTo("error:validation") != 0;j++)
		    	{	//so read in each line in the array
			        try {	//data here is validation, you could ignore it i guess since
			        		//the only check is between the operation and number
						JSONObject joLine = jaData.getJSONObject(j);
						String strOP = joLine.getString("op");	//either sqrt or sq
						int intNumber = joLine.getInt("para");
						Log.v("", "OP: " + strOP + ", Number: " + intNumber);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						//actually we don't need to do this for register but here's something what it would look like
						//strArray[j][0] = strOP;
						//strArray[j][1] = intNumber.toString();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
				 //store("CheckPosterStatus.dat",strArray);
				 i = new Intent(mContext, Register.class);
	            // sending data to new activity
	            mContext.startActivity(i);
		}else if(screen.compareTo("manageUsers") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][5]; //5 fields per row
				
		    	for(int j=0;j<jaData.length();j++)
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						String strID = joLine.getString("ID");
						String strName = joLine.getString("Name");
						String strPassword = joLine.getString("Password");
						String strContact = joLine.getString("Contact");
						String strAccess = joLine.getString("Access");
						Log.v("", "ID: " + strID + ", Name: " + strName + ", Password: "
								+ strPassword + ", Contact: " + strContact + ", Access: "
								+ strAccess);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[j][0] = strID;
						strArray[j][1] = strName;
						strArray[j][2] = strPassword;
						strArray[j][3] = strContact;
						strArray[j][4] = strAccess;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
				 store("ManageUsers.dat",strArray);
		           i = new Intent(mContext, ManageUsers.class);
		           // sending data to new activity
		           mContext.startActivity(i);
		} else if(screen.compareTo("checkPosters") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][4]; //4 fields per row
		    	for(int j=0;j<jaData.length();j++)
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						String strID = joLine.getString("ID");
						String strUser = joLine.getString("User");
						String strFile = joLine.getString("File");
						String strFlag = joLine.getString("Flag");
						Log.v("", "ID: " + strID + ", File: " + strFile + ", Password: "
								+ strFlag + ", User: " + strUser);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[j][0] = strID;
						strArray[j][1] = strUser;
						strArray[j][2] = strFile;
						strArray[j][3] = strFlag;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
				 store("CheckPosters.dat",strArray);
		           i = new Intent(mContext, CheckPosters.class);
		           // sending data to new activity
		           mContext.startActivity(i);
		} else if(screen.compareTo("checkPosterStatus") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][2]; //2 fields per row
		    	for(int j=0;j<jaData.length();j++)
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						
						String strFile = joLine.getString("File");
						String strFlag = joLine.getString("Flag");
						Log.v("", "File: " + strFile + ", Flag: "
								+ strFlag);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[j][0] = strFile;
						strArray[j][1] = strFlag;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
				 store("CheckPosterStatus.dat",strArray);
				 i = new Intent(mContext, CheckPosterStatus.class);
		           // sending data to new activity
		           mContext.startActivity(i);
		} else if(screen.compareTo("changePosterStatus") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][4]; //4 fields per row
		    	for(int j=0;j<jaData.length();j++) //we should only have one result here
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						String strID = joLine.getString("ID");
						String strUser = joLine.getString("User");
						String strFile = joLine.getString("File");
						String strFlag = joLine.getString("Flag");
						Log.v("", "ID: " + strID + ", File: " + strFile + ", Flag: "
								+ strFlag + ", User: " + strUser);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[j][0] = strID;
						strArray[j][1] = strUser;
						strArray[j][2] = strFile;
						strArray[j][3] = strFlag;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 store("ChangePosterStatus.dat",strArray);

			}
		           i = new Intent(mContext, ChangePosterStatus.class);
		           // sending data to new activity
		           mContext.startActivity(i);
		} else if(screen.compareTo("cancelPrintRequest") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][3]; //4 fields per row
		    	for(int j=0;j<jaData.length();j++)
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						String strID = joLine.getString("ID");
						String strFile = joLine.getString("File");
						String strFlag = joLine.getString("Flag");
						Log.v("", "ID: " + strID + ", File: " + strFile + ", Flag: "
								+ strFlag);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[j][0] = strID;
						strArray[j][1] = strFile;
						strArray[j][2] = strFlag;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
				 store("CancelPrintRequest.dat",strArray);
		           i = new Intent(mContext, CancelPrintRequest.class);
		           // sending data to new activity
		           mContext.startActivity(i);
		} else if(screen.compareTo("submitPoster") == 0) {  //untested example
		   	 //parse the JSONArray jaData appropriately
			String[][] strArray = new String[jaData.length()][1]; //1 fields per row
		    	for(int j=0;j<jaData.length();j++) //should only get one
		    	{	//so read in each line in the array
			        try {
						JSONObject joLine = jaData.getJSONObject(j);
						//Success, NameUnavailable, InvalidType, or ErrorCode
						String strReturnCode = joLine.getString("ReturnCode");
						String strErrorCode = ""; //this will usually be empty
						if(strReturnCode.compareTo("Success") == 0); //file uploaded
						if(strReturnCode.compareTo("NameUnavailable") == 0); //please rename file
						if(strReturnCode.compareTo("InvalidType") == 0); //wrong file type
						if(strReturnCode.compareTo("ErrorCode") == 0) // something bad happened
						{
							strErrorCode = joLine.getString("Code"); //so get error code
						}
						Log.v("", "ReturnCode: " + strReturnCode + ", ErrorCode: "
								+ strErrorCode);
						//and store it such that the context we're about to call can read
						//probably something like the store() would work with an array list
						//for users right now it's id, name, password, session, contactinfo
						strArray[0][0] = strReturnCode;
						if(strErrorCode.compareTo("") != 0 && jaData.length() > 1)
							strArray[1][0] = strErrorCode;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
		    	}
				 store("SubmitPoster.dat",strArray);
		        i = new Intent(mContext, SubmitPoster.class);
		        // sending data to new activity
		        mContext.startActivity(i);
		}
	}
}
