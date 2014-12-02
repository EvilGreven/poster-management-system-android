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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	Button buttonExit = null;
	Button buttonReconnect = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonExit = (Button) findViewById(R.id.buttonExit);
		buttonReconnect = (Button) findViewById(R.id.buttonReconnect);
		buttonExit.setOnClickListener(this);
		buttonReconnect.setOnClickListener(this);

        Intent i = new Intent(this, Login.class);
        // sending data to new activity
        this.startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
		case R.id.buttonReconnect:
	        Intent i = new Intent(this, Login.class);
	        // sending data to new activity
	        this.startActivity(i);
	        break;
		case R.id.buttonExit:
			finish();
			break;
		}
	}


}
