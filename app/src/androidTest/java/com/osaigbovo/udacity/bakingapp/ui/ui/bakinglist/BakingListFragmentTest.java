package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.utils.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.fragment.app.testing.FragmentScenario.launchInContainer;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) //AndroidJUnit4.class
public class BakingListFragmentTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();

    private BakingListViewModel bakingListViewModel;

    ViewModelProvider.Factory viewModelFactory;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void testBakingListFragment() {
        //MockitoAnnotations.initMocks(this);

        BakingListFragment bakingListFragment = new BakingListFragment();

        when(bakingListViewModel.getRecipesLiveData()).thenReturn(recipes);

        bakingListFragment.viewModelFactory = ViewModelUtil.createFor(bakingListViewModel);

        FragmentScenario scenario = FragmentScenario.launchInContainer(BakingListFragment.class);

        //noinspection unchecked
        /*scenario.onFragment(new FragmentScenario.FragmentAction() {
            @Override
            public void perform(@NonNull Fragment fragment) {
                BakingListFragment bakingListFragment = (BakingListFragment) fragment;


            }
        });*/

        //onView(withId(R.id.baking_list)).check(matches(isDisplayed()));

    }



   @Test
    public void aloadError() {

        //recipes.postValue(new ArrayList());

        //onView(withId(R.id.baking_list)).check(matches(isDisplayed()));
        //onView(withId(R.id.error_textview)).check(matches(isDisplayed()));
        //onView(withId(R.id.pb_loading_indicator)).check(matches(not(isDisplayed())));
    }

}
