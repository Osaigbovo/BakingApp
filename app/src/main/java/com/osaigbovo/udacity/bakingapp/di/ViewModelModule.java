package com.osaigbovo.udacity.bakingapp.di;

import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.BakingListViewModel;
import com.osaigbovo.udacity.bakingapp.viewmodel.BakingViewModelFactory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/*
 * @author Osaigbovo Odiase.
 * */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BakingListViewModel.class)
    abstract ViewModel bindMoviesListViewModel(BakingListViewModel bakingListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(BakingViewModelFactory factory);
}
