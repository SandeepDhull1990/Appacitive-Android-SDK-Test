package com.appacitive.android.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.test.AndroidTestCase;
import android.util.Log;

import com.appacitive.android.callbacks.AppacitiveAuthenticationCallback;
import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.appacitive.android.model.AppacitiveUser;
import com.example.appacitive_android_sdk_tests.R;

public class AppacitiveUserTest extends AndroidTestCase {

	private boolean isTestSuccessful;

	@Override
	protected void setUp() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		new Handler(getContext().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Appacitive.initializeAppacitive(getContext().getResources()
						.getString(R.string.API_KEY), new AppacitiveCallback() {

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
	}

	/*
	 * Purpose : Testing user authentication for valid user credentials.
	 */
	public void testUserAuthenticationForValidCredentials()
			throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);

		new Handler(getContext().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				AppacitiveUser.authenticate("johndoe", "asd",
						new AppacitiveAuthenticationCallback() {

							@Override
							public void onSuccess() {
								isTestSuccessful = true;
								signal.countDown();
							}

							@Override
							public void onFailure(AppacitiveError error) {
								isTestSuccessful = false;
								signal.countDown();
							}
						});
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	/*
	 * Purpose : Testing user authentication for invalid user credentials.
	 */
	public void testUserAuthenticationForInvalidCredentials()
			throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);

		new Handler(getContext().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				AppacitiveUser.authenticate("invaliduser", "damnit",
						new AppacitiveAuthenticationCallback() {

							@Override
							public void onSuccess() {
								isTestSuccessful = false;
								signal.countDown();
							}

							@Override
							public void onFailure(AppacitiveError error) {
								isTestSuccessful = true;
								signal.countDown();
							}
						});
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	/*
	 * Purpose : Testing user authentication with facebook user credentials.
	 */
	public void testUserAuthenticationForFacebookCredentials()
			throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);

		new Handler(getContext().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				AppacitiveUser
						.authenticateWithFacebook(
								"AAACEdEose0cBAKO5Ejuztbgx6kZCIdaWB6wM9wmOtEFegkSPCrRADeZBXKrT58EjGJcVRq2YNFtkfFz1wZCD9So1FG5ZCyO7hD7xYEyruI2oUmNBYNBg",
								new AppacitiveAuthenticationCallback() {

									@Override
									public void onSuccess() {
										isTestSuccessful = true;
										signal.countDown();
									}

									@Override
									public void onFailure(AppacitiveError error) {
										isTestSuccessful = false;
										signal.countDown();
									}
								});

			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	/*
	 * Purpose : Testing user authentication with valid twitter user credentials.
	 */
	public void testUserAuthenticationForTwitterCredentials() throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);

		new Handler(getContext().getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				AppacitiveUser.authenticateWithTwitter("346420701-3F4fX7hCK5rTlH3CvGafEoXPUW4ShHdJ4zUXkSho",
						"mfbivKtcgRDiwWxkOxMRVh0z7kcK0mbtaC6ht01avw",
						"xtAtWO0pWMcOFiW4b8quA", "bVBzWqUGPYAqPtG2dju4PEZmKjHgIqtqKo6zwGY",
						new AppacitiveAuthenticationCallback() {

							@Override
							public void onSuccess() {
								isTestSuccessful = false;
								signal.countDown();
							}

							@Override
							public void onFailure(AppacitiveError error) {
								isTestSuccessful = true;
								Log.d("TAG", "Error is " + error.toString());
								signal.countDown();
							}
						});
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	/*
	 * Purpose : Testing user authentication with invalid twitter user credentials.
	 */
	public void testUserAuthenticationForInvalidTwitterCredentials() throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);
		
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				
				AppacitiveUser.authenticateWithTwitter(null, null, null, null,
						new AppacitiveAuthenticationCallback() {
					
					@Override
					public void onSuccess() {
						isTestSuccessful = false;
						signal.countDown();
					}
					
					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessful = true;
						Log.d("TAG", "Error is " + error.toString());
						signal.countDown();
					}
				});
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive appacitive = Appacitive.getInstance();
		appacitive.endSession();
	}

}
