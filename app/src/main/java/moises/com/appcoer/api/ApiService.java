package moises.com.appcoer.api;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import moises.com.appcoer.model.Bill;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.model.Hotel;
import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.model.login.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @POST(API.LOGIN)
    Call<User> login(@Query("username") String userName, @Query("password") String password);

    @POST(API.LOGIN)
    Single<User> startLogin(@Query("username") String username, @Query("password") String password);

    @FormUrlEncoded
    @PUT(API.CHANGE_PASSWORD)
    @Headers("Accept: application/json")
    Call<List<User>> changePassword(@Field("new_password") String newPassword, @Field("email") String email, @Query("api_token") String apiToken);

    @FormUrlEncoded
    @PUT(API.RESET_PASSWORD)
    Call<ResponseBody> resetPassword(@Field("email") String email);


    @GET(API.NEWS)
    Call<NewsList> getNews(@Query("per_page") Integer perPage, @Query("page") Integer page,
                           @Query("destacado") Integer outstanding, @Query("api_token") String apiToken);

    @GET
    Call<News> getNewsDescription(@Url String urlNews, @Query("api_token") String apiToken);

    @GET(API.COURSES)
    Call<CourseList> getCourses(@Query("per_page") Integer perPage, @Query("page") Integer page);

    @GET
    Call<Course> getCourseDescription(@Url String urlNews);

    @GET
    Observable<Course> getCourseDescription2(@Url String urlNews);

    @GET(API.LODGINGS)
    Call<List<Hotel>> getLodgingList(@Query("api_token") String apiToken);

    @GET(API.ROOM_LIST)
    Call<List<Room>> getRoomList(@Path("id") int idLodging , @Query("api_token") String apiToken);

    @GET(API.ENROLLMENT_DATE)
    Call<Enrollment> getEnrollmentDate();

    @GET(API.METHOD_PAYMENTS)
    Call<List<MethodPayment>> getMethodPayments();

    @POST(API.RESERVES)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Reservation> reserveRoom(@Path("id") int id, @Query("api_token") String apiToken, @Body Reservation reservation);

    @GET(API.RESERVES_LIST)
    Call<List<Reservation>> getReservations(@Query("api_token") String apiToken);

    @GET(API.ROOM_DATES_BUSY)
    Call<List<String>> getRoomBusyDate(@Path("id") int idRoom, @Query("api_token") String apiToken);

    @GET(API.PROCESSES)
    Call<List<Process>> getProcesses();

    @GET(API.PROCESS_DETAIL)
    Call<Process> getProcessDetail(@Path("id") int id);

    @GET(API.BILLS)
    Call<Bill> getBill(@Query("api_token") String apiToken);
}
