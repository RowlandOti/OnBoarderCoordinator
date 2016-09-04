package com.skyllabler.onboardercoordinator.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderBehaviourExtractor;
import com.skyllabler.onboardercoordinator.view.behaviour.OnBoarderPageBehaviour;
import com.skyllabler.onboardercoordinator.view.inflater.AOnBoarderPageDescriptor;

import java.util.ArrayList;
import java.util.List;

public class OnBoarderPagerAdapter extends PagerAdapter {

    private final AOnBoarderPageDescriptor<Integer, Integer> mOnBoaderPageDescriptor;
    private Context mContext;

    private OnBoarderBehaviourExtractor mBehaviourExtractor;
    private LayoutInflater mLayoutInflater;
    private List<OnBoarderPageBehaviour> mBehaviors;

    public OnBoarderPagerAdapter(Context ctx, AOnBoarderPageDescriptor<Integer, Integer> descriptor) {
        this.mContext = ctx;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mOnBoaderPageDescriptor = descriptor;
        this.mBehaviors = new ArrayList<>();
    }

    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return mOnBoaderPageDescriptor.getDescriptorTypeList().size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup pageView = (ViewGroup) mLayoutInflater.inflate(mOnBoaderPageDescriptor.getDescriptorTypeList().get(position).getKey(), container, false);
        pageView.setBackgroundColor(mOnBoaderPageDescriptor.getDescriptorTypeList().get(position).getValue());
        final List<OnBoarderPageBehaviour> pageBehaviors = mBehaviourExtractor.extractPageBehaviors(pageView);
        if (!pageBehaviors.isEmpty()) {
            this.mBehaviors.addAll(pageBehaviors);
        }
        container.addView(pageView);
        Log.d(OnBoarderPagerAdapter.class.getSimpleName(), "Child Count" + container.getChildCount());
        return pageView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void notifyProgressScroll(float progress, float scroll) {
        for (OnBoarderPageBehaviour onBoarderPageBehavior : mBehaviors) {
            onBoarderPageBehavior.onPlaytimeChange(mBehaviourExtractor.getOnBoarderCoordinatorViewPager(), progress, scroll);
        }
    }

    public void setBehaviourExtractor(OnBoarderBehaviourExtractor mBehaviourExtractor) {
        this.mBehaviourExtractor = mBehaviourExtractor;
    }
}
