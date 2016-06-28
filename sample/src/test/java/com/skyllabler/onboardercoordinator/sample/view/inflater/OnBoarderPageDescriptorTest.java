package com.skyllabler.onboardercoordinator.sample.view.inflater;

import com.skyllabler.onboardercoordinator.sample.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OnBoarderPageDescriptorTest {

    private OnBoarderPageDescriptor onBoarderPageDescriptor;

    @Before
    public void setUp() throws Exception {
        onBoarderPageDescriptor = new OnBoarderPageDescriptor();
    }

    @Test
    public void testGetDescriptorTypeList() throws Exception {
        int size = onBoarderPageDescriptor.getDescriptorTypeList().size();
        Map.Entry<Integer, Integer> pageDescriptionExtract = onBoarderPageDescriptor.getDescriptorTypeList().get(0);
        assertEquals("The descriptor get failed ", (long) R.layout.welcome_page_1, (long) pageDescriptionExtract.getKey());
        assertEquals("The list size comparison failed ", 3, size);
    }
}