package com.skyllabler.onboardercoordinator.view.inflater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AOnBoarderPageDescriptor<L, C> {

    // ToDo: replace with SparseIntArray for performance improvement
    protected List<Map.Entry<L, C>> mDescriptionTypeList;

    public AOnBoarderPageDescriptor() {
        this.mDescriptionTypeList = new ArrayList<Map.Entry<L, C>>();
    }

    public void buildDescriptorList(Map.Entry<L, C>... pageDescriptions) {
        for (Map.Entry<L, C> pageDescription : pageDescriptions) {
            mDescriptionTypeList.add(pageDescription);
        }
    }

    public List<Map.Entry<L, C>> getDescriptorTypeList() {
        return mDescriptionTypeList;
    }
}
