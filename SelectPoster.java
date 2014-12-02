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
import java.io.File;
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
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SelectPoster extends ListActivity implements View.OnClickListener {
	Button buttonBackToSubmitPoster = null;
	//ListView listViewUserList = null;
    List<String> fileValues = new ArrayList<String>();
	int intRequest = -1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		buttonBackToSubmitPoster = (Button) findViewById(R.id.buttonBackToMenu);
		buttonBackToSubmitPoster.setOnClickListener(this);
		//listViewUserList = (ListView) findViewById(R.id.listViewUserList);
		//spinnerUserList.
        // Use the SimpleCursorAdapter to show the
        // elements in a ListView
		String strFile = "Prolog Program 2 to 6.pdf";
		fileValues.add(strFile);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, fileValues);
        setListAdapter(adapter);
		//listViewUserList.
	}
	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
		case R.id.buttonBackToMenu:
	        Intent i = new Intent(this, SubmitPoster.class);
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
        abWarning.setMessage("Upload file "
        		+ fileValues.get(position).toString() + "?").setPositiveButton("Yes", dialogClickListener)
        		.setNegativeButton("No", dialogClickListener
        		).show();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        	String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            		+ "/Prolog.pdf";
            switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //Yes button clicked
            	Log.v("","Yes");
        		String session = get("session.dat");
        		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        		DoPOST mDoPOST;
    			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
    			nameValuePairs.add(new BasicNameValuePair("Session", session));
    			//nameValuePairs.add(new BasicNameValuePair("File", "filename"));
    			nameValuePairs.add(new BasicNameValuePair("File", file));
    			Log.v("",fileValues.get(intRequest).toString());
    			mDoPOST = new DoPOST(SelectPoster.this, nameValuePairs, "submitposter.php");
    			mDoPOST.execute("");
    			SelectPoster.this.finish();
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
