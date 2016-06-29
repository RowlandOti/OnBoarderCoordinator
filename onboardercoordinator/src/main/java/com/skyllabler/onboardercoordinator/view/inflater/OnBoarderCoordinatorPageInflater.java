package com.skyllabler.onboardercoordinator.view.inflater;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyllabler.onboardercoordinator.view.widget.OnBoarderCoordinatorViewPager;

public class OnBoarderCoordinatorPageInflater {
    private final OnBoarderCoordinatorViewPager view;
    private final LayoutInflater inflater;

    public OnBoarderCoordinatorPageInflater(@NonNull OnBoarderCoordinatorViewPager view) {
        this.view = view;
        this.inflater = LayoutInflater.from(view.getContext());
    }

    public View inflate(@LayoutRes int layoutResourceId) {
        final ViewGroup pageView = (ViewGroup) inflater.inflate(layoutResourceId, view, false);
        return pageView;
    }
}
