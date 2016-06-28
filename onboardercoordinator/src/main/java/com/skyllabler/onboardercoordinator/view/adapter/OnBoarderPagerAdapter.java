package com.skyllabler.onboardercoordinator.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyllabler.onboardercoordinator.view.inflater.AOnBoarderPageDescriptor;

public class OnBoarderPagerAdapter extends PagerAdapter {

    private final AOnBoarderPageDescriptor<Integer, Integer> mOnBoaderPageDescriptor;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public OnBoarderPagerAdapter(Context context, AOnBoarderPageDescriptor<Integer, Integer> onBoarderPageDescriptor) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mOnBoaderPageDescriptor = onBoarderPageDescriptor;
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
        // Inflate the layout for the page
        ViewGroup pageView = (ViewGroup) mLayoutInflater.inflate(mOnBoaderPageDescriptor.getDescriptorTypeList().get(position).getKey(), container, false);
        // Set the background color
        pageView.setBackgroundColor(mOnBoaderPageDescriptor.getDescriptorTypeList().get(position).getValue());
        // Add the page to the container
        container.addView(pageView);
        // Return the page
        return pageView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
