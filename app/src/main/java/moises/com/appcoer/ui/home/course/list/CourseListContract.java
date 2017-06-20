package moises.com.appcoer.ui.home.course.list;

import java.util.List;

import moises.com.appcoer.model.Course;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface CourseListContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showProgress(int visibility);

        void showMessageError(int stringId);

        void showCourses(List<Course> courses);
    }

    interface Presenter extends BasePresenter{
        void loadCourses(int page);
    }
}
