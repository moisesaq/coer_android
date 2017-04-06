package moises.com.appcoer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.fragments.CourseListFragment;
import moises.com.appcoer.ui.fragments.IntroduceLodgingFragment;
import moises.com.appcoer.ui.fragments.MenuFragment;
import moises.com.appcoer.ui.fragments.MethodPaymentsFragment;
import moises.com.appcoer.ui.fragments.NewsListFragment;
import moises.com.appcoer.ui.fragments.ReservationListFragment;
import moises.com.appcoer.ui.fragments.ReserveRoomFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                            CourseListFragment.OnCoursesFragmentListener, MenuFragment.Callback, ReserveRoomFragment.OnReserveRoomFragmentListener{
    private TextView mFullName;
    private TextView mEmail;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalManager.setActivityGlobal(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavMenu(toolbar);
        showFragment(MenuFragment.newInstance(), false);
    }

    private void setupNavMenu(Toolbar toolbar){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mFullName = (TextView)headerView.findViewById(R.id.tv_fullname);
        mEmail = (TextView)headerView.findViewById(R.id.tv_email);
        loadUser();
        //showUserGuide();
    }

    public void openNavigationView(){
        drawer.openDrawer(GravityCompat.START);
    }

    private void showUserGuide(){
        UserGuide.getInstance(GlobalManager.getActivityGlobal()).showStageWithToolbar(UserGuide.StageGuide.STAGE_1, toolbar, new UserGuide.CallBack() {
            @Override
            public void onUserGuideOnClick() {
                openNavigationView();
            }
        });
    }

    public void loadUser(){
        User user = Session.getInstance().getUser();
        if(user == null)
            return;
        mFullName.setText(user.getFullName());
        mEmail.setText(user.getEmail() != null ? user.getEmail() : "");
    }

    public void showFragment(Fragment fragment, boolean stack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(stack)
            ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        } else {
            logout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the camera action
        } else if (id == R.id.nav_reserves) {
            showMyReservations();
        } else if (id == R.id.nav_news) {
            showFragment(NewsListFragment.newInstance(), true);
        } else if (id == R.id.nav_courses) {
            showFragment(CourseListFragment.newInstance(), true);
        } else if (id == R.id.nav_method_payments) {
            showFragment(MethodPaymentsFragment.newInstance(), true);
        } else if (id == R.id.nav_processes) {
        } else if(id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMyReservations(){
        if(Session.getInstance().getUser() != null && Session.getInstance().getUser().getApiToken() != null){
            showFragment(ReservationListFragment.newInstance(), true);
        }else{
            showMessageNeedSignUp();
        }
    }

    private void logout(){
        if(Session.getInstance().getUser() == null){
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.message_logout);
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Session.clearSession();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.create().show();
        }

    }

    /*FRAGMENT COURSE LISTENER*/

    @Override
    public void onCourseClick(Course course) {

    }

    /*FRAGMENT MENU LISTENER*/
    @Override
    public void onNewsClick() {
        showFragment(NewsListFragment.newInstance(), true);
    }

    @Override
    public void onCoursesClick() {
        showFragment(CourseListFragment.newInstance(), true);
    }

    @Override
    public void onLodgingClick(int id) {
        if(Session.getInstance().getUser() != null && Session.getInstance().getUser().getApiToken() != null){
            showFragment(IntroduceLodgingFragment.newInstance(id), true);
        }else{
            showMessageNeedSignUp();
        }
    }

    @Override
    public void onMethodPaymentsClick() {
        showFragment(MethodPaymentsFragment.newInstance(), true);
    }

    /*RESERVE ROOM FRAGMENT LISTENER*/
    @Override
    public void onReserveRoomSuccessful() {
    }

    private void showMessageNeedSignUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_not_logged);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }
}
