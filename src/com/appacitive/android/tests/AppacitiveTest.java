package com.appacitive.android.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.test.AndroidTestCase;
import android.test.UiThreadTest;
import android.util.Log;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;

public class AppacitiveTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
	}
	
	/*
	 * Purpose : Test the appacitive session initialization for valid session id
	 */
	@UiThreadTest
	public void testAppacitiveInitializationForValidAPIKey() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
			new Handler(getContext().getMainLooper()).post(new Runnable() {
				
				@Override
				public void run() {
					Appacitive.initializeAppacitive("ukaAo61yoZoeTJsGacH9TDRHnhf/J9/kH2TStR5sD3k=", new AppacitiveCallback() {
						
						@Override
						public void onSuccess() {
							Log.d("TAG", "Appacitive session init id is " + Appacitive.getInstance().getSessionId());
							signal.countDown();
						}
						
						@Override
						public void onFailure(AppacitiveError error) {
							Log.d("TAG", "Appacitive session init error is " + error.toString());
							signal.countDown();
						}
					});
				}
			});
		signal.await(45, TimeUnit.SECONDS);
		assertNotNull("Appacitive session is not initialized", Appacitive.getInstance().getSessionId());
	}
	
	/*
	 * Purpose : Test the appacitive session initialization for invalid session id
	 */
	public void testAppacitiveInitializationForInvalidAPIKey() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				Appacitive.initializeAppacitive("", new AppacitiveCallback() {
					
					@Override
					public void onSuccess() {
						signal.countDown();
					}
					
					@Override
					public void onFailure(AppacitiveError error) {
						signal.countDown();
					}
				});
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
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				Appacitive.initializeAppacitive("", new AppacitiveCallback() {
					
					@Override
					public void onSuccess() {
						signal.countDown();
					}
					
					@Override
					public void onFailure(AppacitiveError error) {
						signal.countDown();
					}
				});
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertNull("Appacitive session is initialized",Appacitive.getInstance());
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
}