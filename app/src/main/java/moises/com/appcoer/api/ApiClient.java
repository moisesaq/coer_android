package moises.com.appcoer.api;

import com.google.gson.JsonObject;

import java.util.List;

import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.model.Lodging;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    Call<List<Lodging>> getLodgingList(@Query("api_token") String apiToken);

    @GET
    Call<List<Room>> getRoomList(@Url String urlLodging, @Query("api_token") String apiToken);
}
