package moises.com.appcoer.global;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.CoerApplication;

public class Session {
    //Shared Preferences file name
    public static final String SESSION = "session_preferences";
    //Shared Preferences keys
    public static final String USER_DATA = "user_data";

    private static Session session;
    private User user;

    private Session(){
    }

    public static Session getInstance(){
        if(session == null)
            session = new Session();
        return session;
    }

    public User getUser() {
        if(user == null)
            user = getCurrentUser();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        saveUser(user);
    }

    /*SHARED PREFERENCES SESSION*/
    private void saveUser(User user){
        SharedPreferences sharedPref = CoerApplication.getContext().getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_DATA, user == null ? "" : user.toString());
        editor.apply();
    }

    private User getCurrentUser(){
        SharedPreferences sharedPreferences = CoerApplication.getContext().getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(USER_DATA, "");
        return userJson.isEmpty() ? null : new Gson().fromJson(userJson, User.class);
    }

    public static void clearSession(){
        Session.getInstance().setUser(null);
        SharedPreferences sharedPreferences = CoerApplication.getContext().getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_DATA);
        editor.apply();
    }
}
