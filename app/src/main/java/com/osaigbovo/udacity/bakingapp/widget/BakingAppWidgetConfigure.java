package com.osaigbovo.udacity.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.osaigbovo.udacity.bakingapp.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

// Activity that allows the user to configure settings when he or she adds a new App Widget,
public class BakingAppWidgetConfigure extends AppCompatActivity {

    private static final String TAG = BakingAppWidgetConfigure.class.getSimpleName();
    private static final String PREFS_NAME
            = "com.osaigbovo.udacity.bakingapp.widget.BakingAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetPrefix;

    public BakingAppWidgetConfigure() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // If back button is pressed cancel the widget host from making the widget placement.
        setResult(RESULT_CANCELED);

        // Set the view layout resource to use.
        setContentView(R.layout.appwidget_configure);

        // Find the EditText
        mAppWidgetPrefix = (EditText) findViewById(R.id.appwidget_prefix);
        // Bind the action for the save button.
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        mAppWidgetPrefix.setText(loadTitlePref(BakingAppWidgetConfigure.this, mAppWidgetId));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BakingAppWidgetConfigure.this;
            // When the button is clicked, save the string in our prefs and return that they clicked OK.
            String titlePrefix = mAppWidgetPrefix.getText().toString();
            saveTitlePref(context, mAppWidgetId, titlePrefix);

            // Push widget update to surface with newly set prefix
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // TODO 1
            //BakingAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, titlePrefix);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return context.getString(R.string.appwidget_prefix_default);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
    }

    static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds,
                                  ArrayList<String> texts) {
    }

}
