package com.iknorite.android.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity
{
	String editTextPreference = null;
	Button prefBtn;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		prefBtn = (Button) findViewById(R.id.btn01);
		prefBtn.setOnClickListener(
			new View.OnClickListener()
			{
				public void onClick(View v)
				{
					Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
					startActivity(settingsActivity);
				}
			});

		getPrefs();
	}

	private void getPrefs()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		// Get the xml/preferences.xml preferences
		editTextPreference = prefs.getString("awayMsgPref", "I'm currently unavailable.");

		TextView txt01 = (TextView) findViewById(R.id.txt01);
		txt01.setText(editTextPreference);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		getPrefs();
	}
}
