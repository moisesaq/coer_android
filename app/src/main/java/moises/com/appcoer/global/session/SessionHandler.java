package moises.com.appcoer.global.session;

import moises.com.appcoer.model.login.User;

public interface SessionHandler {

    void setUser(User user);

    User getUser();

    void clearSession();
}
