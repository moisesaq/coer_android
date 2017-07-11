package moises.com.appcoer.ui.home.news.detail;

import android.util.Log;

import moises.com.appcoer.api.API;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsPresenter implements NewsContract.Presenter{

    private final NewsContract.View newsView;

    public NewsPresenter(NewsContract.View newsView){
        this.newsView = newsView;
        this.newsView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        newsView.showNews();
    }

    @Override
    public void updateNews(int newsId) {
        getDescription(newsId);
    }

    private void getDescription(int newsId){
        String urlNews = String.format("%s%s%s", API.NEWS, "/", newsId);
        Call<News> newsCall = RestApiAdapter.getInstance().startConnection().getNewsDescription(
                urlNews,
                Session.getInstance().getUser() == null ? null : Session.getInstance().getUser().getApiToken());
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful())
                    success(response.body());
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                failed(t);
            }
        });
    }

    private void success(News news){
        newsView.showNewsUpdated(news);
    }

    private void failed(Throwable throwable){
        Log.d("ERROR", " FAILED >>> " + throwable.toString());
    }
}
