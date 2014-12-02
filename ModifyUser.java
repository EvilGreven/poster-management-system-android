//Purpose: 			ModifyUser lets an admin add/delete/change a user
//Original Author:		Brett Jones
//Creation Date:		12/06/2013

package com.example.pmsapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
//import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.Toast;

public class ModifyUser extends Activity implements View.OnClickListener {
    EditText editTextName = null;
    EditText editTextPassword = null;
    EditText editTextContact = null;
    EditText editTextAccess = null;
    Button buttonCancel;
    Button buttonSave;
    Button buttonAdd;
    Button buttonDelete;
    String strID = "";
    String strSession = "";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_user);
        strID=getIntent().getStringExtra("ID");
        strSession=getIntent().getStringExtra("Session");
        editTextName=(EditText)this.findViewById(R.id.editTextName);
        editTextPassword=(EditText)this.findViewById(R.id.editTextPassword);
        editTextContact=(EditText)this.findViewById(R.id.editTextContact);
        editTextAccess=(EditText)this.findViewById(R.id.editTextAccess);
        editTextName.setText(getIntent().getStringExtra("Name"));
        editTextPassword.setText(getIntent().getStringExtra("Password"));
        editTextContact.setText(getIntent().getStringExtra("Contact"));
        editTextAccess.setText(getIntent().getStringExtra("Access"));
        buttonCancel=(Button)this.findViewById(R.id.buttonCancel);
        buttonSave=(Button)this.findViewById(R.id.buttonSave);
        buttonAdd=(Button)this.findViewById(R.id.buttonAdd);
        buttonDelete=(Button)this.findViewById(R.id.buttonDelete);
        buttonCancel.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
       
    }
	@Override
	public void onClick(View view) {
		//get("session.dat");
		String strName = editTextName.getText().toString();
		String strPassword = editTextPassword.getText().toString();
		String strContact = editTextContact.getText().toString();
		String strAccess = editTextAccess.getText().toString();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		switch (view.getId())
		{
		case R.id.buttonCancel:
	        Intent i = new Intent(this, ManageUsers.class);
	        // sending data to new activity
	        this.startActivity(i);
			break;
		case R.id.buttonDelete:
			//temp test - php uses ID to modify stuff
			//each name/value pair is a PARAMETER and its VALUE
			//nameValuePairs.add(new BasicNameValuePair("ID", "79"));
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", strSession));
			//need ID and del action
			nameValuePairs.add(new BasicNameValuePair("action", "del"));
			nameValuePairs.add(new BasicNameValuePair("ID", strID));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ModifyUser.this, nameValuePairs, "manageusers.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonSave:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", strSession));
			//need ID, Name, Password, Contact, Access, and ed action
			nameValuePairs.add(new BasicNameValuePair("action", "ed"));
			nameValuePairs.add(new BasicNameValuePair("ID", strID));
			nameValuePairs.add(new BasicNameValuePair("Name", strName));
			nameValuePairs.add(new BasicNameValuePair("Password", strPassword));
			nameValuePairs.add(new BasicNameValuePair("Access", strAccess));
			nameValuePairs.add(new BasicNameValuePair("Contact", strContact));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ModifyUser.this, nameValuePairs, "manageusers.php");
			mDoPOST.execute("");
			break;
		case R.id.buttonAdd:
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			nameValuePairs.add(new BasicNameValuePair("Session", strSession));
			//need Name, Password, Contact, Access
			nameValuePairs.add(new BasicNameValuePair("Name", strName));
			nameValuePairs.add(new BasicNameValuePair("Password", strPassword));
			nameValuePairs.add(new BasicNameValuePair("Access", strAccess));
			nameValuePairs.add(new BasicNameValuePair("Contact", strContact));
			Log.v("",nameValuePairs.toString());
			mDoPOST = new DoPOST(ModifyUser.this, nameValuePairs, "manageusers.php");
			mDoPOST.execute("");
			break;
		}
        finish();
    }

}
