package com.skyllabler.onboardercoordinator.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.skyllabler.onboardercoordinator.R;
import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderPageBehaviour;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class OnBoarderPageLayout extends RelativeLayout {

    public OnBoarderPageLayout(Context context) {
        super(context);
    }

    public OnBoarderPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnBoarderPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OnBoarderPageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams params) {
        return new LayoutParams(params);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public List<OnBoarderPageBehaviour> getBehaviors(OnBoarderCoordinatorViewPager coordinatorLayout) {
        List<OnBoarderPageBehaviour> result = new ArrayList<>();
        for (int index = 0; index < getChildCount(); index++) {
            final View view = getChildAt(index);
            if (view.getLayoutParams() instanceof LayoutParams) {
                final LayoutParams params = (LayoutParams) view.getLayoutParams();
                final OnBoarderPageBehaviour behavior = params.getBehavior();
                if (behavior != null) {
                    behavior.setCoordinator(coordinatorLayout);
                    behavior.setTarget(view);
                    behavior.setPage(this);
                    result.add(behavior);
                }
            }
        }
        return result;
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        public final static int NO_DESTINY_VIEW = -1;
        private int destinyViewId = NO_DESTINY_VIEW;

        private OnBoarderPageBehaviour behavior;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            extractAttributes(context, attrs);
        }

        @SuppressWarnings("unused")
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }

        public OnBoarderPageBehaviour getBehavior() {
            return behavior;
        }

        public int getDestinyViewId() {
            return destinyViewId;
        }

        private void extractAttributes(Context context, AttributeSet attrs) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OnBoarderPageLayout_LayoutParams);
            if (attributes.hasValue(R.styleable.OnBoarderPageLayout_LayoutParams_view_behavior)) {
                behavior = parseBehavior(context, attributes.getString(R.styleable.OnBoarderPageLayout_LayoutParams_view_behavior));
            }
            if (attributes.hasValue(R.styleable.OnBoarderPageLayout_LayoutParams_destiny)) {
                destinyViewId = attributes.getResourceId(R.styleable.OnBoarderPageLayout_LayoutParams_destiny, NO_DESTINY_VIEW);
            }
            attributes.recycle();
        }

        private OnBoarderPageBehaviour parseBehavior(Context context, String name) {
            OnBoarderPageBehaviour result = null;
            if (!TextUtils.isEmpty(name)) {
                final String fullName;
                if (name.startsWith(".")) {
                    fullName = context.getPackageName() + name;
                } else {
                    fullName = name;
                }
                try {
                    Class<OnBoarderPageBehaviour> behaviorClazz = (Class<OnBoarderPageBehaviour>) Class.forName(fullName);
                    final Constructor<OnBoarderPageBehaviour> mainConstructor = behaviorClazz.getConstructor();
                    mainConstructor.setAccessible(true);
                    result = mainConstructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not inflate Behavior subclass " + fullName, e);
                }
            }
            return result;
        }
    }

}
