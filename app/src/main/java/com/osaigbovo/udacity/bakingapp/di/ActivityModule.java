package com.osaigbovo.udacity.bakingapp.di;

import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.RecipeDetailActivity;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.MainActivity;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoActivity;
import com.osaigbovo.udacity.bakingapp.widget.BakingAppWidgetConfigure;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all sub-components within the app. Add bindings for other sub-components here.
 *
 * @author Osaigbovo Odiase.
 * @ContributesAndroidInjector was introduced removing the need to:
 * a) Create separate components annotated with @Subcomponent (the need to define @Subcomponent classes.)
 * b) Write custom annotations like @PerActivity.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract RecipeDetailActivity contributeRecipeDetailActivity();

    @ContributesAndroidInjector()
    abstract StepInfoActivity contributeStepInfoActivity();

    @ContributesAndroidInjector
    abstract BakingAppWidgetConfigure contributeBakingAppWidgetConfigure();

}
