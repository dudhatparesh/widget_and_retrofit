package com.enggdream.widgetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Paresh on 4/13/2017.
 */

public class CustomWidgetProvider extends AppWidgetProvider {
    private static final String TAG = CustomWidgetProvider.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent widgetIntent) {
        final String action = widgetIntent.getAction();
        //Toast.makeText(context, "action received: " + action, Toast.LENGTH_SHORT).show();
        super.onReceive(context, widgetIntent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //Toast.makeText(context, "onDisabled():last widget instance removed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //Toast.makeText(context, "Enabled widget", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,
                CustomWidgetProvider.class);
        //Toast.makeText(context, "onUpdate called at " + new Date().toString(), Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = context.getSharedPreferences("MY", Context.MODE_PRIVATE);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        remoteViews.setTextViewText(R.id.tv_text,
                preferences.getString("HASHRATE", "Loading Data"));
        ComponentName thiswidget = new ComponentName(context, CustomWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);


        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 3 seconds
        am.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 3000, pi);
    }
}
