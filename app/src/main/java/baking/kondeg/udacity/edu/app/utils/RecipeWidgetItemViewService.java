package baking.kondeg.udacity.edu.app.utils;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeWidgetItemViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteWidgetItemViewFactory(this.getApplicationContext(),intent);
    }

}
