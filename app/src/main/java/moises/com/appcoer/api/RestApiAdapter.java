package moises.com.appcoer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import moises.com.appcoer.model.User;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    public static String CONTENT_TYPE_APPLICATION = "application/x-www-form-urlencoded";
    public static String CONTENT_TYPE_JSON = "application/json";

    public static RestApiAdapter getInstance(){
        return new RestApiAdapter();
    }

    public ApiClient startConnection(Gson gson){
        return startConnection(gson, CONTENT_TYPE_APPLICATION);
    }

    public ApiClient startConnection(Gson gson, final String contentType){
        OkHttpClient.Builder httBuilder = new OkHttpClient.Builder();

        /*if(!Session.getInstance().getToken().isEmpty() && addBearer){
            httBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder();
                    if(addBearer)
                        requestBuilder.header("Authorization", "Bearer " + Session.getInstance().getToken());
                    requestBuilder.header("Content-Type", contentType); // <-- this is the important line

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }*/

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API.COER)
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(httBuilder.build()).build();
        return retrofit.create(ApiClient.class);
    }

    public static Gson buildGenericDeserializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(JsonObject.class, new ApiClientDeserializer.GenericDeserializer());
        return gsonBuilder.create();
    }

    public static Gson buildUserDeserializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new ApiClientDeserializer.UserDeserializer());
        return gsonBuilder.create();
    }
}
