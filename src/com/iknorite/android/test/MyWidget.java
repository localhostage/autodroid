package com.iknorite.android.test;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidget extends AppWidgetProvider
{
	public static final String REFRESH = "com.iknorite.android.test.mywidget.REFRESH";
	public static boolean isEnabled = false;

	@Override
	public void onReceive(Context ctxt, Intent intent)
	{
		if (REFRESH.equals(intent.getAction()))
		{
			ctxt.startService(new Intent(ctxt, MyWidgetService.class));
		}
		else
		{
			super.onReceive(ctxt, intent);
		}
	}

	@Override
	public void onUpdate(Context ctxt, AppWidgetManager mgr, int[] appWidgetIds)
	{
		ctxt.startService(new Intent(ctxt, MyWidgetService.class));
	}

	public static void enableAutoResponse(Context context)
	{
		SmsReceiver.isEnabled = isEnabled;
	}

	public static class MyWidgetService extends IntentService
	{
		private SharedPreferences prefs = null;

		public MyWidgetService()
		{
			super("MyWidget$MyWidgetService");
		}

		@Override
		public void onCreate()
		{
			super.onCreate();

			prefs = PreferenceManager.getDefaultSharedPreferences(this);
		}

		@Override
		public void onHandleIntent(Intent intent)
		{
			ComponentName me = new ComponentName(this, MyWidget.class);
			AppWidgetManager mgr = AppWidgetManager.getInstance(this);

			mgr.updateAppWidget(me, buildUpdate(this));
		}

		private RemoteViews buildUpdate(Context context)
		{
			RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.mywidget);

			isEnabled = !isEnabled;
			enableAutoResponse(context);

			if(isEnabled)
				updateViews.setImageViewResource(R.id.bttn01, R.drawable.enabled);
			else
				updateViews.setImageViewResource(R.id.bttn01, R.drawable.disabled);


			Intent i = new Intent(this, MyWidget.class);
			i.setAction(REFRESH);

			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i ,0);

			updateViews.setOnClickPendingIntent(R.id.bttn01, pi);

			return (updateViews);
		}
	}
}
