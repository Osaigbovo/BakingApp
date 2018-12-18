package com.osaigbovo.udacity.bakingapp.data.local.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.osaigbovo.udacity.bakingapp.data.model.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

/**
 * TypeConverter which persists Steps type into a known database type.
 *
 * @author Osaigbovo Odiase.
 */
public class StepConverter {

    @TypeConverter
    public static ArrayList<Step> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Step>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Step> genres) {
        Gson gson = new Gson();
        return gson.toJson(genres);
    }
}
