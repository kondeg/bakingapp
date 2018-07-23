package baking.kondeg.udacity.edu.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.data.Ingredient;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.data.Recipe;
import baking.kondeg.udacity.edu.app.utils.EndpointValues;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeListFragment extends Fragment {
    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecipeListRecyclerViewAdapter adapter;
    private ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecipeListFragment newInstance(int columnCount) {
        RecipeListFragment fragment = new RecipeListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recipes);
        // Set the adapter
        RecyclerView.LayoutManager mLayoutManager = null;
        if (recyclerView !=null) {
            if (view.getTag()!=null && view.getTag().equals("tablet")){
                mLayoutManager = new GridLayoutManager(getContext(),3);
                recyclerView.setLayoutManager(mLayoutManager);
            }
            else {
                mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
            }
            recyclerView.setLayoutManager(mLayoutManager);
            if (savedInstanceState!=null && savedInstanceState.getParcelableArrayList(EndpointValues.recipeList)!=null) {
                mRecipes = savedInstanceState.getParcelableArrayList(EndpointValues.recipeList);
            } else {
                queryRecipeApi();
            }
            adapter = new RecipeListRecyclerViewAdapter(mRecipes);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "save instance state fragment");
        savedInstanceState.putParcelableArrayList(EndpointValues.recipeList, mRecipes);
        super.onSaveInstanceState(savedInstanceState);
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
        void onListFragmentInteraction(Recipe item);
    }


    private void queryRecipeApi() {
        final JsonArrayRequest mJsonObjectRequest = new JsonArrayRequest
                (EndpointValues.recipeEndpoint, new Response.Listener<JSONArray>()  {
                    @Override
                    public void onResponse(JSONArray jsonObject) {
                        try {
                            String id;
                            String name;
                            Recipe recipeVal;
                            Integer servings;
                            String image;
                            ArrayList<Ingredient> ingredients;
                            ArrayList<PreparationInstruction> steps;
                            if (mRecipes == null) {
                                mRecipes = new ArrayList<Recipe>();
                            } else {
                                mRecipes.clear();
                            }
                            // TODO: Loop through the array
                            for (int i = 0; i < jsonObject.length(); i++) {
                                JSONObject recipe = jsonObject.getJSONObject(i);
                                id = recipe.getString("id");
                                name = recipe.getString("name");
                                servings = recipe.getInt("servings");
                                image = recipe.getString("image");
                                recipeVal = new Recipe();
                                recipeVal.setId(id);
                                recipeVal.setName(name);
                                recipeVal.setServings(servings);
                                recipeVal.setImage(image);
                                ingredients = new ArrayList<Ingredient>();
                                steps = new ArrayList<>();
                                JSONArray ingredArray = recipe.getJSONArray("ingredients");
                                for (int n = 0; n < ingredArray.length(); n++) {
                                    JSONObject ingred = ingredArray.getJSONObject(n);
                                    Ingredient in = new Ingredient();
                                    in.setId(Integer.toString(n));
                                    in.setIngredient(ingred.getString("ingredient"));
                                    in.setMeasure(ingred.getString("measure"));
                                    in.setQuantity(ingred.getDouble("quantity"));
                                    ingredients.add(in);
                                }
                                recipeVal.setIngredients(ingredients);
                                JSONArray stepArray = recipe.getJSONArray("steps");
                                for (int n = 0; n < stepArray.length(); n++) {
                                    JSONObject step = stepArray.getJSONObject(n);
                                    PreparationInstruction instruction = new PreparationInstruction();
                                    instruction.setId(step.getString("id"));
                                    instruction.setShortDescription(step.getString("shortDescription"));
                                    instruction.setDescription(step.getString("description"));
                                    instruction.setVideoURL(step.getString("videoURL"));
                                    steps.add(instruction);
                                }
                                recipeVal.setInstructions(steps);
                                mRecipes.add(recipeVal);
                            }
                            Log.d(LOG_TAG, "Recipe size "+mRecipes.size());
                            adapter.notifyDataSetChanged();

                        } catch (JSONException ex) {
                            Log.e(LOG_TAG, ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(LOG_TAG, error.getMessage());
                    }
                });

        // Queue the async request
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(mJsonObjectRequest);
    }
}
