package moises.com.appcoer.global.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import moises.com.appcoer.model.User;

public class SessionManager implements SessionHandler{
    private static final String SESSION = "session_preferences";
    private static final String USER_DATA = "user_data";

    private static SessionManager sessionManager;
    private User user;

    private Context context;

    @Inject
    public SessionManager(Context context){
        this.context = context;
    }

    public static SessionManager getInstance(Context context){
        if(sessionManager == null)
            sessionManager = new SessionManager(context);
        return sessionManager;
    }

    @Override
    public User getUser() {
        if(user == null)
            user = getCurrentUser();
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        saveUser(user);
    }

    /** SHARED PREFERENCES SESSION */
    private void saveUser(User user){
        SharedPreferences sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_DATA, user == null ? "" : user.toString());
        editor.apply();
    }

    private User getCurrentUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(USER_DATA, "");
        return userJson.isEmpty() ? null : new Gson().fromJson(userJson, User.class);
    }

    @Override
    public void clearSession(){
        setUser(null);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_DATA);
        editor.apply();
    }
}
