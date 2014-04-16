package com.thoughtworks.sulabh.activity;

import android.content.Intent;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class DetailsActivityTest {
	private DetailsActivity detailsActivity;
	private Intent intent;

	@Before
	public void setUp() throws Exception {
		detailsActivity = spy(new DetailsActivity());
		doNothing().when(detailsActivity).onCreate(null);
		intent = mock(Intent.class);
		Bundle bundle = new Bundle();
		when(intent.getExtras()).thenReturn(bundle);
		when(detailsActivity.getIntent()).thenReturn(intent);
	}

	@Test
	public void mapsTrueToYes() {
		String actual = detailsActivity.mapValues("true");
		String expected = "Yes";
		assertEquals(expected, actual);
	}

	@Test
	public void mapsFalseToNo() {
		String actual = detailsActivity.mapValues("false");
		String expected = "No";
		assertEquals(expected, actual);
	}
}