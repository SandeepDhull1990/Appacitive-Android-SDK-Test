package com.appacitive.android.tests;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.callbacks.AppacitiveDownloadCallback;
import com.appacitive.android.callbacks.AppacitiveUploadCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.appacitive.android.model.AppacitiveFile;
import com.example.appacitive_android_sdk_tests.BuildConfig;
import com.example.appacitive_android_sdk_tests.R;

public class AppacitiveFileTest extends AndroidTestCase {

	private boolean isTestSuccessfull;

	@Override
	protected void setUp() throws InterruptedException {
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
		signal.await(15, TimeUnit.SECONDS);
	}
	
	public void testAppacitiveFileUpload() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessfull = false;
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
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	public void testAppacitiveFileDownload() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		
		AppacitiveFile.download("hotel.png", new AppacitiveDownloadCallback() {
			
			@Override
			public void onSuccess(InputStream inputStream) {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive.endSession();
	}
	
}
