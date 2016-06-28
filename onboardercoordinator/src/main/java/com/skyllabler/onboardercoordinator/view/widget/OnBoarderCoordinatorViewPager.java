package com.skyllabler.onboardercoordinator.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.skyllabler.onboardercoordinator.R;
import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderPageBehaviour;

import java.util.ArrayList;
import java.util.List;

public class OnBoarderCoordinatorViewPager extends ViewPager {

    private OnPageScrollListener mOnPageScrollListener;
    private List<OnBoarderPageBehaviour> behaviors = new ArrayList<>();
    private int mPageSelected = 0;

    public OnBoarderCoordinatorViewPager(Context context) {
        super(context);
        initializeView(context, null);
    }

    public OnBoarderCoordinatorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    private void initializeView(Context context, AttributeSet attrs) {
        extractAttributes(context, attrs);

    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OnBoarderCoordinatorLayout);
        attributes.recycle();
    }

    public void setOnPageScrollListener(final OnPageScrollListener onPageScrollListener) {
        this.mOnPageScrollListener = onPageScrollListener;
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int numOfPages = (getAdapter().getCount() - 1);
                int maximumScroll = getMeasuredWidth() * numOfPages;
                mOnPageScrollListener.onScrollPage(OnBoarderCoordinatorViewPager.this, positionOffset, maximumScroll);
            }

            @Override
            public void onPageSelected(int position) {
                mPageSelected = position;
                mOnPageScrollListener.onPageSelected(OnBoarderCoordinatorViewPager.this, mPageSelected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface OnPageScrollListener {
        void onScrollPage(View v, float progress, float maximum);

        void onPageSelected(View v, int pageSelected);
    }
}
