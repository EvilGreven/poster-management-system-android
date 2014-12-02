//Purpose: 				CancelPrintRequest lets a user cancel a submitted-status poster
//Original Author:		Brett Jones
//Creation Date:		11/20/2013
//Modification Date:	12/04/2013
//Modification Purpose: Debugging, fixed get
//Modification Date:	12/05/2013
//Modification Purpose: Adding more examples...
//Modification Date:	12/06/2013
//Modification Purpose: Fixing a few issues
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
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class CancelPrintRequest extends ListActivity implements View.OnClickListener {
	Button buttonBackToMenu = null;
	//ListView listViewUserList = null;
	ArrayAdapter<String> aaUserList;
    List<String> fileValues = new ArrayList<String>();
	List<String> flagValues = new ArrayList<String>();
	List<String> idValues = new ArrayList<String>();
	int intRequest = -1;
	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		buttonBackToMenu = (Button) findViewById(R.id.buttonBackToMenu);
		buttonBackToMenu.setOnClickListener(this);
		//listViewUserList = (ListView) findViewById(R.id.listViewUserList);
		//spinnerUserList.
		String strUserList = get("CancelPrintRequest.dat");
		String strTemp = "";
		int j=0;
		for(int i=0; i<strUserList.length(); i++) {
			if(strUserList.charAt(i) == ',') {
				if(j==0) { //got ID
					idValues.add(strTemp);
				}else if(j==1) { //got FILE
					fileValues.add(strTemp); //add file to list
				}else if(j==2) { //FLAG
					flagValues.add(strTemp);
				}
				j++;
				if(j>2) j=0;
				strTemp="";
			} else {
				strTemp += strUserList.charAt(i);
			}
		}
        // Use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, fileValues);
        setListAdapter(adapter);
		//listViewUserList.
		Log.v("",strUserList);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
		case R.id.buttonBackToMenu:
	        Intent i = new Intent(this, WelcomeUser.class);
	        // sending data to new activity
	        this.startActivity(i);
			break;
		}
        finish();
	}
	//JEFF/ROBERT this is how you select stuff with this implementation.  I'm not messing with spinners - Brett
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Get the item that was clicked
        //position should be interchangable with index, so use it with the other ArrayLists to populate the popup
        //String keyword = (String) getListAdapter().getItem(position);
        //Toast.makeText(this, "You selected: " + keyword + " at: " + position, Toast.LENGTH_LONG).show();
        intRequest = position;
        AlertDialog.Builder abWarning = new AlertDialog.Builder(getListView().getContext());
        abWarning.setMessage("Cancel printing of "
        		+ fileValues.get(position).toString() + "?").setPositiveButton("Yes", dialogClickListener)
        		.setNegativeButton("No", dialogClickListener
        		).show();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	Log.v("","Yes");
        		String session = get("session.dat");
        		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        		DoPOST mDoPOST;
    			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
    			nameValuePairs.add(new BasicNameValuePair("Session", session));
    			nameValuePairs.add(new BasicNameValuePair("ID", idValues.get(intRequest).toString()));
    			nameValuePairs.add(new BasicNameValuePair("action", "cancel"));
    			Log.v("",nameValuePairs.toString());
    			mDoPOST = new DoPOST(CancelPrintRequest.this, nameValuePairs, "cancelprintrequest.php");
    			mDoPOST.execute("");
    			CancelPrintRequest.this.finish();
             break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
            	Log.v("","No");
                break;
            }
        }
    };
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
