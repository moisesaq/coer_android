package moises.com.appcoer.global.session;

import moises.com.appcoer.model.User;

public interface SessionHandler {

    void setUser(User user);

    User getUser();

    void clearSession();
}
