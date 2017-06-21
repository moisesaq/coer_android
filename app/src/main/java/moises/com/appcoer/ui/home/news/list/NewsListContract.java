package moises.com.appcoer.ui.home.news.list;

import java.util.List;

import moises.com.appcoer.model.News;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface NewsListContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showProgress(int visibility);

        void showMessageError(int stringId);

        void showNews(List<News> news);
    }

    interface Presenter extends BasePresenter{
        void loadNews(int page);
    }
}
