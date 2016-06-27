package com.skyllabler.onboardercoordinator.views.controller;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewTreeObserver;

import com.skyllabler.onboardercoordinator.views.widget.OnBoarderCoordinatorLayout;

public class OnBoarderCoordinatorTouchController {

    public static final int MAX_VELOCITY = 300;
    public static final int CURRENT_VELOCITY = 1000;
    public static final long SMOOTH_SCROLL_DURATION = 300;
    public static final String PROPERTY_SCROLL_X = "scrollX";
    private final OnBoarderCoordinatorLayout view;
    private float iniEventX;
    private int currentScrollX;
    private int minScroll = 0;
    private int maxScroll = 0;
    private VelocityTracker velocityTracker;
    private ObjectAnimator smoothScrollAnimator;
    private OnPageScrollListener onPageScrollListener;

    public OnBoarderCoordinatorTouchController(final OnBoarderCoordinatorLayout view) {
        this.view = view;
        configureLayoutListener();
    }

    private void configureLayoutListener() {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                OnBoarderCoordinatorLayout onboarderCoordinatorLayout = OnBoarderCoordinatorTouchController.this.view;
                int pagesSteps = onboarderCoordinatorLayout.getNumOfPages() - 1;
                maxScroll = pagesSteps * onboarderCoordinatorLayout.getWidth();
                ViewTreeObserver viewTreeObserver = onboarderCoordinatorLayout.getViewTreeObserver();
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
        view.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                view.notifyProgressScroll(view.getScrollX() / (float) view.getWidth(),
                        view.getScrollX());
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                currentScrollX = view.getScrollX();
                iniEventX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                setScrollX(scrollLimited(event.getX()));
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(CURRENT_VELOCITY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setScrollX(scrollLimited(event.getX()));
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(CURRENT_VELOCITY);
                evaluateMoveToPage();
                break;
        }
        return true;
    }

    private void evaluateMoveToPage() {
        float xVelocity = velocityTracker.getXVelocity();
        currentScrollX = view.getScrollX();
        moveToPagePosition(xVelocity);
    }

    private int scrollLimited(float scrollX) {
        int newScrollPosition = (int) (currentScrollX + iniEventX - scrollX);
        return Math.max(minScroll, Math.min(newScrollPosition, maxScroll));
    }

    private void setScrollX(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setScrollX(value);
        } else {
            view.scrollTo(value, view.getScrollY());
        }
        if (onPageScrollListener != null) {
            onPageScrollListener.onScrollPage(value);
        }
    }

    private void moveToPagePosition(float xVelocity) {
        float width = view.getWidth();
        int currentPage = (int) Math.floor(currentScrollX / width);
        float percentageOfNewPageVisible = (currentScrollX % width) / width;
        if (xVelocity < -MAX_VELOCITY) {
            scrollToPage(currentPage + 1);
        } else if (xVelocity > MAX_VELOCITY) {
            scrollToPage(currentPage);
        } else if (percentageOfNewPageVisible > 0.5f) {
            scrollToPage(currentPage + 1);
        } else {
            scrollToPage(currentPage);
        }
    }

    public void scrollToPage(int newCurrentPage) {
        scrollToPage(newCurrentPage, true);
    }

    public void scrollToPage(int newCurrentPage, boolean animated) {
        int width = view.getWidth();
        int limitedNumPage = Math.max(0, Math.min(view.getNumOfPages() - 1, newCurrentPage));
        int scrollX = limitedNumPage * width;
        if (animated) {
            smoothScrollX(scrollX);
        } else {
            setScrollX(scrollX);
        }
    }

    private void smoothScrollX(int scrollX) {
        cancelScrollAnimationIfNecessary();
        if (view.getScrollX() != scrollX) {
            smoothScrollAnimator = ObjectAnimator.ofInt(view, PROPERTY_SCROLL_X, view.getScrollX(), scrollX);
            smoothScrollAnimator.setDuration(SMOOTH_SCROLL_DURATION);
            smoothScrollAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onPageScrollListener != null) {
                        int width = view.getWidth();
                        int page = view.getScrollX() / width;
                        int limitedNumPage = Math.max(0, Math.min(view.getNumOfPages() - 1, page));
                        onPageScrollListener.onPageSelected(limitedNumPage);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            smoothScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (onPageScrollListener != null) {
                        onPageScrollListener.onScrollPage(view.getScrollX());
                    }
                }
            });
            smoothScrollAnimator.start();
        }
    }

    private void cancelScrollAnimationIfNecessary() {
        if ((smoothScrollAnimator != null) && (smoothScrollAnimator.isRunning())) {
            smoothScrollAnimator.cancel();
            view.clearAnimation();
            smoothScrollAnimator = null;
        }
    }

    public void setOnPageScrollListener(OnPageScrollListener onPageScrollListener) {
        this.onPageScrollListener = onPageScrollListener;
    }

    public interface OnPageScrollListener {
        void onScrollPage(float progress);

        void onPageSelected(int pageSelected);
    }
}