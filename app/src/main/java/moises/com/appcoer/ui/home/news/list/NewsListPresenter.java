package moises.com.appcoer.ui.home.news.list;

import android.view.View;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListPresenter implements NewsListContract.Presenter{

    private static final int PAGE_INITIAL = 1;
    private final NewsListContract.View newsListView;

    public NewsListPresenter(NewsListContract.View newsListView){
        this.newsListView = newsListView;
        this.newsListView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        newsListView.showLoading(true);
        loadNews(PAGE_INITIAL);
    }

    @Override
    public void loadNews(int page) {
        getNews(page);
    }

    private void getNews(final int page) {
        showProgress(page);
        User user = Session.getInstance().getUser();
        Call<NewsList> newsListCall = RestApiAdapter.getInstance().startConnection()
                .getNews(null, page, 0, user == null ? null : user.getApiToken());
        newsListCall.enqueue(new Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                if(response.isSuccessful() && response.body() != null &&
                        response.body().getNews() != null){
                    success(response.body().getNews());
                }else {
                    failed(R.string.message_withot_news, page);
                }
                newsListView.showProgress(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                failed(R.string.message_something_went_wrong, page);
            }
        });
    }

    private void showProgress(int page){
        newsListView.showProgress(page > PAGE_INITIAL ? View.VISIBLE : View.GONE);
    }

    private void success(List<News> news){
        newsListView.showLoading(false);
        newsListView.showNews(news);
    }

    private void failed(int stringId, int page){
        if(page == PAGE_INITIAL)
            newsListView.showMessageError(stringId);

    }
}
