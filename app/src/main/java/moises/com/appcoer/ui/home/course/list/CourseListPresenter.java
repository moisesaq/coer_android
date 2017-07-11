package moises.com.appcoer.ui.home.course.list;

import android.view.View;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.CourseList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseListPresenter implements CourseListContract.Presenter{

    private static final int PAGE_INITIAL = 1;
    private final CourseListContract.View courseListView;

    public CourseListPresenter(CourseListContract.View courseListView){
        this.courseListView = courseListView;
        this.courseListView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        courseListView.showLoading(true);
        loadCourses(PAGE_INITIAL);
    }

    @Override
    public void loadCourses(int page) {
        getCourses(page);
    }

    protected void getCourses(final int page) {
        showProgress(page);
        Call<CourseList> courseListCall = RestApiAdapter.getInstance().startConnection().getCourses(null, page);
        courseListCall.enqueue(new Callback<CourseList>() {
            @Override
            public void onResponse(Call<CourseList> call, Response<CourseList> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getCourses() != null && response.body().getCourses().size() > 0){
                    success(response.body().getCourses());
                }else{
                    failed(R.string.message_without_courses, page);
                }
                courseListView.showProgress(View.GONE);
            }

            @Override
            public void onFailure(Call<CourseList> call, Throwable t) {
                failed(R.string.message_something_went_wrong, page);
            }
        });
    }

    private void showProgress(int page){
        courseListView.showProgress(page > PAGE_INITIAL ? View.VISIBLE : View.GONE);
    }

    private void success(List<Course> courses){
        courseListView.showLoading(false);
        courseListView.showCourses(courses);
    }

    private void failed(int stringId, int page){
        if(page == PAGE_INITIAL)
            courseListView.showMessageError(stringId);

    }

}
