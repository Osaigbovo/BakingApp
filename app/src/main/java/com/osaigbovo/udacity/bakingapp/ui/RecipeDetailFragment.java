package com.osaigbovo.udacity.bakingapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Recipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single Recipe detail screen. This fragment is either contained in a
 * {@link MainActivity} in two-pane mode (on tablets) or a {@link RecipeDetailActivity} on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String ARG_RECIPE = "recipe";

    private Recipe recipe;

    @BindView(R.id.item_detail)
    TextView textView;

    private Unbinder unbinder;

    public RecipeDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_RECIPE)) {
            recipe = getArguments().getParcelable(ARG_RECIPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (recipe != null) {
            textView.setText(recipe.getName());
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
