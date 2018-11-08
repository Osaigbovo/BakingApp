package com.osaigbovo.udacity.bakingapp.data.repository;

import com.osaigbovo.udacity.bakingapp.data.NetworkBoundSource;
import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.remote.RequestInterface;
import com.osaigbovo.udacity.bakingapp.data.remote.ServiceGenerator;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RecipeRepository {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RequestInterface requestInterface;

    public RecipeRepository(/*RequestInterface requestInterface*/) {

        //this.requestInterface = requestInterface;
    }

    public Observable<List<Recipe>> getRecipe() {
        requestInterface = ServiceGenerator.createService(RequestInterface.class);
        return requestInterface.getRecipes();

        /*compositeDisposable
                .add(requestInterface.getRecipes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Response<List<Recipe>>>() {
                            @Override
                            public void onNext(Response<List<Recipe>> listResponse) {
                                List<Recipe> recipes = listResponse.body();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        })
                );*/

    }

/*
    public Flowable<Resource<List<Recipe>>> getRecipes() {
        return Flowable.create(emitter -> new NetworkBoundSource<List<Recipe>, List<Recipe>>(emitter) {
            @Override
            public Observable<List<Recipe>> getRemote() {
                requestInterface = ServiceGenerator.createService(RequestInterface.class);
                return requestInterface.getRecipes();
            }

            @Override
            public Flowable<List<Recipe>> getLocal() {
                return null;
            }

            @Override
            public void saveCallResult(@NonNull List<Recipe> data) {

            }

            *//*@Override
            public Function<List<Movie>, List<TopMoviesEntity>> mapper() {
                return EntityMapper.convert();
            }*//*
        }, BackpressureStrategy.BUFFER);
    }*/
}
