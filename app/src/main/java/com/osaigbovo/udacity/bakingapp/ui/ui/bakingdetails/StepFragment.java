package com.osaigbovo.udacity.bakingapp.ui.ui.bakingdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osaigbovo.udacity.bakingapp.R;
import com.osaigbovo.udacity.bakingapp.data.model.Ingredient;
import com.osaigbovo.udacity.bakingapp.data.model.Step;
import com.osaigbovo.udacity.bakingapp.data.repository.RecipeRepository;
import com.osaigbovo.udacity.bakingapp.di.Injectable;
import com.osaigbovo.udacity.bakingapp.ui.adapter.StepsAdapter;
import com.osaigbovo.udacity.bakingapp.ui.ui.bakinglist.MainActivity;
import com.osaigbovo.udacity.bakingapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import iammert.com.expandablelib.ExpandCollapseListener;
import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

/**
 * A fragment representing a single Recipe detail screen. This fragment is either contained in a
 * {@link MainActivity} in two-pane mode (on tablets) or a {@link RecipeDetailActivity} on handsets.
 */
public class StepFragment extends Fragment implements Injectable {

    private static final String ARG_STEPS = "step";
    private static final String ARG_INGREDIENTS = "ingredient";

    private ArrayList<Step> steps;
    private ArrayList<Ingredient> ingredients;
    private StepsAdapter.OnStepSelectedListener mListener;

    @BindView(R.id.expandable_ingredients) ExpandableLayout sectionLinearLayout;
    @BindView(R.id.recycler_view_steps) RecyclerView stepsRecyclerView;
    @BindString(R.string.ingredient_label) String ingredientLabel;

    @Inject
    RecipeRepository recipeRepository;

    private Unbinder unbinder;

    public StepFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (StepsAdapter.OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepSelectedListener!");
        }
    }

    static StepFragment newInstance(ArrayList<Step> steps, ArrayList<Ingredient> ingredients) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_STEPS, steps);
        args.putParcelableArrayList(ARG_INGREDIENTS, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if (extras != null && extras.containsKey(ARG_STEPS)) {
            this.steps = extras.getParcelableArrayList(ARG_STEPS);
            this.ingredients = extras.getParcelableArrayList(ARG_INGREDIENTS);
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setIngredientsUI();
        setStepsUI();

        return rootView;
    }

    private void setIngredientsUI() {
        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<Ingredient, Ingredient>() {
            @Override
            public void renderParent(View view, Ingredient model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.text_parent)).setText(ingredientLabel);
                view.findViewById(R.id.image_arrow)
                        .setBackgroundResource(isExpanded ? R.drawable.arrow_up : R.drawable.arrow_down);
            }

            @Override
            public void renderChild(View view, @NonNull Ingredient model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.text_child_ingredient))
                        .setText(ViewUtils
                                .capitaliseFirstLetter(Objects.requireNonNull(model.getIngredient())));
                ((TextView) view.findViewById(R.id.text_child_quantity))
                        .setText(String.valueOf(model.getQuantity()));
                ((TextView) view.findViewById(R.id.text_child_measure))
                        .setText(Objects.requireNonNull(model.getMeasure()).toLowerCase());
            }
        });
        sectionLinearLayout.addSection(getSection());
        sectionLinearLayout.setCollapseListener((ExpandCollapseListener.CollapseListener<Ingredient>)
                (parentIndex, parent, view) -> view.findViewById(R.id.image_arrow)
                        .setBackgroundResource(R.drawable.arrow_down));
        sectionLinearLayout.setExpandListener((ExpandCollapseListener.ExpandListener<Ingredient>)
                (parentIndex, parent, view) -> view.findViewById(R.id.image_arrow)
                        .setBackgroundResource(R.drawable.arrow_up));
    }

    private void setStepsUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        stepsRecyclerView.setLayoutManager(layoutManager);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(new StepsAdapter(steps, mListener));
    }

    private Section<Ingredient, Ingredient> getSection() {
        Section<Ingredient, Ingredient> section = new Section<>();
        section.parent = ingredients.get(0);
        section.children.addAll(ingredients);
        section.expanded = false;
        return section;
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

}
