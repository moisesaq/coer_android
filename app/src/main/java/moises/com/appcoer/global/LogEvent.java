package moises.com.appcoer.global;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import moises.com.appcoer.global.session.SessionManager;

public class LogEvent {

    public static final String EVENT_START_SESSION = "Start session";
    public static final String EVENT_RESERVE_ROON = "Reserve room";

    public static void logEventFirebaseAnalytic(Context context, String event){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("user_name", SessionManager.getInstance(context).getUser().getFullName());
        if(SessionManager.getInstance(context).getUser().getEmail() != null)
            bundle.putString("user_email", SessionManager.getInstance(context).getUser().getEmail());
        firebaseAnalytics.logEvent(event, bundle);
    }
}
