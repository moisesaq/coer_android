package moises.com.appcoer.ui.home.course.detail;

import moises.com.appcoer.model.Course;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;


public interface CourseContract {
    interface View extends BaseView<Presenter>{
        void showCurrentCourse();

        void showCourseUpdated(Course course);
    }

    interface Presenter extends BasePresenter{
        void updateCourse(Course course);
    }
}
