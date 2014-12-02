//Purpose: 				WelcomeUser handles welcome screen for users
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class WelcomeUser extends Activity implements View.OnClickListener {
	Button buttonLogout = null;
	Button buttonCheckPosterStatus = null;
	Button buttonSubmitPoster = null;
	Button buttonCancelPrintRequest = null;

	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_user);
		buttonLogout = (Button) findViewById(R.id.buttonLogout);
		buttonCheckPosterStatus = (Button) findViewById(R.id.buttonCheckPosterStatus);
		buttonSubmitPoster = (Button) findViewById(R.id.buttonSubmitPoster);
		buttonCancelPrintRequest = (Button) findViewById(R.id.buttonCancelPrintRequest);
		buttonLogout.setOnClickListener(this);
		buttonCheckPosterStatus.setOnClickListener(this);
		buttonSubmitPoster.setOnClickListener(this);
		buttonCancelPrintRequest.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		String session = get("session.dat");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		switch (view.getId())
		{
		case R.id.buttonLogout:
			//each name/value pair is a PARAMETER and its VALUE
			//nameValuePairs.add(new BasicNameValuePair("ID", "79"));
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeUser.this, nameValuePairs, "logout.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonCheckPosterStatus:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeUser.this, nameValuePairs, "checkposterstatus.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonSubmitPoster:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeUser.this, nameValuePairs, "submitposter.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonCancelPrintRequest:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeUser.this, nameValuePairs, "cancelprintrequest.php");
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
