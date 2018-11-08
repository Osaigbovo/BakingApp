package com.osaigbovo.udacity.bakingapp.ui;

import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BakingListViewModel extends ViewModel {

    RecipeRepository recipeRepository;

    private LiveData<Resource<List<Recipe>>> recipesLiveData;

    public BakingListViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;

        /*recipesLiveData = LiveDataReactiveStreams
                .fromPublisher(recipeRepository.getRecipes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                );*/

    }

    LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        return recipesLiveData;
    }

    @Override
    protected void onCleared() {

    }

}
