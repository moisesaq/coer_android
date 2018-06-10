package moises.com.appcoer.ui.home.news.detail;

import moises.com.appcoer.model.News;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface NewsContract {

    interface View extends BaseView<Presenter>{

        void showNews();

        void showNewsUpdated(News news);
    }

    interface Presenter extends BasePresenter{

        void updateNews(int newsId);
    }
}
