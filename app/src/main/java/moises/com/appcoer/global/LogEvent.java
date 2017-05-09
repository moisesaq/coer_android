package moises.com.appcoer.global;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class LogEvent {

    public static final String EVENT_START_SESSION = "Start session";
    public static final String EVENT_RESERVE_ROON = "Reserve room";

    public static void logEventFirebaseAnalytic(Context context, String event){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("user_name", Session.getInstance().getUser().getFullName());
        if(Session.getInstance().getUser().getEmail() != null)
            bundle.putString("user_email", Session.getInstance().getUser().getEmail());
        firebaseAnalytics.logEvent(event, bundle);
    }
}
