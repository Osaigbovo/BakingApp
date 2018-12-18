package com.osaigbovo.udacity.bakingapp.data.remote;

import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Interface that communicates with API using Retrofit 2 and RxJava 2.
 *
 * @author Osaigbovo Odiase.
 */
public interface RequestInterface {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<List<Recipe>> getRecipes();

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipe>> getRecipess();

}
