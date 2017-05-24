package com.devtau.retrofit2;

import android.content.res.Resources;

interface MainActivityView {
	void showProgressBar();
	void dismissProgressBar();
	void updateUI(String msg);
	void showToast(String msg);
	Resources getResources();
}
