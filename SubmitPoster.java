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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SubmitPoster extends Activity implements View.OnClickListener {
	Button buttonBackToMenu = null;
	Button buttonSelect = null;
	TextView textViewReturn = null;
	TextView textViewError = null;
    List<String> returnValues = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_poster);
		buttonBackToMenu = (Button) findViewById(R.id.buttonBackToMenu);
		buttonSelect = (Button) findViewById(R.id.buttonSelect);
		textViewReturn = (TextView) findViewById(R.id.textViewReturn);
		textViewError = (TextView) findViewById(R.id.textViewError);
		buttonBackToMenu.setOnClickListener(this);
		buttonSelect.setOnClickListener(this);
		String strUserList = get("SubmitPoster.dat");
		String strTemp = "";
		int j=0;
		for(int i=0; i<strUserList.length(); i++) {
			if(strUserList.charAt(i) == ',') {
				returnValues.add(strTemp);
				j++;
				strTemp="";
			} else {
				strTemp += strUserList.charAt(i);
			}
		}
		String strReturn = returnValues.get(0).toString();
		if(j==1 && strReturn.length() > 0) { //only a returnValue
			textViewReturn.setText("Poster Upload: " + strReturn);
		} else if(j==2) { //also an errorValue
			textViewReturn.setText("Poster Upload: " + strReturn);
			textViewError.setText("Error:   " + returnValues.get(1).toString());
			
		} //should get no more than 2 entries
		Log.v("",strUserList);
}
	@Override
	public void onClick(View view) {
		String session = get("session.dat");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		Intent i;
		switch (view.getId())
		{
		case R.id.buttonBackToMenu:
	        i = new Intent(this, WelcomeUser.class);
	        // sending data to new activity
	        this.startActivity(i);
			break;
		case R.id.buttonSelect:
	        i = new Intent(this, SelectPoster.class);
	        // sending data to new activity
	        this.startActivity(i);
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
