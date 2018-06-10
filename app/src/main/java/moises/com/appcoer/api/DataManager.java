package moises.com.appcoer.api;

import javax.inject.Inject;

import io.reactivex.Observable;
import moises.com.appcoer.model.login.User;

public class DataManager implements DataHandler{
    private ApiService apiService;

    @Inject
    public DataManager(ApiService apiService){
        this.apiService = apiService;
    }

    @Override
    public Observable<User> login(String username, String password) {
        return apiService.startLogin(username, password).toObservable();
    }
}
