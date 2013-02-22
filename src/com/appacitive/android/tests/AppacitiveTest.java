package com.appacitive.android.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.example.appacitive_android_sdk_tests.BuildConfig;

public class AppacitiveTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
	}
	
	/*
	 * Purpose : Test the appacitive session initialization for valid session id
	 */
	public void testAppacitiveInitializationForValidAPIKey() throws InterruptedException {
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
		assertNotNull("Appacitive session is not initialized",Appacitive.getInstance().getSessionId());
	}
	
	/*
	 * Purpose : Test the appacitive session initialization for invalid session id
	 */
	public void testAppacitiveInitializationForInvalidAPIKey() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		Appacitive.initializeAppacitive(getContext(), "", new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertNull("Appacitive session is initialized",Appacitive.getInstance());
	}

	/*
	 * Purpose : Test the appacitive session initialization for null session id
	 */
	public void testAppacitiveInitializationForNullAPIKey() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		Appacitive.initializeAppacitive(getContext(), "", new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertNull("Appacitive session is initialized",Appacitive.getInstance());
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
}