package com.appacitive.android.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.example.appacitive_android_sdk_tests.BuildConfig;

public class AppacitiveInteraceTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		Appacitive.initializeAppacitive(getContext(), BuildConfig.API_KEY, new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
	}
	
	/*
	 * Purporse: check the default environment to be sandbox
	 */
	public void testDefaultEnvironment() {
		Appacitive appacitive = Appacitive.getInstance();
		if(appacitive != null) {
			assertEquals("sandbox", appacitive.getEnvironment());
		}
	}
	
	/*
	 * Purpose: check the set environment to the live
	 */
	public void testEnvironment() {
		Appacitive appacitive = Appacitive.getInstance();
		if(appacitive != null) {
			appacitive.setEnableLiveEnvironment(true);
			assertEquals("live", appacitive.getEnvironment());
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive.endSession();
	}

}