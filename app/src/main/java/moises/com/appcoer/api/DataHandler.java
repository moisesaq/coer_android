package moises.com.appcoer.api;

import io.reactivex.Observable;
import moises.com.appcoer.model.login.User;

public interface DataHandler {

    Observable<User> login(String username, String password);
}
