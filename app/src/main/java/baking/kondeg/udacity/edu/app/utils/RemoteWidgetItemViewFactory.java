package baking.kondeg.udacity.edu.app.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.data.Ingredient;

public class RemoteWidgetItemViewFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext = null;

    ArrayList<Ingredient> ingredients;

    private static final String LOG_TAG = RemoteWidgetItemViewFactory.class.getSimpleName();

    public RemoteWidgetItemViewFactory(Context context, Intent intent) {
        mContext = context;
        ingredients = intent.getBundleExtra("bundle").getParcelableArrayList(context.getResources().getString(R.string.ingredients));
        Log.d(LOG_TAG, "Ingredients are null "+(ingredients.size()));
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients==null?0:ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(LOG_TAG, "Ingredients widget "+position+" "+ingredients.get(position).getIngredientString());
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list);

        views.setTextViewText(R.id.widget_grid_item, ingredients.get(position).getIngredientString());

        //Intent fillInIntent = new Intent();
        //fillInIntent.putExtras(extras);
        //views.setOnClickFillInIntent(R.id.widget_grid_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
