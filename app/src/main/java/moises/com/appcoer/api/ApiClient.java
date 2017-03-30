package moises.com.appcoer.api;

import com.google.gson.JsonObject;

import moises.com.appcoer.model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiClient {

    @POST(API.LOGIN)
    Call<User> login(@Query("username") String userName, @Query("password") String password);

    /*@PUT(ApiClient.ApiRest.ME)
    Call<User> updateProfile(@Body JSONObject jsonObject);

    @GET("/public/sexual_orientation")
    Call<JsonObject> getSexOrientationList();

    @Multipart
    @POST(ApiClient.ApiRest.UPLOAD)
    Call<User> upload(@Query("access_token") String token, @Query("file") String fileName,
                      @Part MultipartBody.Part file
    );

    @GET(ApiClient.ApiRest.CONTACTS_LIST)
    Call<Contacts> getContactList(@Query("page") int page);*/
}
