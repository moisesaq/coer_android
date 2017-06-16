package moises.com.appcoer.ui.home.menu;

import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import retrofit2.Call;
import retrofit2.Response;

public class MenuPresenter implements MenuContract.Presenter {

    private final MenuContract.View menuView;

    private boolean loadingEnrollment;
    private boolean loadingNews;

    public MenuPresenter(MenuContract.View menuView){
        this.menuView = menuView;
        menuView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        initialize();
    }

    private void initialize(){
        if(!loadingEnrollment) updateEnrollment();
        if(!loadingNews) getNewsOutstanding();
    }

    @Override
    public void updateEnrollment() {
        getEnrollment();
    }

    @Override
    public void getNewsOutstanding() {
        getImportantNews();
    }

    private void getEnrollment(){
        loadingEnrollment = true;
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<Enrollment> enrollmentCall = apiClient.getEnrollmentDate();
        enrollmentCall.enqueue(new retrofit2.Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                loadingEnrollment = false;
                if(response.isSuccessful() && response.body() != null)
                    menuView.showEnrollment(response.body());
            }

            @Override
            public void onFailure(Call<Enrollment> call, Throwable t) {
                loadingEnrollment = false;
            }
        });
    }

    private void getImportantNews() {
        loadingNews = true;
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        User user = Session.getInstance().getUser();
        Call<NewsList> newsListCall = apiClient.getNews(1, null, 1,
                user == null ? null : Session.getInstance().getUser().getApiToken());
        newsListCall.enqueue(new retrofit2.Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                loadingNews = false;
                if(response.isSuccessful() && response.body() != null &&
                        response.body().getNews() != null && response.body().getNews().size() > 0)
                    menuView.showNewsOutstanding(response.body().getNews().get(0));
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                loadingNews = false;
            }
        });
    }
}
