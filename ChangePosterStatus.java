//Purpose: 				ChangePosterStatus lets an admin change the status of a poster
//Original Author:		Brett Jones
//Creation Date:		11/20/2013
//Modification Date:	12/04/2013
//Modification Purpose: Debugging, fixed get
//Modification Date:	12/05/2013
//Modification Purpose: Adding more examples...
package com.example.pmsapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ChangePosterStatus extends Activity implements View.OnClickListener {
	Button buttonBackToCheckPosters = null;
	Button buttonSetPosterProcessing = null;
	Button buttonSetPosterPrinted = null;
	Button buttonSetPosterWrong = null;
	Button buttonSetPosterOther = null;
	TextView textViewFile = null;
	TextView textViewFlag = null;
	TextView textViewUser = null;
    List<String> fileValues = new ArrayList<String>();
	List<String> flagValues = new ArrayList<String>();
	List<String> idValues = new ArrayList<String>();
	List<String> userValues = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_poster_status);
		buttonBackToCheckPosters = (Button) findViewById(R.id.buttonBackToCheckPosters);
		buttonSetPosterProcessing = (Button) findViewById(R.id.buttonSetPosterProcessing);
		buttonSetPosterPrinted = (Button) findViewById(R.id.buttonSetPosterPrinted);
		buttonSetPosterWrong = (Button) findViewById(R.id.buttonSetPosterWrong);
		buttonSetPosterOther = (Button) findViewById(R.id.buttonSetPosterOther);
		textViewFile = (TextView) findViewById(R.id.textViewFile);
		textViewFlag = (TextView) findViewById(R.id.textViewFlag);
		textViewUser = (TextView) findViewById(R.id.textViewUser);
		buttonBackToCheckPosters.setOnClickListener(this);
		buttonSetPosterProcessing.setOnClickListener(this);
		buttonSetPosterPrinted.setOnClickListener(this);
		buttonSetPosterWrong.setOnClickListener(this);
		buttonSetPosterOther.setOnClickListener(this);
		String strUserList = get("ChangePosterStatus.dat");
		String strTemp = "";
		int j=0;
		for(int i=0; i<strUserList.length(); i++) {
			if(strUserList.charAt(i) == ',') {
				if(j==0) { //got ID
					idValues.add(strTemp);
				}else if(j==1) { //got USER
					userValues.add(strTemp);
				}else if(j==2) { //got FILE
					fileValues.add(strTemp); //add file to list
				}else if(j==3) { //got FLAG
					flagValues.add(strTemp);
				}
				j++;
				if(j>3) j=0;
				strTemp="";
			} else {
				strTemp += strUserList.charAt(i);
			}
		}
		textViewFile.setText("Filename: " + fileValues.get(0).toString());
		textViewFlag.setText("Status:   " + flagValues.get(0).toString());
		textViewUser.setText("User:     " + userValues.get(0).toString());
		// Use the SimpleCursorAdapter to show the
        // elements in a ListView
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
         //   android.R.layout.simple_list_item_1, fileValues);
        //setListAdapter(adapter);
		//listViewUserList.
		Log.v("",strUserList);
}
	@Override
	public void onClick(View view) {
		String session = get("session.dat");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		switch (view.getId())
		{
		case R.id.buttonBackToCheckPosters:
	        Intent i = new Intent(this, CheckPosters.class);
	        // sending data to new activity
	        this.startActivity(i);
			break;
		case R.id.buttonSetPosterProcessing:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			nameValuePairs.add(new BasicNameValuePair("ID", idValues.get(0).toString()));
			nameValuePairs.add(new BasicNameValuePair("submit", "processing"));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ChangePosterStatus.this, nameValuePairs, "changeposterstatus.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonSetPosterPrinted:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			nameValuePairs.add(new BasicNameValuePair("ID", idValues.get(0).toString()));
			nameValuePairs.add(new BasicNameValuePair("submit", "printed"));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ChangePosterStatus.this, nameValuePairs, "changeposterstatus.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonSetPosterWrong:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			nameValuePairs.add(new BasicNameValuePair("ID", idValues.get(0).toString()));
			nameValuePairs.add(new BasicNameValuePair("submit", "wrong"));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ChangePosterStatus.this, nameValuePairs, "changeposterstatus.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonSetPosterOther:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			nameValuePairs.add(new BasicNameValuePair("ID", idValues.get(0).toString()));
			nameValuePairs.add(new BasicNameValuePair("submit", "other"));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ChangePosterStatus.this, nameValuePairs, "changeposterstatus.php");
			mDoPOST.execute("");
			break;
		}
        finish();
	}

	public String get(String filename) {
		String content = "";
	    try {
	        InputStream inputStream = openFileInput(filename);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            content = stringBuilder.toString();
	        }
	    } catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
	    return content;
	}
}
