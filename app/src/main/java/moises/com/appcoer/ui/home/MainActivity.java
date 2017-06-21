package moises.com.appcoer.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Bill;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.home.course.list.CourseListFragment;
import moises.com.appcoer.ui.home.hotel.HotelFragment;
import moises.com.appcoer.ui.home.menu.MenuFragment;
import moises.com.appcoer.ui.home.news.detail.NewsFragment;
import moises.com.appcoer.ui.home.news.list.NewsListFragment;
import moises.com.appcoer.ui.home.payments.PaymentsFragment;
import moises.com.appcoer.ui.home.process.list.ProcessListFragment;
import moises.com.appcoer.ui.home.reservations.ReservationsFragment;
import moises.com.appcoer.ui.home.reserve.ReserveRoomFragment;
import moises.com.appcoer.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CourseListFragment.OnCoursesFragmentListener,
                                                                    MenuFragment.OnMenuFragmentListener, ReserveRoomFragment.OnReserveRoomFragmentListener{
    private TextView mFullName;
    private TextView mEmail;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

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
        showUserGuide();
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
        mFullName.setText(user.getFullName() != null ? user.getFullName() : "");
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
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the camera action
        } else if (id == R.id.nav_reserves) {
            showMyReservations();
        } else if (id == R.id.nav_news) {
            showFragment(NewsListFragment.newInstance(false), true);
        } else if (id == R.id.nav_courses) {
            showFragment(CourseListFragment.newInstance(), true);
        } else if (id == R.id.nav_method_payments) {
            showFragment(PaymentsFragment.newInstance(), true);
        } else if (id == R.id.nav_processes) {
            showFragment(ProcessListFragment.newInstance(), true);
        } else if (id == R.id.nav_bills) {
            showBills();
        }else if(id == R.id.nav_logout){
            logout();
            //showFragment(TestFragment.newInstance(), true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMyReservations(){
        if(Session.getInstance().getUser() != null && Session.getInstance().getUser().getApiToken() != null){
            showFragment(ReservationsFragment.newInstance(), true);
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

    @Override
    public void onImportantNewsClick(News news) {
        showFragment(NewsFragment.newInstance(news), true);
    }

    /*FRAGMENT MENU LISTENER*/
    @Override
    public void onNewsClick() {
        showFragment(NewsListFragment.newInstance(true), true);
    }

    @Override
    public void onCoursesClick() {
        showFragment(CourseListFragment.newInstance(), true);
    }

    @Override
    public void onLodgingClick(int id) {
        if(Session.getInstance().getUser() != null && Session.getInstance().getUser().getApiToken() != null){
            showFragment(HotelFragment.newInstance(id), true);
        }else{
            showMessageNeedSignUp();
        }
    }

    @Override
    public void onProcessesClick() {
        showFragment(ProcessListFragment.newInstance(), true);
    }

    @Override
    public void onMethodPaymentsClick() {
        showFragment(PaymentsFragment.newInstance(), true);
    }

    @Override
    public void onBillsClick() {
        showBills();
    }

    private void showBills(){
        GlobalManager.showProgressDialog();
        if(Session.getInstance().getUser() != null && Session.getInstance().getUser().getApiToken() != null){
            ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
            Call<Bill> billCall = apiClient.getBill(Session.getInstance().getUser().getApiToken());
            billCall.enqueue(new Callback<Bill>() {
                @Override
                public void onResponse(Call<Bill> call, Response<Bill> response) {
                    GlobalManager.dismissProgressDialog();
                    if(response.isSuccessful() && !response.body().getUrl().isEmpty()) {
                        GlobalManager.getCustomTabsIntent(MainActivity.this).launchUrl(MainActivity.this, Uri.parse(response.body().getUrl()));
                    }else{
                        Utils.showToastMessage(getString(R.string.message_something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<Bill> call, Throwable t) {
                    GlobalManager.dismissProgressDialog();
                    Utils.showToastMessage(getString(R.string.message_something_went_wrong));
                }
            });
        }else{
            showMessageNeedSignUp();
        }
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

    private Fragment getFragment(String tag){
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
