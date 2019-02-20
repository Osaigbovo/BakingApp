package com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.di.Injectable;
import com.osaigbovo.udacity.bakingapp.ui.adapter.BakingListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

// Fragment
public class BakingListFragment extends Fragment implements Injectable{

    private static final String ARG_RECIPES_LIST = "recipes_list";

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.baking_list)
    RecyclerView recyclerView;

    private BakingListViewModel bakingListViewModel;
    private List<Recipe> recipes;
    private BakingListAdapter.OnBakingItemSelectedListener mListener;
    //private OnIdlingResourceChangeListener mIdleResourceListener;
    private Unbinder unbinder;

    @VisibleForTesting
    public BakingListFragment() {
    }

    public void setOnRecipeSelectedListener(Activity activity) {
        AndroidSupportInjection.inject(this);
        mListener = (BakingListAdapter.OnBakingItemSelectedListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bakingListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(BakingListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baking_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        //mIdleResourceListener.idlingResourceChangeListener(false);

        if (savedInstanceState != null) {
            this.recipes = savedInstanceState.getParcelableArrayList(ARG_RECIPES_LIST);
            setupRecyclerView(recipes);
        } else {
            bakingListViewModel.getRecipesLiveData().observe(this, resource -> {
                if (resource != null && resource.data.size() > 0) {
                    this.recipes = resource.data;
                    setupRecyclerView(recipes);
                }
            });
        }
        //mIdleResourceListener.idlingResourceChangeListener(true);

        return rootView;
    }

    private void setupRecyclerView(List<Recipe> recipes) {
        recyclerView.setAdapter(new BakingListAdapter(getActivity(), recipes, mListener));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(ARG_RECIPES_LIST, (ArrayList) recipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*public interface OnIdlingResourceChangeListener {
        void idlingResourceChangeListener(boolean countingIdlingResource);
    }*/

}
