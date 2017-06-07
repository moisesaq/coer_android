package moises.com.appcoer.api;
import io.reactivex.Observable;
import io.reactivex.Single;
import moises.com.appcoer.model.User;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    public static String CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded";
    public static String CONTENT_TYPE_JSON = "application/json";

    private static RestApiAdapter respApiAdapter;

    private RestApiAdapter(){
    }

    public static RestApiAdapter getInstance(){
        if(respApiAdapter == null)
            respApiAdapter = new RestApiAdapter();
        return respApiAdapter;
    }

    public ApiClient startConnection(){
        OkHttpClient.Builder httBuilder = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        Retrofit retrofit = builder.client(httBuilder.build()).build();
        return retrofit.create(ApiClient.class);
    }

    public static Observable<User> login(String userName, String password){
        return getInstance().startConnection().startLogin(userName, password);
    }
}
