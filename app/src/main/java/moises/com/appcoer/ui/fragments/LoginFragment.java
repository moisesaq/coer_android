package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseLoginFragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private InputTextView mUserName, mPassword;
    private Button mSignIn;

    private OnLoginFragmentListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        setTitle(getString(R.string.app_full_name1), getString(R.string.app_full_name2));
        mUserName = (InputTextView)view.findViewById(R.id.itv_user_name);
        mPassword = (InputTextView)view.findViewById(R.id.itv_password);
        Button bLogin = (Button)view.findViewById(R.id.b_login);
        bLogin.setOnClickListener(this);
        Button bGuest = (Button)view.findViewById(R.id.b_guest);
        bGuest.setOnClickListener(this);
        TextView mForgotPassword = (TextView)view.findViewById(R.id.tv_forgot_password);
        mForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_login:
                login(mUserName.getText(), mPassword.getText());
                break;
            case R.id.b_guest:
                mListener.onStartGuest();
                break;
            case R.id.tv_forgot_password:
                mListener.onForgotPasswordClick();
                break;
        }
    }

    private void login(String userName, String password){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<User> userCall = apiClient.login(userName, password);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "SUCCESS >>> " + response.message() + " code " + response.code());
                if(response.body() != null){
                    mListener.onLoginSuccessful(response.body());
                }else{
                    Utils.showDialogMessage("", getString(R.string.error_incorrect_password), null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "ERROR >>> " + t.toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentListener) {
            mListener = (OnLoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginFragmentListener {
        void onLoginSuccessful(User user);
        void onStartGuest();
        void onForgotPasswordClick();
    }
}
