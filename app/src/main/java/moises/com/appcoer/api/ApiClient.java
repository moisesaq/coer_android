package moises.com.appcoer.api;

import com.google.gson.JsonObject;

import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.model.LodgingList;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
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
import retrofit2.http.Url;

public interface ApiClient {

    @POST(API.LOGIN)
    Call<User> login(@Query("username") String userName, @Query("password") String password);

    @GET(API.NEWS)
    Call<NewsList> getNews(@Query("per_page") Integer perPage, @Query("page") Integer page,
                           @Query("destacado") Integer outstanding, @Query("api_token") String apiToken);

    @GET
    Call<News> getNewsDescription(@Url String urlNews, @Query("api_token") String apiToken);

    @GET(API.COURSES)
    Call<CourseList> getCourses(@Query("per_page") Integer perPage, @Query("page") Integer page);

    @GET
    Call<Course> getCourseDescription(@Url String urlNews);

    @GET(API.LODGINGS)
    Call<LodgingList> getLodgingList(@Query("api_token") String apiToken);

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
