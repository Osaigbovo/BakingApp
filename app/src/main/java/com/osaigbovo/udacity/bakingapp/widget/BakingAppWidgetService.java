package com.osaigbovo.udacity.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingAppWidgetService extends RemoteViewsService {

    public BakingAppWidgetService() {
        super();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
