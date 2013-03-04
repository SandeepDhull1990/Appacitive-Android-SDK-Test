package com.appacitive.android.tests;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.test.AndroidTestCase;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.callbacks.AppacitiveDownloadCallback;
import com.appacitive.android.callbacks.AppacitiveUploadCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.appacitive.android.model.AppacitiveFile;
import com.example.appacitive_android_sdk_tests.R;

public class AppacitiveFileTest extends AndroidTestCase {

	private boolean isTestSuccessfull;

	@Override
	protected void setUp() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				Appacitive.initializeAppacitive(getContext().getResources().getString(R.string.API_KEY), new AppacitiveCallback() {
					
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
	
	public void testAppacitiveFileUpload() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessfull = false;
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.hotel);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				AppacitiveFile.uploadData("hotel.png", "image/png", 10, byteArray, new AppacitiveUploadCallback() {
					
					@Override
					public void onSuccess(String url) {
						isTestSuccessfull = true;
						signal.countDown();
					}
					
					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessfull = false;
						signal.countDown();
					}
				});
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	public void testAppacitiveFileDownload() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		new Handler(getContext().getMainLooper()).post(new Runnable() {
			
			@Override
			public void run() {
				AppacitiveFile.download("hotel.png", new AppacitiveDownloadCallback() {
					
					@Override
					public void onSuccess(byte[] data) {
						isTestSuccessfull = true;
						signal.countDown();
					}
					
					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessfull = false;
						signal.countDown();
					}
					
				});
			}
		});
		
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive appacitive = Appacitive.getInstance();
		appacitive.endSession();
	}
	
}
