package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.utils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Creates a one off view model factory for the given view model instance.
 * Taken from
 * https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
public class ViewModelUtil {
    private ViewModelUtil() {}
    public static <T extends ViewModel> ViewModelProvider.Factory createFor(T model) {
        return new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                if (modelClass.isAssignableFrom(model.getClass())) {
                    return (T) model;
                }
                throw new IllegalArgumentException("unexpected model class " + modelClass);
            }
        };
    }
}
