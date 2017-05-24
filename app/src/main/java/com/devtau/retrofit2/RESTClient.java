package com.devtau.retrofit2;

import android.util.Log;
import java.io.IOException;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * library authors page: http://square.github.io/retrofit/
 */
public class RESTClient {

	private static final String LOG_TAG = "RESTClient";
	private	MainActivityView view;


	public RESTClient(MainActivityView view) {
		this.view = view;
	}


	public boolean sendRequest(String user) {
		Log.d(LOG_TAG, "entered retrofit sendRequest method");
		if (user == null || "".equals(user)){
			Log.d(LOG_TAG, "error. user cannot be null or empty");
			return false;
		}
		view.showProgressBar();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BackendAPI.API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient())
				.build();
		BackendAPI client = retrofit.create(BackendAPI.class);

		Callback<GitModelPOJO> callback = new Callback<GitModelPOJO>() {
			@Override
			public void onResponse (Call<GitModelPOJO> call, Response<GitModelPOJO> response){
				view.dismissProgressBar();
				if (response.isSuccessful()) {
					Log.d(LOG_TAG, "retrofit response isSuccessful");
					GitModelPOJO user = response.body();
					handleSuccess(user);
				} else {
					//ответ пришел, но говорит об ошибке
					int errorCode = response.code();
					Log.d(LOG_TAG, "retrofit response is not successful. errorCode: " + String.valueOf(errorCode));
					Log.d(LOG_TAG, "check API_PRECISE_URL and executeRequest parameters if any");
					ResponseBody errorBody = response.errorBody();
					handleError(errorBody);
				}
			}

			@Override
			public void onFailure (Call <GitModelPOJO> call, Throwable t){
				Log.e(LOG_TAG, "retrofit failure: " + t.getLocalizedMessage());
				Log.e(LOG_TAG, "check API_BASE_URL and internet connection");
				view.dismissProgressBar();
				handleFailure(t.getLocalizedMessage());
			}
		};
		Call<GitModelPOJO> call = client.executeRequest(user);
		call.enqueue(callback);
		return true;
	}

	private void handleSuccess(GitModelPOJO user) {
		Locale locale = view.getResources().getConfiguration().locale;
		String formatter = view.getResources().getString(R.string.account_data_formatter);
		String accountDataString = String.format(locale, formatter,
				user.getName(), user.getBlog(), user.getCompany(), user.getAvatarUrl());
		view.updateUI(accountDataString);
	}

	private void handleError(ResponseBody errorBody) {
		String errorMessage = "error";
		try {
			errorMessage = errorBody.string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		view.updateUI(errorMessage);
	}

	private void handleFailure(String failureMessage) {
		view.showToast(failureMessage);
	}
}
