package com.osaigbovo.udacity.bakingapp.data.local.dao;

import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Data Access Object that contains methods used for accessing the database.
 *
 * @author Osaigbovo Odiase.
 */
@Dao
public interface RecipesDao {

    @Query("SELECT * FROM recipes")
    Flowable<List<Recipe>> getRecipes();

    @Query("SELECT * from recipes where id = :id")
    Single<Recipe> getRecipe(int id);

    @Query("SELECT * from recipes where name = :name")
    Observable<Recipe> getRecipe(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveRecipes(List<Recipe> recipeList);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Query("DELETE FROM recipes WHERE id=:id")
    void deleteRecipe(int id);
}
