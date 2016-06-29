package com.skyllabler.onboardercoordinator.view.behaviour;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.skyllabler.onboardercoordinator.view.widget.OnBoarderCoordinatorViewPager;
import com.skyllabler.onboardercoordinator.view.widget.OnBoarderPageLayout;

public abstract class OnBoarderPageBehaviour {

    private final static int NO_DESTINY_VIEW = -1;
    protected OnBoarderCoordinatorViewPager coordinatorLayout;
    private OnBoarderPageLayout page;
    private View targetView;
    private View destinyView;

    protected abstract void onCreate(OnBoarderCoordinatorViewPager coordinator);

    public abstract void onPlaytimeChange(OnBoarderCoordinatorViewPager coordinator, float newPlaytime, float newScrollPosition);

    protected OnBoarderPageLayout getPage() {
        return page;
    }

    public void setPage(OnBoarderPageLayout page) {
        this.page = page;
    }

    protected View getTargetView() {
        return targetView;
    }

    protected View getDestinyView() {
        if (targetView != null && destinyView == null && coordinatorLayout != null) {
            int destinyViewId = ((OnBoarderPageLayout.LayoutParams) targetView.getLayoutParams()).getDestinyViewId();
            if (destinyViewId != NO_DESTINY_VIEW) {
                destinyView = coordinatorLayout.findViewById(destinyViewId);
                int c = coordinatorLayout.getChildCount();
                Log.d(OnBoarderPageBehaviour.class.getSimpleName(),"Child Count" +c);
                Log.d(OnBoarderPageBehaviour.class.getSimpleName(),"Destiny Id is not Null" +destinyViewId);
            }
        }
        return destinyView;
    }

    public void setCoordinator(OnBoarderCoordinatorViewPager coordinator) {
        this.coordinatorLayout = coordinator;
        this.coordinatorLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onCreate(coordinatorLayout);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            coordinatorLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            coordinatorLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    public void setTarget(View target) {
        this.targetView = target;
    }
}
