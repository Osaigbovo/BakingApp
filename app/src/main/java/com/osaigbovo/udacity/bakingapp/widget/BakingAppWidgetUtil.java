package com.osaigbovo.udacity.bakingapp.widget;

import com.osaigbovo.udacity.bakingapp.data.model.Ingredient;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;

public class BakingAppWidgetUtil {

    private final RecipeRepository recipeRepository;

    @Inject
    public BakingAppWidgetUtil(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    Set<String> getRecipeNamesFromPrefs() {
        return recipeRepository.getSharedPreferences().getRecipeNamesList();
    }

    void deleteRecipeFromPrefs(int widgetId) {
        recipeRepository.getSharedPreferences().deleteRecipeName(widgetId);
    }

    void saveRecipeNameToPrefs(int appWidgetId, String name) {
        recipeRepository.getSharedPreferences().saveChosenRecipeName(appWidgetId, name);
    }

    String getRecipeNameFromPrefs(int appWidgetId) {
        return recipeRepository.getSharedPreferences().getChosenRecipeName(appWidgetId);
    }

    Observable<Recipe> getRecipe(String recipeName) {
        return recipeRepository.getRecipe(recipeName);
    }
}
