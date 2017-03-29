package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.ApiClientDeserializer;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.ui.LoginActivity;
import moises.com.appcoer.ui.MainActivity;
import moises.com.appcoer.ui.view.InputTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = LoginFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private InputTextView mUserName, mPassword;
    private Button mSignIn;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        mUserName = (InputTextView)view.findViewById(R.id.itv_user_name);
        mPassword = (InputTextView)view.findViewById(R.id.itv_password);
        Button bLogin = (Button)view.findViewById(R.id.b_login);
        bLogin.setOnClickListener(this);
        Button bGuest = (Button)view.findViewById(R.id.b_guest);
        bGuest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_login:
                login(mUserName.getText(), mPassword.getText());
                break;
            case R.id.b_guest:
                break;
        }
    }

    private void login(String userName, String password){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection(RestApiAdapter.buildGenericDeserializer());
        Call<JsonObject> jsonObjectCall = apiClient.login(userName, password);
        jsonObjectCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "SUCCESS >>> " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "ERROR >>> " + t.toString());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
