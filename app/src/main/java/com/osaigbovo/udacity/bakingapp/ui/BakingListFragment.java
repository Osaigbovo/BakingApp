package com.osaigbovo.udacity.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.Resource;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;
import com.osaigbovo.udacity.bakingapp.ui.adapter.BakingListAdapter;

import org.reactivestreams.Subscription;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

// Fragment
public class BakingListFragment extends Fragment {

    @BindView(R.id.baking_list)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private BakingListViewModel mViewModel;
    private BakingListAdapter.OnBakingItemSelectedListener mListener;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    RecipeRepository recipeRepository;

    public BakingListFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BakingListAdapter.OnBakingItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnBakingItemSelectedListener!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeRepository = new RecipeRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_baking_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setupRecyclerView();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(BakingListViewModel.class);
    }

    private void setupRecyclerView() {

        mCompositeDisposable.add(recipeRepository.getRecipe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipe>>() {
                    @Override
                    public void accept(List<Recipe> resource) throws Exception {
                        BakingListFragment.this.handleResults(resource);
                    }
                }, this::handleError));
    }

    private void handleResults(List<Recipe> resource) {
        if (resource != null) {

            List<Recipe> recipes = resource;

            recyclerView.setAdapter(new BakingListAdapter(getActivity(), recipes, mListener));

            Log.w("BakingListFragment", String.valueOf(recipes.size()));

        } else {

        }
    }

    private void handleError(Throwable e) {
        e.printStackTrace();
        //Timber.i("In onError()" +e.getMessage());
        //Timber.e(e.getMessage());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.dispose();
        unbinder.unbind();
    }

}
