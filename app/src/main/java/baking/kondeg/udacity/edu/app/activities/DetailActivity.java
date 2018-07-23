package baking.kondeg.udacity.edu.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.data.PreparationInstruction;
import baking.kondeg.udacity.edu.app.data.Recipe;
import baking.kondeg.udacity.edu.app.fragments.DetailFragment;
import baking.kondeg.udacity.edu.app.fragments.DetailRecyclerViewAdapter;
import baking.kondeg.udacity.edu.app.fragments.PreparationStepFragment;
import baking.kondeg.udacity.edu.app.fragments.RecipeListFragment;
import baking.kondeg.udacity.edu.app.utils.EndpointValues;

public class DetailActivity extends AppCompatActivity implements DetailRecyclerViewAdapter.ListItemClickListener, PreparationStepFragment.ListItemClickListener {

    private Fragment mContent;
    private Fragment stepFragment;

    private Recipe selectedRecipe;

    private final static String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        setToolbarListener(toolbar);

        if (savedInstanceState == null) {
            selectedRecipe = getIntent().getExtras().getParcelable(EndpointValues.selectedRecipe);
            getSupportActionBar().setTitle(selectedRecipe.getName());
            if((savedInstanceState==null)) {
                mContent = new DetailFragment();
                mContent.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_content, mContent)
                        .commit();
            }
        }  else {
            selectedRecipe = savedInstanceState.getParcelable(getResources().getString(R.string.recipe));
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, getResources().getString(R.string.stepFragment));
            stepFragment = getSupportFragmentManager().getFragment(savedInstanceState, getResources().getString(R.string.stepDetailFragment));
            int containerId = savedInstanceState.getInt(getResources().getString(R.string.containerId));
            Log.d(LOG_TAG, "restoreFragment "+(mContent==null)+(stepFragment==null));
            if (mContent!=null && (stepFragment==null || findViewById(R.id.detail_coordinator_layout)!=null)) {
                Log.d(LOG_TAG, "restoreMFragment");
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_content, mContent).commit();
            } else if (mContent==null && findViewById(R.id.detail_coordinator_layout)!=null) {
                mContent = new DetailFragment();
                mContent.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_content, mContent)
                        .commit();

            }
            if (stepFragment!=null && containerId>0) {
                Log.d(LOG_TAG, "restoreStepFragment");
                Log.d(LOG_TAG, "Contrainer Id "+containerId);
                getSupportFragmentManager().beginTransaction().replace(containerId, stepFragment).commit();
            }
        }
    }

    private void setToolbarListener(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "toolbarClickListener");
                if (findViewById(R.id.prep_step_layout) != null) {
                    Log.d(LOG_TAG, "toolbarClickListenerPrepStep");
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(EndpointValues.selectedRecipe, selectedRecipe);
                    v.getContext().startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "toolbarClickListenerDetail");
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onListItemClick(List<PreparationInstruction> steps, int clickedItemIndex) {
        stepFragment = new PreparationStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EndpointValues.selectedRecipe, selectedRecipe);
        bundle.putInt(EndpointValues.itemClicked, clickedItemIndex);
        stepFragment.setArguments(bundle);
        if (findViewById(R.id.detail_coordinator_layout)!=null && findViewById(R.id.detail_coordinator_layout).getTag()!=null && findViewById(R.id.detail_coordinator_layout).getTag().equals("tablet")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_content_steps, stepFragment).commit();

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_content, stepFragment).commit();
            mContent =null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "activityOnSaveInstanceState");
        if (mContent!=null) {
            getSupportFragmentManager().putFragment(savedInstanceState, getResources().getString(R.string.stepFragment), mContent);
        }
        if(stepFragment!=null) {
            getSupportFragmentManager().putFragment(savedInstanceState, getResources().getString(R.string.stepDetailFragment), stepFragment);
            ViewGroup vg = ((ViewGroup)stepFragment.getView().getParent());
            if (vg!=null) {
                savedInstanceState.putInt(getResources().getString(R.string.containerId), vg.getId());
            }
        }
        savedInstanceState.putParcelable(getResources().getString(R.string.recipe), selectedRecipe);
        super.onSaveInstanceState(savedInstanceState);
    }


}
