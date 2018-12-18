package com.osaigbovo.udacity.bakingapp.data.local.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/*
* Utility class for Shared Preference.
* */
@Singleton
public class SharedPreferencesUtil {

    private static final String PREFS_FILE_NAME = "baking_app_prefs";
    private static final String PREFERENCE_RECIPES = "baking_app_recipes";
    private static final String PREFERENCE_CHOSEN_RECIPE = "baking_app_widget_chosen_recipe_";

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    @SuppressWarnings("Convert2streamapi")
    public void saveRecipeNamesList(List<Recipe> recipes) {

        Set<String> values = new HashSet<>();

        for (Recipe recipe : recipes) {
            values.add(recipe.getName());
        }
        sharedPreferences.edit().putStringSet(PREFERENCE_RECIPES, values).apply();
    }

    public Set<String> getRecipeNamesList() {
        return sharedPreferences.getStringSet(PREFERENCE_RECIPES, new HashSet<>(0));
    }

    public String getChosenRecipeName(int keySuffix) {
        return sharedPreferences.getString(PREFERENCE_CHOSEN_RECIPE + keySuffix, "");
    }

    public void saveChosenRecipeName(int keySuffix, String name) {
        sharedPreferences.edit().putString(PREFERENCE_CHOSEN_RECIPE + keySuffix, name).apply();
    }

    public void deleteRecipeName(int keySuffix) {
        sharedPreferences.edit().remove(PREFERENCE_CHOSEN_RECIPE + keySuffix).apply();
    }

}
