package baking.kondeg.udacity.edu.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.activities.DetailActivity;
import baking.kondeg.udacity.edu.app.data.Ingredient;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.data.Recipe;
import baking.kondeg.udacity.edu.app.utils.EndpointValues;
import baking.kondeg.udacity.edu.app.utils.RecipeWidgetService;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DetailFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DetailRecyclerViewAdapter.ListItemClickListener mListener;
    private Recipe selectedRecipe;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DetailFragment newInstance(int columnCount) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mListener = (DetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState!=null) {
            selectedRecipe = savedInstanceState.getParcelable(EndpointValues.selectedRecipe);
        } else {
            selectedRecipe = getArguments().getParcelable(EndpointValues.selectedRecipe);
        }
        TextView ingredients = view.findViewById(R.id.recipe_ingredients);
        ingredients.setText(getIngredientString(selectedRecipe.getIngredients()));

        RecyclerView recyclerView = view.findViewById(R.id.recipe_detail_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        DetailRecyclerViewAdapter mRecipeDetailAdapter =new DetailRecyclerViewAdapter(selectedRecipe.getInstructions(), mListener);
        recyclerView.setAdapter(mRecipeDetailAdapter);

        RecipeWidgetService.startWidget(getContext(), selectedRecipe.getIngredients());

        return view;
    }

    private String getIngredientString(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i<ingredients.size(); i++) {
            builder.append(ingredients.get(i).getIngredient().substring(0, 1).toUpperCase()+ingredients.get(i).getIngredient().substring(1));
            builder.append(" ("+ingredients.get(i).getQuantity().toString()+" ");
            builder.append(ingredients.get(i).getMeasure()+")\n");
        }
        return builder.toString();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "save instance state fragment detail");
        savedInstanceState.putParcelable(EndpointValues.selectedRecipe, selectedRecipe);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PreparationInstruction item);
    }
}
