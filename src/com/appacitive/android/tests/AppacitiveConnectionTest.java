package com.appacitive.android.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.AndroidTestCase;
import android.util.Log;

import com.appacitive.android.callbacks.AppacitiveCallback;
import com.appacitive.android.callbacks.AppacitiveFetchCallback;
import com.appacitive.android.model.Appacitive;
import com.appacitive.android.model.AppacitiveConnection;
import com.appacitive.android.model.AppacitiveError;
import com.appacitive.android.model.AppacitiveObject;
import com.example.appacitive_android_sdk_tests.BuildConfig;

public class AppacitiveConnectionTest extends AndroidTestCase {

	private boolean isTestSuccessful;

	@Override
	protected void setUp() throws Exception {
		final CountDownLatch signal = new CountDownLatch(1);
		Appacitive.initializeAppacitive(getContext(), BuildConfig.API_KEY,
				new AppacitiveCallback() {

					@Override
					public void onSuccess() {
						signal.countDown();
					}

					@Override
					public void onFailure(AppacitiveError error) {
						Log.d("TAG", "Error  " + error.toString());
						signal.countDown();
					}
				});
		signal.await(15, TimeUnit.SECONDS);
	}

	public void testConnectionCreationWithValidObjectIds()
			throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);
		final AppacitiveObject object1 = new AppacitiveObject("lists");
		object1.addProperty("list_name", "demoList");
		object1.saveObject(new AppacitiveCallback() {

			@Override
			public void onSuccess() {
				final AppacitiveObject object2 = new AppacitiveObject("tasks");
				object2.addProperty("task_name", "test task");
				object2.saveObject(new AppacitiveCallback() {

					@Override
					public void onSuccess() {
						AppacitiveConnection connection = new AppacitiveConnection(
								"list_tasks");

						connection.setLabelA(object1.getSchemaType());
						connection.setArticleAId(object1.getObjectId());

						connection.setLabelB(object2.getSchemaType());
						connection.setArticleBId(object2.getObjectId());

						connection.createConnection(new AppacitiveCallback() {

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

					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessful = false;
						signal.countDown();
					}
				});
			}

			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessful = false;
				signal.countDown();
			}
		});

		signal.await(45, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	public void testConnectionCreationWithInvalidObjectIds()
			throws InterruptedException {
		isTestSuccessful = false;
		final CountDownLatch signal = new CountDownLatch(1);
		final AppacitiveObject object1 = new AppacitiveObject("lists");
		object1.addProperty("list_name", "demoList");
		object1.saveObject(new AppacitiveCallback() {

			@Override
			public void onSuccess() {
				final AppacitiveObject object2 = new AppacitiveObject("tasks");
				object2.addProperty("task_name", "test task");
				object2.saveObject(new AppacitiveCallback() {

					@Override
					public void onSuccess() {
						AppacitiveConnection connection = new AppacitiveConnection(
								"list_tasks");

						connection.setLabelA(object1.getSchemaType());
						connection.setArticleAId(-32404143243433l);

						connection.setLabelB(object2.getSchemaType());
						connection.setArticleBId(-23412493492349l);

						connection.createConnection(new AppacitiveCallback() {

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

					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessful = false;
						signal.countDown();
					}
				});
			}

			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessful = false;
				signal.countDown();
			}
		});

		signal.await(45, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	public void testDeleteConnectionWithValidConnectionId()
			throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection.searchAllConnections("list_tasks",
				new AppacitiveFetchCallback() {

					@Override
					public void onSuccess(Map<String, Object> response) {
						List<Map> connections = (List<Map>) response.get("connections");
						Map<String, Object> firstConnection = connections.get(0);
						AppacitiveConnection connection = new AppacitiveConnection(
								"list_tasks");
						connection.setConnectionId(Long
								.parseLong((String) firstConnection.get("__id")));
						connection.deleteConnection(new AppacitiveCallback() {

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

					@Override
					public void onFailure(AppacitiveError error) {
						isTestSuccessful = false;
						signal.countDown();
					}
				});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	public void testDeleteConnectionWithInvalidConnectionId()
			throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection connection = new AppacitiveConnection("list_tasks");
		connection.setConnectionId(-3124134244l);
		connection.deleteConnection(new AppacitiveCallback() {

			@Override
			public void onSuccess() {
				isTestSuccessful = false;
				signal.countDown();
			}

			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "In onFailure " + error.toString());
				isTestSuccessful = true;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testDeleteConnectionsWithValidConnectionIds() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		ArrayList<String> connectionIds = new ArrayList<String>();
		connectionIds.add("18589101291865114");
		connectionIds.add("18588975188018197");
		connectionIds.add("18588468558037523");
		connectionIds.add("18587311542501901");
		AppacitiveConnection.deleteConnections(connectionIds, "list_tasks", new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessful = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "In onFailure " + error.toString());
				isTestSuccessful = false;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testDeleteConnectionsWithInvalidConnectionIds() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		ArrayList<String> connectionIds = new ArrayList<String>();
		connectionIds.add("-21312341241242");
		connectionIds.add("-13423423134213");
		AppacitiveConnection.deleteConnections(connectionIds, "list_tasks", new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessful = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "In onFailure " + error.toString());
				isTestSuccessful = true;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testFetchConnectionWithValidConnectionId() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection connection = new AppacitiveConnection("list_tasks");
		connection.setConnectionId(18589522840388102l);
		connection.fetchConnection(new AppacitiveCallback() {
			
			@Override
			public void onSuccess() {
				isTestSuccessful = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "Error : " + error.toString());
				isTestSuccessful = false;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testFetchConnectionWithInvalidConnectionId() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection connection = new AppacitiveConnection("list_tasks");
		connection.setConnectionId(-213423421412l);
		connection.fetchConnection(new AppacitiveCallback() {
			
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
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testFetchConnectionsWithValidConnectionIds() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		ArrayList<String> connectionIds = new ArrayList<String>();
		connectionIds.add("18589522840388102");
		connectionIds.add("18590230608216327");
		AppacitiveConnection.fetchConnections(connectionIds, "list_tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessful = true;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessful = false;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	public void testFetchConnectionsWithInvalidConnectionIds() throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		ArrayList<String> connectionIds = new ArrayList<String>();
		connectionIds.add("-123124940913123");
		connectionIds.add("-319240324234392");
		AppacitiveConnection.fetchConnections(connectionIds, "list_tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessful = false;
				signal.countDown();
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessful = true;
				signal.countDown();
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}

	public void testSearchAllConnectionWithValidRelationName () throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection.searchAllConnections("list_tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				isTestSuccessful = true;
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				isTestSuccessful = false;
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	public void testSearchAllConnectionWithInvalidRelationName () throws InterruptedException {
		final CountDownLatch signal = new CountDownLatch(1);
		isTestSuccessful = false;
		AppacitiveConnection.searchAllConnections("list_tasks", new AppacitiveFetchCallback() {
			
			@Override
			public void onSuccess(Map<String, Object> response) {
				Log.d("TAG", "In onSuccess");
				isTestSuccessful = false;
			}
			
			@Override
			public void onFailure(AppacitiveError error) {
				Log.d("TAG", "In onError " + error.toString());
				isTestSuccessful = true;
			}
		});
		signal.await(30, TimeUnit.SECONDS);
		assertEquals(true, isTestSuccessful);
	}
	
	@Override
	protected void tearDown() throws Exception {
		Appacitive.endSession();
	}
}
