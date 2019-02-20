package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist;

import android.util.Log;

import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class BakingListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LiveData<Resource<List<Recipe>>> observer;

    @Mock
    private BakingListViewModel bakingListViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void fetchRecipeNames() throws NullPointerException{

        Recipe recipe = new Recipe(1, "Cake", new ArrayList<>(), new ArrayList<>(), 8, "cakes.jpg");

        when(bakingListViewModel.getRecipesLiveData()).thenReturn(observer);

        //observer.getValue().data.add(recipe);

        assert(bakingListViewModel.getRecipesLiveData().getValue().data.size() > 1);

        //assertThat(bakingListViewModel.getRecipesLiveData().getValue().data.isEmpty());
    }


}
