package com.skyllabler.onboardercoordinator.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.skyllabler.onboardercoordinator.R;
import com.skyllabler.onboardercoordinator.view.adapter.OnBoarderPagerAdapter;
import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderPageBehaviour;

import java.util.ArrayList;
import java.util.List;

public class OnBoarderCoordinatorViewPager extends ViewPager {

    private OnPageScrollListener mOnPageScrollListener;
    private List<OnBoarderPageBehaviour> behaviors;
    private int maxScroll = 0;
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
        behaviors = new ArrayList<>();
        extractAttributes(context, attrs);
        configureLayoutListener();
    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OnBoarderCoordinatorLayout);
        attributes.recycle();
    }

    private void configureLayoutListener() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int pagesSteps = getAdapter().getCount() - 1;
                maxScroll = pagesSteps * OnBoarderCoordinatorViewPager.this.getWidth();
                ViewTreeObserver viewTreeObserver = OnBoarderCoordinatorViewPager.this.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
                configureScrollListener();
            }
        });
    }

    private void configureScrollListener() {
        this.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                OnBoarderCoordinatorViewPager vp = OnBoarderCoordinatorViewPager.this;
                ((OnBoarderPagerAdapter) vp.getAdapter()).notifyProgressScroll(vp.getScrollX() / (float) vp.getWidth(), vp.getScrollX());
            }
        });
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
