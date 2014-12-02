//Purpose: 			MainActivity handles login and transition to welcome
//Original Author:		Brett Jones
//Creation Date:		11/20/2013
//Modification Date:	12/04/2013
//Modification Purpose: Debugging, fixed get
//Modification Date:	12/05/2013
//Modification Purpose: Adding more examples...
package com.example.pmsapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.example.pmsapp.DoPOST;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity  implements View.OnClickListener {
	String Session = null;
	Button buttonLogin = null;
	Button buttonClear = null;
	Button buttonRegister = null;
	EditText editTextName = null;
	EditText editTextPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonClear = (Button) findViewById(R.id.buttonClear);
		buttonRegister = (Button) findViewById(R.id.buttonRegister);
		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		
		buttonLogin.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
		buttonRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		DoPOST mDoPOST;
		switch (view.getId())
		{
		case R.id.buttonLogin:
			String name = editTextName.getText().toString();
			String password = editTextPassword.getText().toString();
			if(name.length() < 1 || name.compareTo("Enter Name...") == 0) {
				Toast.makeText(getApplicationContext(), "You must enter a valid name!", Toast.LENGTH_LONG).show();
			} else if(password.length() < 1 || password.compareTo("Enter Password...") == 0) {
				Toast.makeText(getApplicationContext(), "You must enter a valid password!", Toast.LENGTH_LONG).show();
			} else {
				//Setup the parameters
				nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
				nameValuePairs.add(new BasicNameValuePair("Name", name));
				nameValuePairs.add(new BasicNameValuePair("Password", password));
				nameValuePairs.add(new BasicNameValuePair("Session", Session));
				mDoPOST = new DoPOST(Login.this, nameValuePairs, "login.php");
				mDoPOST.execute("");
				//buttonLogin.setEnabled(false);
				finish();
			}
			break;
		case R.id.buttonClear:
			editTextName.setText("Enter Name...");
			editTextPassword.setText("Enter Password...");
			break;
		case R.id.buttonRegister:
			//Setup the parameters
			nameValuePairs.add(new BasicNameValuePair("Mobile", "true"));
			mDoPOST = new DoPOST(Login.this, nameValuePairs, "register.php");
			mDoPOST.execute("");
			finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



}
