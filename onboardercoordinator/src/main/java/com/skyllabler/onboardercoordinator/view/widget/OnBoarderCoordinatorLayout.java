package com.skyllabler.onboardercoordinator.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.skyllabler.onboardercoordinator.R;
import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderPageBehaviour;
import com.skyllabler.onboardercoordinator.view.controller.OnBoarderCoordinatorTouchController;
import com.skyllabler.onboardercoordinator.view.inflater.OnBoarderCoordinatorPageInflater;

import java.util.ArrayList;
import java.util.List;

public class OnBoarderCoordinatorLayout extends HorizontalScrollView {

    public static final boolean ANIMATED = true;
    public static final boolean INANIMATED = false;
    public static final int WITHOUT_MARGIN = 0;
    public static final int RADIUS = 12;
    public static final int RADIUS_MARGIN = 30;
    public static final int DEF_INDICATOR_UNSELECTED_COLOR = Color.WHITE;
    public static final int DEF_INDICATOR_SELECTED_COLOR = Color.BLACK;
    private OnBoarderCoordinatorTouchController touchController;
    private OnBoarderCoordinatorPageInflater pageInflater;
    private FrameLayout mainContentView;
    private List<OnBoarderPageBehaviour> behaviors = new ArrayList<>();
    private OnPageScrollListener onPageScrollListener;
    private int pageSelected = 0;
    private int indicatorColorUnselected = DEF_INDICATOR_UNSELECTED_COLOR;
    private int indicatorColorSelected = DEF_INDICATOR_SELECTED_COLOR;
    private Paint indicatorPaintUnselected;
    private Paint indicatorPaintSelected;
    private boolean showIndicators = true;
    private boolean scrollingEnabled = true;

    public OnBoarderCoordinatorLayout(Context context) {
        super(context);
        initializeView(context, null);
    }

    public OnBoarderCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public OnBoarderCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OnBoarderCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView(context, attrs);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEventCaptured = false;
        if (scrollingEnabled) {
            touchEventCaptured = touchController.onTouchEvent(event);
            if (!touchEventCaptured) {
                touchEventCaptured = super.onTouchEvent(event);
            }
        }
        return touchEventCaptured;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int index = 0; index < getNumOfPages(); index++) {
            ViewGroup childAt = (ViewGroup) mainContentView.getChildAt(index);
            configurePageLayout(childAt, index);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (showIndicators) {
            drawIndicator(canvas);
        }
    }

    private void drawIndicator(Canvas canvas) {
        int centerX = (getWidth() - RADIUS) / 2 + RADIUS / 2;
        int indicatorWidth = RADIUS * 2;
        int indicatorAndMargin = indicatorWidth + RADIUS_MARGIN;
        int leftIndicators = centerX - ((getNumOfPages() - 1) * indicatorAndMargin) / 2;
        int positionY = getHeight() - RADIUS - RADIUS_MARGIN;
        for (int i = 0; i < getNumOfPages(); i++) {
            int x = leftIndicators + indicatorAndMargin * i + getScrollX();
            canvas.drawCircle(x, positionY, RADIUS, indicatorPaintUnselected);
        }
        float width = (float) getWidth();
        float scrollProgress = getScrollX() / width;
        float selectedXPosition = leftIndicators + getScrollX() + scrollProgress * indicatorAndMargin;
        canvas.drawCircle(selectedXPosition, positionY, RADIUS, indicatorPaintSelected);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        this.setHorizontalScrollBarEnabled(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        touchController = new OnBoarderCoordinatorTouchController(this);
        pageInflater = new OnBoarderCoordinatorPageInflater(this);
        extractAttributes(context, attrs);
        buildMainContentView();
        attachMainContentView();
        configureIndicatorColors();
    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OnBoarderCoordinatorLayout);
        indicatorColorUnselected = attributes.getColor(R.styleable.OnBoarderCoordinatorLayout_indicatorUnselected, DEF_INDICATOR_UNSELECTED_COLOR);
        indicatorColorSelected = attributes.getColor(R.styleable.OnBoarderCoordinatorLayout_indicatorSelected, DEF_INDICATOR_SELECTED_COLOR);
        showIndicators = attributes.getBoolean(R.styleable.OnBoarderCoordinatorLayout_showIndicators, showIndicators);
        scrollingEnabled = attributes.getBoolean(R.styleable.OnBoarderCoordinatorLayout_scrollingEnabled, scrollingEnabled);
        attributes.recycle();
    }

    private void buildMainContentView() {
        mainContentView = new FrameLayout(this.getContext());
        mainContentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mainContentView.setClipToPadding(false);
        mainContentView.setClipChildren(false);
    }

    private void attachMainContentView() {
        removeAllViews();
        setClipChildren(false);
        addView(mainContentView);
    }

    private void configureIndicatorColors() {
        indicatorPaintUnselected = new Paint();
        indicatorPaintUnselected.setColor(indicatorColorUnselected);
        indicatorPaintSelected = new Paint();
        indicatorPaintSelected.setColor(indicatorColorSelected);
    }

    private void configurePageLayout(ViewGroup pageView, int position) {
        int coordinatorWidth = getMeasuredWidth();
        int reversePosition = getNumOfPages() - 1 - position;
        int pageMarginLeft = (coordinatorWidth * reversePosition);
        int originalHeight = pageView.getLayoutParams().height;
        LayoutParams layoutParams = new LayoutParams(coordinatorWidth, originalHeight);
        layoutParams.setMargins(pageMarginLeft, WITHOUT_MARGIN, WITHOUT_MARGIN, WITHOUT_MARGIN);
        pageView.setLayoutParams(layoutParams);
    }

    public void addPage(@LayoutRes int... layoutResourceIds) {
        for (int i = layoutResourceIds.length - 1; i >= 0; i--) {
            int layoutResourceId = layoutResourceIds[i];
            final View pageView = pageInflater.inflate(layoutResourceId);
            final List<OnBoarderPageBehaviour> pageBehaviors = extractPageBehaviors(pageView);
            if (!pageBehaviors.isEmpty()) {
                this.behaviors.addAll(pageBehaviors);
            }
            mainContentView.addView(pageView);
        }
        if (onPageScrollListener != null) {
            onPageScrollListener.onPageSelected(this, 0);
        }
        requestLayout();
    }

    private List<OnBoarderPageBehaviour> extractPageBehaviors(View view) {
        List<OnBoarderPageBehaviour> behaviors = new ArrayList<>();
        if (view instanceof OnBoarderPageLayout) {
            final OnBoarderPageLayout pageLayout = (OnBoarderPageLayout) view;
            final List<OnBoarderPageBehaviour> pageBehaviors = pageLayout.getBehaviors(this);
            if (!pageBehaviors.isEmpty()) {
                behaviors.addAll(pageBehaviors);
            }
        }
        return behaviors;
    }

    public void notifyProgressScroll(float progress, float scroll) {
        for (OnBoarderPageBehaviour onBoarderPageBehavior : behaviors) {
            onBoarderPageBehavior.onPlaytimeChange(this, progress, scroll);
        }
    }


    public int getPageSelected() {
        return pageSelected;
    }

    public int getNumOfPages() {
        int result = 0;
        if (mainContentView != null) {
            result = mainContentView.getChildCount();
        }
        return result;
    }

    public void setCurrentPage(int newCurrentPage, boolean animated) {
        pageSelected = Math.max(0, Math.min(getNumOfPages() - 1, newCurrentPage));
        touchController.scrollToPage(pageSelected, animated);
    }

    public void setOnPageScrollListener(OnBoarderCoordinatorLayout.OnPageScrollListener onPageScrollListener) {
        this.onPageScrollListener = onPageScrollListener;
        touchController.setOnPageScrollListener(new OnBoarderCoordinatorTouchController.OnPageScrollListener() {
            @Override
            public void onScrollPage(float progress) {
                int numOfPages = (getNumOfPages() - 1);
                int maximumScroll = getMeasuredWidth() * numOfPages;
                OnBoarderCoordinatorLayout.this.onPageScrollListener.onScrollPage(OnBoarderCoordinatorLayout.this, progress, maximumScroll);
            }

            @Override
            public void onPageSelected(int pageSelected) {
                OnBoarderCoordinatorLayout.this.pageSelected = pageSelected;
                OnBoarderCoordinatorLayout.this.onPageScrollListener.onPageSelected(OnBoarderCoordinatorLayout.this, pageSelected);
            }
        });
    }

    public void showIndicators(boolean show) {
        this.showIndicators = show;
    }

    public void setScrollingEnabled(boolean enabled) {
        this.scrollingEnabled = enabled;
    }

    public void setIndicatorColorSelected(int indicatorColorSelected) {
        this.indicatorColorSelected = indicatorColorSelected;
        indicatorPaintSelected.setColor(indicatorColorSelected);
    }

    public void setIndicatorColorUnselected(int indicatorColorUnselected) {
        this.indicatorColorUnselected = indicatorColorUnselected;
        indicatorPaintUnselected.setColor(indicatorColorUnselected);
    }

    public interface OnPageScrollListener {
        void onScrollPage(View v, float progress, float maximum);

        void onPageSelected(View v, int pageSelected);
    }


}
