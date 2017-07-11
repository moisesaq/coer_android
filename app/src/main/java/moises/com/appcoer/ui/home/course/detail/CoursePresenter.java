package moises.com.appcoer.ui.home.course.detail;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moises.com.appcoer.api.API;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Course;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursePresenter implements CourseContract.Presenter{

    private final CourseContract.View courseView;

    public CoursePresenter(CourseContract.View courseView){
        this.courseView = courseView;
        this.courseView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        courseView.showCurrentCourse();
    }

    @Override
    public void updateCourse(Course course) {
        getCourseUpdated(course);
    }

    //TODO Later change to this
    private void getDetail(Course course){
        String urlCourse = API.COURSES + "/" + course.getId();
        RestApiAdapter.getCourseDescription(urlCourse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::success, this::failed);
    }

    private void getCourseUpdated(Course course){
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
        String urlCourse = String.format("%s%s%s", API.COURSES, "/", course.getId());
        Call<Course> courseCall = apiClient.getCourseDescription(urlCourse);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if(response.isSuccessful())
                    success(response.body());
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                failed(t);
            }
        });
    }

    private void success(Course course){
        courseView.showCourseUpdated(course);
    }

    private void failed(Throwable throwable){
        Log.d("ERROR", " FAILED >>> " + throwable.toString());
    }
}
