package com.osaigbovo.udacity.bakingapp.data.local;

import com.osaigbovo.udacity.bakingapp.data.local.converter.IngredientConverter;
import com.osaigbovo.udacity.bakingapp.data.local.converter.StepConverter;
import com.osaigbovo.udacity.bakingapp.data.local.dao.RecipesDao;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * The Room database that contains the recipes table
 * Define an abstract class that extends RoomDatabase.
 * This class is annotated with @Database, lists the entities contained in the database,
 * and the DAOs which access them.
 */
@Database(entities = {Recipe.class}, version = 1/*, exportSchema = false*/)
@TypeConverters({StepConverter.class, IngredientConverter.class})
public abstract class RecipesDatabase extends RoomDatabase {

    public abstract RecipesDao recipesDao();

}
