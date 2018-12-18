package com.osaigbovo.udacity.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import java.util.Set;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;

// Activity that allows the user to configure settings when he or she adds a new App Widget,
public class BakingAppWidgetConfigure extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final String TAG = BakingAppWidgetConfigure.class.getSimpleName();

    @Inject
    Context context;
    @Inject
    BakingAppWidgetUtil bakingAppWidgetUtil;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private CompositeDisposable compositeDisposable;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.radio_group_options)
    RadioGroup recipesRadioGroup;
    @BindString(R.string.widget_config_no_data)
    String noDataErrorMessage;

    public static Recipe recipeL;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // If back button is pressed cancel the widget host from making the widget placement.
        setResult(RESULT_CANCELED);
        // Set the view layout resource to use.
        setContentView(R.layout.app_widget_configure);
        ButterKnife.bind(this);

        compositeDisposable = new CompositeDisposable();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // An intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        displayRecipeNames();
    }

    public void displayRecipeNames() {
        // Get Recipe names from SharedPreferences.
        Set<String> names = bakingAppWidgetUtil.getRecipeNamesFromPrefs();
        if (names.size() == 0) {
            Toast.makeText(this, noDataErrorMessage, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Populate the RadioGroup with radio buttons.
        int currentIndex = 0;
        ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.RadioButton);
        AppCompatRadioButton button;
        for (String name : names) {
            button = new AppCompatRadioButton(newContext);
            button.setText(name);

            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewCompat.setLayoutDirection(button,
                    ViewCompat.getLayoutDirection(button) == ViewCompat.LAYOUT_DIRECTION_LTR ?
                            ViewCompat.LAYOUT_DIRECTION_RTL : ViewCompat.LAYOUT_DIRECTION_LTR);

            button.setId(currentIndex++);
            recipesRadioGroup.addView(button);

        }
        // Check the first RadioButton when loaded
        if (recipesRadioGroup.getChildCount() > 0) {
            ((AppCompatRadioButton) recipesRadioGroup.getChildAt(0)).setChecked(true);
        }

    }

    @OnClick(R.id.save_button)
    public void saveButton() {

        // When the button is clicked, save the string in our prefs.
        int checkedItemId = recipesRadioGroup.getCheckedRadioButtonId();
        String recipeName = ((AppCompatRadioButton) recipesRadioGroup
                .getChildAt(checkedItemId)).getText().toString();

        bakingAppWidgetUtil.saveRecipeNameToPrefs(mAppWidgetId, recipeName);

        // Push widget update to surface with newly set choice
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        compositeDisposable.add(bakingAppWidgetUtil
                .getRecipe(recipeName)
                .map(recipe -> {
                    recipeL = recipe;
                    return recipe.getIngredients();
                })
                .subscribe(ingredients -> BakingAppWidgetProvider
                                .updateAppWidget(context,
                                        appWidgetManager, mAppWidgetId,
                                        recipeName, ingredients, recipeL),
                        throwable -> Log.w(TAG, "Error: unable to populate widget data.")));

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}
