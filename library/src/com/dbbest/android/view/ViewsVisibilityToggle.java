package com.dbbest.android.view;

import android.view.View;
import com.dbbest.framework.patterns.ObjectsStatesManager;
import com.dbbest.framework.patterns.StateChanger;
import com.dbbest.framework.patterns.StateProvider;
import com.dbbest.framework.patterns.StateRestorer;

/**
 * User: Tikhonenko.S
 * Date: 26.12.13
 * Time: 15:17
 */
public class ViewsVisibilityToggle {
    private Iterable<View> views;
    private ObjectsStatesManager<View, Integer> statesManager =
            new ObjectsStatesManager<View, Integer>(new ViewVisibilityStateProvider());

    private boolean visibilityChanged = false;
    private StateRestorer visibilityRestorer;

    public ViewsVisibilityToggle(Iterable<View> views) {
        this.views = views;
    }

    public void setVisibility(final int visibility){
        if(visibilityChanged){
            return;
        }

        visibilityRestorer = statesManager.changeStates(views, new StateChanger<View>() {
            @Override
            public void changeState(View view) {
                view.setVisibility(visibility);
            }
        });

        visibilityChanged = true;
    }

    public void restoreVisibility(){
        if(!visibilityChanged){
            return;
        }

        visibilityRestorer.restore();
        visibilityChanged = false;
    }
}
