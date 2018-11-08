package com.osaigbovo.udacity.bakingapp.util;

import com.osaigbovo.udacity.bakingapp.data.remote.RequestInterface;
import com.osaigbovo.udacity.bakingapp.data.remote.ServiceGenerator;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;

public class Injection {

    public static RecipeRepository provideRecipeRepository() {
        return new RecipeRepository();
    }

    public static RequestInterface provideRecipeService() {
        return ServiceGenerator.createService(RequestInterface.class);
    }

}
