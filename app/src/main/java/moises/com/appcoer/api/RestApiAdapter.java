package moises.com.appcoer.api;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    public static String CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded";
    public static String CONTENT_TYPE_JSON = "application/json";

    public static RestApiAdapter getInstance(){
        return new RestApiAdapter();
    }

    public ApiClient startConnection(){
        OkHttpClient.Builder httBuilder = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API.COER)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httBuilder.build()).build();
        return retrofit.create(ApiClient.class);
    }
}
