package baking.kondeg.udacity.edu.app.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;

import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.activities.DetailActivity;
import baking.kondeg.udacity.edu.app.data.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static ArrayList<Ingredient> ingredients;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
       // Intent appIntent = new Intent(context, RecipeWidgetItemViewService.class);
       // appIntent.addCategory(Intent.ACTION_MAIN);
       // appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
       //// appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.widget_view, appPendingIntent);
        Intent intent = new Intent(context, RecipeWidgetItemViewService.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList(context.getResources().getString(R.string.ingredients),ingredients);
        intent.putExtra("bundle",b);
        views.setRemoteAdapter(R.id.widget_view, intent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));

        final String action = intent.getAction();

        if (action.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
            ingredients = intent.getBundleExtra("bundle").getParcelableArrayList(context.getResources().getString(R.string.ingredients));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_view);

            RecipeWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
            super.onReceive(context, intent);
        }
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

