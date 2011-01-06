package com.iknorite.android.test;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: eugene.parker
 * Date: 1/4/11
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmsReceiver extends BroadcastReceiver
{
	public static boolean isEnabled = false;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (isEnabled)
		{
			//---get the SMS message passed in---
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String str = "";
			String from = null;
			if (bundle != null)
			{
				//---retrieve the SMS message received---
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				for (int i = 0; i < msgs.length; i++)
				{
					msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					from = ((from == null)
						? msgs[i].getOriginatingAddress()
						: from);
					str += "SMS from " + msgs[i].getOriginatingAddress();
					str += " :";
					str += msgs[i].getMessageBody();
					str += "\n";
				}
				//---display the new SMS message---
				Toast.makeText(context, "AutoDroid: " + from, Toast.LENGTH_SHORT).show();
				sendSms(context, from);
			}
		}
		else
		{
			// Toast.makeText(context, "SmsReceiver: disabled!", Toast.LENGTH_LONG).show();
		}
	}

    void sendSms(Context context, String dest) {

        if (isEnabled) {
            // Toast.makeText(context, "AutoDroid: enabled", Toast.LENGTH_LONG).show();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            // Get the xml/preferences.xml preferences
            String awayMsg = prefs.getString("awayMsgPref", "I'm currently unavailable.");

            PendingIntent pi = PendingIntent.getActivity(context, 0,
                    new Intent(context, SmsReceiver.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(dest, null, awayMsg, pi, null);
        } else {
            // Toast.makeText(context, "AutoDroid: disabled", Toast.LENGTH_LONG).show();
        }
    }
}
