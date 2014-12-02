//Purpose: 				WelcomeAdmin handles welcome screen for admins
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.pmsapp.DoPOST;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeAdmin extends Activity implements View.OnClickListener {
	Button buttonLogout = null;
	Button buttonCheckPosters = null;
	Button buttonManageUsers = null;

	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		buttonLogout = (Button) findViewById(R.id.buttonLogout);
		buttonCheckPosters = (Button) findViewById(R.id.buttonCheckPosters);
		buttonManageUsers = (Button) findViewById(R.id.buttonManageUsers);
		buttonLogout.setOnClickListener(this);
		buttonCheckPosters.setOnClickListener(this);
		buttonManageUsers.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		String session = get("session.dat");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		switch (view.getId())
		{
		case R.id.buttonLogout:
			//temp test - php uses ID to modify stuff
			//each name/value pair is a PARAMETER and its VALUE
			//nameValuePairs.add(new BasicNameValuePair("ID", "79"));
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeAdmin.this, nameValuePairs, "logout.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonCheckPosters:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeAdmin.this, nameValuePairs, "checkposters.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonManageUsers:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", session));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(WelcomeAdmin.this, nameValuePairs, "manageusers.php");
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
