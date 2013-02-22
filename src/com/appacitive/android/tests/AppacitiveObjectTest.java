package com.appacitive.android.tests;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;
import android.util.Log;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.callbacks.AppacitiveFetchCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveError;
import com.appacitive.android.model.AppacitiveObject;
import com.appacitive.android.model.AppacitiveQuery;
import com.example.appacitive_android_sdk_tests.BuildConfig;

public class AppacitiveObjectTest extends AndroidTestCase {
	
	private boolean isTestSuccessfull;

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
		signal.await(15, TimeUnit.SECONDS);
	}
	
	/*
	 * Purpose : Testing save operation of AppacitiveObject for valid schema name
	 */
	public void testSaveForValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject object = new AppacitiveObject("tasks");
		object.addProperty("task_name", "Buy new shoes");
		object.addProperty("completed_at", "2013-02-02");
		object.addAttribute("test", "test");
		object.addAttribute("test1", "test2");
		object.addTag("Demo");
		object.saveObject(new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing save operation of AppacitiveObject for invalid schema name
	 */
	public void testSaveForInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject object = new AppacitiveObject("");
		object.addProperty("task_name", "Buy new shoes");
		object.addProperty("completed_at", "2013-02-02");
		object.saveObject(new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing save API of AppacitiveObject for null schema name
	 */
	public void testSaveForNullSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject object = new AppacitiveObject(null);
		object.addProperty("task_name", "Buy new shoes");
		object.addProperty("completed_at", "2013-02-02");
		object.saveObject(new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing Search API of AppacitiveObject for valid schema name
	 */
	public void testSearchForValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		
		AppacitiveObject.searchAllObjects("tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing Search API of AppacitiveObject for invalid schema name
	 */
	public void testSearchForInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		
		AppacitiveObject.searchAllObjects("", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing search API with valid query for valid schema name 
	 */
	public void testSearchObjectForValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Demo");
		String query = AppacitiveQuery.queryStringForSearchWithOneOrMoreTags(tags);
		AppacitiveObject.searchObjects("tasks", "query=" + query, new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "The response is " + error.toString());
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Testing search API with invalid query for valid schema name 
	 */
	public void testSearchObjectForInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject.searchObjects("", "query=abc++1", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	
	/*
	 * Purpose : Test fetch API for valid AppacitiveObject
	 */
	public void testFetchObjectForValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject appacitiveObject = new AppacitiveObject("tasks");
		appacitiveObject.setObjectId(18425911758160389l);
		appacitiveObject.fetchObject(new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Test fetch API for invalid AppacitiveObject
	 */
	public void testFetchObjectForInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject appacitiveObject = new AppacitiveObject("");
		appacitiveObject.fetchObject(new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Test multi-fetch API for valid schema AppacitiveObject
	 */
	public void testFetchMultipleObjectWithIdsForValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("18433450635822092");
		ids.add("18433296495149332");
		ids.add("18433052942402571");
		AppacitiveObject.fetchObjectsWithIds(ids, "tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = false;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Test multifetch API for invalid schema AppacitiveObject
	 */
	public void testFetchMultipleObjectWithIdsForInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("18425911758160389");
		ids.add("18423952835740164");
		ids.add("18423016526578433");
		AppacitiveObject.fetchObjectsWithIds(ids, "", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessfull = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessfull = true;
				signal.countDown();
			}
		});
		
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Delete object with valid schema name
	 */
	public void testDeleteObjectWithValidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject object = new AppacitiveObject("tasks");
		object.setObjectId(18429902315455236l);
		object.deleteObject();
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	/*
	 * Purpose : Delete object with invalid schema name
	 */
	public void testDeleteObjectWithInvalidSchemaName() throws InterruptedException {
		isTestSuccessfull = false;
		final CountDownLatch signal = new CountDownLatch(1);
		AppacitiveObject object = new AppacitiveObject("abc++2");
		object.setObjectId(18429902315455236l);
		object.deleteObject();
		signal.await(15, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessfull);
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive.endSession();
	}
}