package com.osaigbovo.udacity.bakingapp.di;

import android.app.Application;
import android.content.Context;

import com.osaigbovo.udacity.bakingapp.data.local.RecipesDatabase;
import com.osaigbovo.udacity.bakingapp.data.local.dao.RecipesDao;
import com.osaigbovo.udacity.bakingapp.data.local.preferences.SharedPreferencesUtil;
import com.osaigbovo.udacity.bakingapp.data.remote.RequestInterface;
import com.osaigbovo.udacity.bakingapp.data.remote.ServiceGenerator;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

/**
 * Application-wide dependencies.
 * Module means the class which contains methods who will provide dependencies.
 *
 * @author Osaigbovo Odiase.
 */
@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    RequestInterface provideRecipesService() {
        return ServiceGenerator.createService(RequestInterface.class);
    }

    @Provides
    @Singleton
    RecipesDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, RecipesDatabase.class, "recipes.db")
                //.addMigrations(RecipesDatabase.MIGRATION_1_2)
                .build();
    }

    @Provides
    @Singleton
    RecipesDao provideRecipesDao(RecipesDatabase db) {
        return db.recipesDao();
    }

    @Provides
    @Singleton
    SharedPreferencesUtil providesSharedPreferences(Context context) {
        return new SharedPreferencesUtil(context);
    }

    /*@Provides
    @Singleton
    CheckConnectionBroadcastReceiver providesCheckConnectionBroadcastReceiver() {
        return new CheckConnectionBroadcastReceiver();
    }*/

}
