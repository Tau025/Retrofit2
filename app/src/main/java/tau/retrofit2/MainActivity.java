package tau.retrofit2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    //library authors page: http://square.github.io/retrofit/
    private static final String LOG_TAG = "MainActivity";
    //Base URL: always ends with /
    private static final String API_BASE_URL = "https://api.github.com/";
    //@Url: DO NOT start with /
    private static final String API_PRECISE_URL = "users/{user}";
    private EditText mUserEditText;
    private TextView mTextView;
    private Button button;
    private Dialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateWidgets();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(mUserEditText.getText().toString());
            }
        });
    }

    private void initiateWidgets() {
        mUserEditText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        mTextView = (TextView) findViewById(R.id.textView);
        progressBar = createProgressBar(this);
    }

    private boolean sendRequest(String user) {
        Log.d(LOG_TAG, "entered retrofit sendRequest method");
        if (user == null || "".equals(user)){
            Log.d(LOG_TAG, "error. user cannot be null or empty");
            return false;
        }
        progressBar.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        GitAPI gitClient = retrofit.create(GitAPI.class);

        Callback<GitModelPOJO> callback = new Callback<GitModelPOJO>() {
            @Override
            public void onResponse (Call <GitModelPOJO> call, Response <GitModelPOJO> response){
                if (response.isSuccessful()) {
                    //получен ответ от сервера об успехе. обработаем полученный результат
                    Log.d(LOG_TAG, "retrofit response isSuccessful");
                    GitModelPOJO user = response.body();
                    progressBar.dismiss();
                    handleSuccess(user);
                } else {
                    //ответ пришел, но говорит об ошибке
                    int errorCode = response.code();
                    Log.d(LOG_TAG, "retrofit response is not successful. errorCode: " + String.valueOf(errorCode));
                    Log.d(LOG_TAG, "check API_PRECISE_URL and executeRequest parameters if any");
                    ResponseBody errorBody = response.errorBody();
                    progressBar.dismiss();
                    handleError(errorBody);
                }
            }

            @Override
            public void onFailure (Call <GitModelPOJO> call, Throwable t){
                Log.d(LOG_TAG, "retrofit failure: " + t.getLocalizedMessage());
                Log.d(LOG_TAG, "check API_BASE_URL and internet connection");
                progressBar.dismiss();
                handleFailure(t.getLocalizedMessage());
            }
        };
        Call<GitModelPOJO> call = gitClient.executeRequest(user);
        call.enqueue(callback);
        return true;
    }

    private void handleSuccess(GitModelPOJO user){
        Locale locale = getResources().getConfiguration().locale;
        mTextView.setText(String.format(locale, "Аккаунт Github: %s\nСайт: %s\nКомпания: %s\nАватара: %s",
                user.getName(), user.getBlog(), user.getCompany(), user.getAvatarUrl()));
    }

    private void handleError(ResponseBody errorBody){
        try {
            mTextView.setText(errorBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleFailure(String failureMessage){
        Toast.makeText(MainActivity.this, failureMessage, Toast.LENGTH_LONG).show();
    }

    private Dialog createProgressBar(Context mContext){
        //avoid passing getApplicationContext() as a parameter. pass "this" from activity instead
        Dialog pd = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.progress_bar, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        pd.setContentView(view);
        return pd;
    }

    private interface GitAPI {
        //По-хорошему класс GitModelPOJO следует переименовать в User.
        //Также принято размещать подобные файлы в отдельном пакете Model
        //Base URL: always ends with /
        //@Url: DO NOT start with /
        @GET(API_PRECISE_URL)
        Call<GitModelPOJO> executeRequest(
                @Path("user") String user
        );
    }
}
