package tau.retrofit2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by TAU on 22.04.2016.
 */
public class ServiceGenerator {
    //является заменой старого RestAdapter
    private static final String API_BASE_URL = "https://api.github.com/";
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(new OkHttpClient()).build();
        return retrofit.create(serviceClass);
    }
}
