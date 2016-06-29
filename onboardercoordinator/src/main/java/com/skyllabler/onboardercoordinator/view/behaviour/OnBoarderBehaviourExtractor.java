package com.skyllabler.onboardercoordinator.view.behaviour;

import android.view.View;

import com.skyllabler.onboardercoordinator.view.widget.OnBoarderCoordinatorViewPager;
import com.skyllabler.onboardercoordinator.view.widget.OnBoarderPageLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoarderBehaviourExtractor {

    private OnBoarderCoordinatorViewPager mOnBoarderCoordinatorViewPager;

    public OnBoarderBehaviourExtractor(OnBoarderCoordinatorViewPager mOnBoarderCoordinatorViewPager) {
        this.mOnBoarderCoordinatorViewPager = mOnBoarderCoordinatorViewPager;
    }

    public List<OnBoarderPageBehaviour> extractPageBehaviors(View view) {
        List<OnBoarderPageBehaviour> behaviors = new ArrayList<>();
        if (view instanceof OnBoarderPageLayout) {
            final OnBoarderPageLayout pageLayout = (OnBoarderPageLayout) view;
            final List<OnBoarderPageBehaviour> pageBehaviors = pageLayout.getBehaviors(mOnBoarderCoordinatorViewPager);
            if (!pageBehaviors.isEmpty()) {
                behaviors.addAll(pageBehaviors);
            }
        }
        return behaviors;
    }

    public OnBoarderCoordinatorViewPager getOnBoarderCoordinatorViewPager() {
        return mOnBoarderCoordinatorViewPager;
    }
}
