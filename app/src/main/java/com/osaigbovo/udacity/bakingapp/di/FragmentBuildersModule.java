package com.osaigbovo.udacity.bakingapp.di;

import com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails.StepFragment;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.BakingListFragment;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakingstep.StepInfoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/*
 * @author Osaigbovo Odiase.
 * */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract BakingListFragment contributeBakingListFragment();

    @ContributesAndroidInjector
    abstract StepFragment contributeStepFragment();

    @ContributesAndroidInjector
    abstract StepInfoFragment contributeStepInfoFragment();

}
