package com.thoughtworks.sulabh.espresso;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import com.thoughtworks.sulabh.activity.LaunchActivity;

@LargeTest
public class LaunchActivityEspressoTest extends ActivityInstrumentationTestCase2<LaunchActivity> {

    @SuppressWarnings("deprecation")
     public LaunchActivityEspressoTest() {
       // This constructor was deprecated - but we want to support lower API levels.
       super("com.thoughtworks.sulabh.activity", LaunchActivity.class);
     }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testCheckText() {
        assertEquals(1, 1);
    }
}
