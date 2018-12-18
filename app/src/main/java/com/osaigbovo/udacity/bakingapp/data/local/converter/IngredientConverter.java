package com.osaigbovo.udacity.bakingapp.data.local.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.osaigbovo.udacity.bakingapp.data.model.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

/**
 * TypeConverter which persists Ingredient type into a known database type.
 *
 * @author Osaigbovo Odiase.
 */
public class IngredientConverter {

    @TypeConverter
    public static ArrayList<Ingredient> convertStringToList(String ingredientString) {
        Type listType = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        return new Gson().fromJson(ingredientString, listType);
    }

    @TypeConverter
    public static String convertListToString(ArrayList<Ingredient> videos) {
        Gson gson = new Gson();
        return gson.toJson(videos);
    }
}
