package moises.com.appcoer.ui.home.menu;

import javax.inject.Inject;

import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.DataHandler;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.session.SessionHandler;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import retrofit2.Call;
import retrofit2.Response;

public class MenuPresenter implements MenuContract.Presenter {

    private final MenuContract.View menuView;
    private final DataHandler dataManager;
    private final SessionHandler session;

    private boolean loadingEnrollment;
    private boolean loadingNews;

    @Inject
    public MenuPresenter(MenuContract.View menuView, DataHandler dataManager, SessionHandler session){
        this.menuView = menuView;
        this.dataManager = dataManager;
        this.session = session;
        //menuView.setPresenter(this);
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
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
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
        ApiService apiClient = RestApiAdapter.getInstance().startConnection();
        User user = SessionManager.getInstance(menuView.getFragment().getActivity()).getUser();
        Call<NewsList> newsListCall = apiClient.getNews(1, null, 1,
                user == null ? null : SessionManager.getInstance(menuView.getFragment().getActivity()).getUser().getApiToken());
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
