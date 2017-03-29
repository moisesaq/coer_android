package moises.com.appcoer.ui.interactors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;

public class LoginInteractor {
    private Context mContext;

    public LoginInteractor(Context context){
        this.mContext = context;
    }

    public void login(String email, String password, final Callback callback) {
        // Check lógica
        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);
        if (!(c1 && c2)) {
            return;
        }

        // Check red
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callback)) {
            return;
        }

        // Consultar Firebase Authentication
        loginUser(email, password, callback);
    }

    private boolean isValidEmail(String email, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            callback.onEmailError("Escribe tu correo");
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError("Correo no válido");
            isValid = false;
        }
        // Más reglas de negocio...
        return isValid;
    }

    private boolean isValidPassword(String password, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password)) {
            callback.onPasswordError("Escribe tu contraseña");
            isValid = false;
        }

        // Más reglas de negocio...
        return isValid;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable(Callback callback) {
        /*int statusCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(mContext);

        if (GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)) {
            callback.onBeUserResolvableError(statusCode);
            return false;
        } else if (statusCode != ConnectionResult.SUCCESS) {
            callback.onGooglePlayServicesFailed();
            return false;
        }*/

        return true;
    }

    private void loginUser(String email, String password, Callback callback){

    }

    interface Callback {

        void onEmailError(String msg);

        void onPasswordError(String msg);

        void onNetworkConnectFailed();

        void onBeUserResolvableError(int errorCode);

        void onGooglePlayServicesFailed();

        void onAuthFailed(String msg);

        void onAuthSuccess();
    }
}
