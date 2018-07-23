package baking.kondeg.udacity.edu.app.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.fragments.RecipeListFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment mContent;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        if((savedInstanceState==null)) {
            mContent = new RecipeListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.landing_page, mContent)
                    .commit();
        }
    }

}
