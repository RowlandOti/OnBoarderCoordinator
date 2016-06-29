package com.skyllabler.onboardercoordinator.sample.view.inflater;

import android.util.SparseIntArray;

import com.skyllabler.onboardercoordinator.sample.R;
import com.skyllabler.onboardercoordinator.view.inflater.AOnBoarderPageDescriptor;

import java.util.AbstractMap;
import java.util.Map;

public class OnBoarderPageDescriptor extends AOnBoarderPageDescriptor<Integer, Integer> {

    public static final Map.Entry<Integer, Integer> page1Description = new AbstractMap.SimpleEntry<>(R.layout.welcome_page_1, R.color.page1);
    public static final Map.Entry<Integer, Integer> page2Description = new AbstractMap.SimpleEntry<>(R.layout.welcome_page_2, R.color.page2);
    public static final Map.Entry<Integer, Integer> page3Description = new AbstractMap.SimpleEntry<>(R.layout.welcome_page_3, R.color.page3);

    public OnBoarderPageDescriptor() {
        buildDescriptorList(page1Description, page2Description, page3Description);
    }
}
