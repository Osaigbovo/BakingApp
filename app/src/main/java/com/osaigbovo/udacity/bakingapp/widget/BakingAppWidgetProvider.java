package com.osaigbovo.udacity.bakingapp.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Ingredient;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity;
import com.osaigbovo.udacity.bakingapp.util.ViewUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

import static com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity.ARG_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = BakingAppWidgetProvider.class.getSimpleName();

    @Inject
    BakingAppWidgetUtil bakingAppWidgetUtil;
    private Recipe recipeL;

    @SuppressLint("CheckResult")
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, String recipeName,
                                       ArrayList<Ingredient> ingredients,
                                       Recipe recipe) {

        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);

        for (Ingredient ingredient : ingredients) {
            RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
                    R.layout.baking_app_widget_ingredients_list_item);

            String line = ViewUtils.formatIngdedient(context,
                    ingredient.getIngredient(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure());

            ingredientView.setTextViewText(R.id.widget_ingredient_name, line);
            views.addView(R.id.widget_ingredients_container, ingredientView);
        }

        // Intent to launch DetailsActivity
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(ARG_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);
        super.onReceive(context, intent);
    }

    /* Called to update the App Widget at intervals defined by the updatePeriodMillis attribute in the
     * AppWidgetProviderInfo. Also called when the user adds the App Widget, so it should perform the
     * essential setup, such as define event handlers for Views and start a temporary Service, if necessary.
     * Method not called if a configuration Activity has been declared when the user adds the App Widget,
     * but is called for the subsequent updates. Responsibility of the configuration Activity to
     * perform the first update when configuration is done.
     * */
    @SuppressLint("CheckResult")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // appWidgetIds an array of IDs that identify each App Widget created by this provider.
        // If the user creates more than one instance of the App Widget, then they are all updated
        // simultaneously.
        for (int appWidgetId : appWidgetIds) {
            String recipeName = bakingAppWidgetUtil.getRecipeNameFromPrefs(appWidgetId);

            //noinspection ResultOfMethodCallIgnored
            bakingAppWidgetUtil
                    .getRecipe(recipeName)
                    .map(recipe -> {
                        recipeL = recipe;
                        return recipe.getIngredients();
                    })
                    .take(1)
                    .subscribe(
                            // OnNext
                            ingredients ->
                                    BakingAppWidgetProvider
                                            .updateAppWidget(context, appWidgetManager,
                                                    appWidgetId, recipeName, ingredients, recipeL),
                            // OnError
                            throwable ->
                                    Timber.d(throwable.getMessage()));

        }

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
        for (int appWidgetId : appWidgetIds) {
            bakingAppWidgetUtil.deleteRecipeFromPrefs(appWidgetId);
        }
    }

}

