package com.devtau.retrofit2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivityView{

    private EditText usernameEditText;
    private TextView userInfoTextView;
    private Dialog progressBar;
    private RESTClient restClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restClient = new RESTClient(this);
        initUI();
    }

    @Override
    public void showProgressBar() {
        progressBar.show();
    }

    @Override
    public void dismissProgressBar() {
        progressBar.dismiss();
    }

    @Override
    public void updateUI(String msg) {
        userInfoTextView.setText(msg);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void initUI() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        userInfoTextView = (TextView) findViewById(R.id.userInfoTextView);
        progressBar = createProgressBar(this);
        findViewById(R.id.requestDataButton).setOnClickListener(view -> restClient.sendRequest(usernameEditText.getText().toString()));
    }

    private Dialog createProgressBar(Context context) {
        //avoid passing getApplicationContext() as a parameter. pass "this" from activity instead
        Dialog progressDialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_bar, null);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        progressDialog.setContentView(view);
        return progressDialog;
    }
}
