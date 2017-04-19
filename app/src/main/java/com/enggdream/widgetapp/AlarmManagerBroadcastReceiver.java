package com.enggdream.widgetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paresh on 4/13/2017.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //Toast.makeText(context, "Alarm Manager fired at " + new Date().toString(), Toast.LENGTH_SHORT).show();
        /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");*/
        //Acquire the lock
        //wl.acquire();
        Calendar calendar = new GregorianCalendar(2017, 3, 14, 0, 0, 0);
        Calendar todaysCalendar = new GregorianCalendar();
        if (calendar.before(todaysCalendar)) {/*
            Toast.makeText(context, "Product expired", Toast.LENGTH_SHORT).show();*/
            updateWidgetText(context, String.format("Please purchase app"));
        } else {

            //Toast.makeText(context, "Product valid", Toast.LENGTH_SHORT).show();
            ApiService gitHubService = ApiService.retrofit.create(ApiService.class);
            Call<List<Data>> call = gitHubService.getData();
            call.enqueue(new Callback<List<Data>>() {
                @Override
                public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                    //You can do the processing here update the widget/remote views.
                    if (response.isSuccessful()) {
                        String hashRate = response.body().get(response.body().size() - 1).getHashRate();
                        Double longHashRate = Double.parseDouble(hashRate);
                        longHashRate = longHashRate / 1_000_000_000_000L;
                        updateWidgetText(context, String.format("%.3f PH/s", longHashRate));
                        ////Toast.makeText(context, "HashRate updated", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = context.getSharedPreferences("MY_PREF",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("HASHRATE", hashRate);
                        editor.commit();
                    }

                }

                @Override
                public void onFailure(Call<List<Data>> call, Throwable t) {
                    //Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        //Release the lock
        //wl.release();

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent demointent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, demointent, 0);
        //After after 3 seconds
        am.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 60000, pi);
    }

    void updateWidgetText(Context context, String text) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        remoteViews.setTextViewText(R.id.tv_text, text);
        ComponentName thiswidget = new ComponentName(context, CustomWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thiswidget, remoteViews);
    }
}
