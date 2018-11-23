package com.osaigbovo.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = BakingAppWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // TODO 2 Create an Intent to launch DetailsActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.button, pendingIntent);

        /*CharSequence widgetText = context.getString(R.string.appwidget_text);
        views.setTextViewText(R.id.appwidget_text, widgetText);*/

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    // Called for every broadcast and before each of the callback methods.
    /*@Override
    public void onReceive(Context context, Intent intent) {

    }*/

    /* Called to update the App Widget at intervals defined by the updatePeriodMillis attribute in the
     * AppWidgetProviderInfo. Also called when the user adds the App Widget, so it should perform the
     * essential setup, such as define event handlers for Views and start a temporary Service, if necessary.
     * Method not called if a configuration Activity has been declared when the user adds the App Widget,
     * but is called for the subsequent updates. Responsibility of the configuration Activity to
     * perform the first update when configuration is done.
     * */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Register the event handlers in this callback.
        // appWidgetIds an array of IDs that identify each App Widget created by this provider.
        // If the user creates more than one instance of the App Widget, then they are all updated simultaneously.
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

       super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /*
     * Called when the widget is first placed and any time the widget is resized.
     * Use this callback to show or hide content based on the widget's size ranges. Get the size ranges
     * by calling getAppWidgetOptions(), which returns a Bundle.
     * */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {

    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
    }

    // Called when an instance the App Widget is created for the first time.
    // Functionality that only needs to be setup once.
    @Override
    public void onEnabled(Context context) {
    }

    // Called when the last instance of your App Widget is deleted from the App Widget host.
    // Clean up any work done in onEnabled(Context), such as delete a temporary database.
    @Override
    public void onDisabled(Context context) {
    }

    // Called every time an App Widget is deleted from the App Widget host.
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

}

