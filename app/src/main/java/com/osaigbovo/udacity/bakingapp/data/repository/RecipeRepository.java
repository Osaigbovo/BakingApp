package com.osaigbovo.udacity.bakingapp.data.repository;

import com.osaigbovo.udacity.bakingapp.data.NetworkBoundSource;
import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.local.dao.RecipesDao;
import com.osaigbovo.udacity.bakingapp.data.local.preferences.SharedPreferencesUtil;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.remote.RequestInterface;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
* Repository that communicates with data sources:
* Room Database, API and Shared Preference.
* */
@Singleton
public class RecipeRepository {

    private final RequestInterface requestInterface;
    private final RecipesDao recipesDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    @Inject
    RecipeRepository(RecipesDao recipesDao, RequestInterface requestInterface,
                     SharedPreferencesUtil sharedPreferencesUtil) {
        this.requestInterface = requestInterface;
        this.recipesDao = recipesDao;
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }

    public Flowable<Resource<List<Recipe>>> getRecipes() {
        return Flowable.create(emitter -> new NetworkBoundSource<List<Recipe>, List<Recipe>>(emitter) {
            @Override
            public Single<List<Recipe>> getRemote() {
                return requestInterface.getRecipess();
            }

            @Override
            public Flowable<List<Recipe>> getLocal() {
                return recipesDao.getRecipes();
            }

            @Override
            public void saveCallResult(@NonNull List<Recipe> recipeList) {
                recipesDao.saveRecipes(recipeList);
            }

            @Override
            public void saveCallPref(@NonNull List<Recipe> recipeList) {
                sharedPreferencesUtil.saveRecipeNamesList(recipeList);
            }

            @Override
            public Function<List<Recipe>, List<Recipe>> mapper() {
                return recipeList -> recipeList;
            }
        }, BackpressureStrategy.BUFFER);
    }

    public Observable<Recipe> getRecipe(String recipeName) {
        return recipesDao
                .getRecipe(recipeName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public SharedPreferencesUtil getSharedPreferences() {
        return sharedPreferencesUtil;
    }

}
