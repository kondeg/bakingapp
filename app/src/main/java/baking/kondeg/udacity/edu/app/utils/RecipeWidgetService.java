package baking.kondeg.udacity.edu.app.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import baking.kondeg.udacity.edu.app.R;
import baking.kondeg.udacity.edu.app.data.Ingredient;

public class RecipeWidgetService extends IntentService {

    public RecipeWidgetService() {
        super(RecipeWidgetService.class.getSimpleName());
    }

    public static void startWidget(Context context, ArrayList<Ingredient> ingredientList) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList(context.getResources().getString(R.string.ingredients),ingredientList);
        intent.putExtra("bundle",b);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Ingredient> ingredientList = intent.getBundleExtra("bundle").getParcelableArrayList(getResources().getString(R.string.ingredients));
            handleActionUpdateBakingWidgets(ingredientList);

        }
    }



    private void handleActionUpdateBakingWidgets(ArrayList<Ingredient> ingredientArrayList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        Bundle b = new Bundle();
        b.putParcelableArrayList(getResources().getString(R.string.ingredients),ingredientArrayList);
        intent.putExtra("bundle",b);
        sendBroadcast(intent);
    }
}
