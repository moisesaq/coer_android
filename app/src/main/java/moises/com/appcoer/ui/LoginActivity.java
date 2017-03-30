package moises.com.appcoer.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import moises.com.appcoer.R;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showFragment(LoginFragment.newInstance(""), false);
    }

    private void showFragment(Fragment fragment, boolean stack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(stack)
            ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.activity_login, fragment);
        ft.commit();
    }

    /*LOGIN FRAGMENT LISTENER*/
    @Override
    public void onFragmentInteraction(User user) {
        Session.getInstance().setUser(user);
        goToMainActivity();
    }

    private void goToMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
