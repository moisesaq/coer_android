package moises.com.appcoer.ui.home.menu;

import android.support.annotation.NonNull;

import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.News;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface MenuContract {

    interface View extends BaseView<Presenter>{

        void showEnrollment(@NonNull Enrollment enrollment);

        void showNewsOutstanding(@NonNull  News news);
    }

    interface Presenter extends BasePresenter{

        void updateEnrollment();

        void getNewsOutstanding();
    }
}
