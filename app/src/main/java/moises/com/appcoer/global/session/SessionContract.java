package moises.com.appcoer.global.session;

import moises.com.appcoer.model.login.User;

public interface SessionContract {

    void setUser(User user);

    User getUser();

    void clearSession();
}
