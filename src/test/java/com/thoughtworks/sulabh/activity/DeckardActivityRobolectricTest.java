package com.thoughtworks.sulabh.activity;

import android.app.Activity;
import com.example.activity.DeckardActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class DeckardActivityRobolectricTest {

    @Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(DeckardActivity.class).create().get();
        assertTrue(activity != null);
    }
}
