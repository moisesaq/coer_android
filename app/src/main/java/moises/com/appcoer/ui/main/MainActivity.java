package moises.com.appcoer.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiService;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.session.SessionHandler;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Bill;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.home.course.list.CourseListFragment;
import moises.com.appcoer.ui.home.hotel.HotelFragment;
import moises.com.appcoer.ui.home.menu.MenuContract;
import moises.com.appcoer.ui.home.menu.MenuFragment;
import moises.com.appcoer.ui.home.news.detail.NewsFragment;
import moises.com.appcoer.ui.home.news.list.NewsListFragment;
import moises.com.appcoer.ui.home.payments.PaymentsFragment;
import moises.com.appcoer.ui.home.process.list.ProcessListFragment;
import moises.com.appcoer.ui.home.reservations.ReservationsFragment;
import moises.com.appcoer.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        MainContract.View, NavigationView.OnNavigationItemSelectedListener,
        CourseListFragment.OnCoursesFragmentListener, MenuFragment.OnMenuFragmentListener,
        HasSupportFragmentInjector{

    @Inject DispatchingAndroidInjector<Fragment> injector;
    @Inject SessionHandler session;
    @Inject MenuContract.View menuView;

    protected TextView tvFullName;
    protected TextView tvEmail;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.drawer_layout) protected DrawerLayout drawer;
    @BindView(R.id.nav_view) protected NavigationView navigationView;

    private Unbinder unbinder;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalManager.setActivityGlobal(this);
        setupNavMenu();
        showFragment(menuView.getFragment(), false);
    }

    private void setupNavMenu(){
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvFullName = (TextView)headerView.findViewById(R.id.tv_fullname);
        tvEmail = (TextView)headerView.findViewById(R.id.tv_email);
        loadUser();
        showUserGuide();
    }

    public void openNavigationView(){
        drawer.openDrawer(GravityCompat.START);
    }

    private void showUserGuide(){
        UserGuide.getInstance(GlobalManager.getActivityGlobal())
                .showStageWithToolbar(UserGuide.StageGuide.STAGE_1, toolbar,
                        new UserGuide.CallBack() {
            @Override
            public void onUserGuideOnClick() {
                openNavigationView();
            }
        });
    }

    public void loadUser(){
        User user = SessionManager.getInstance(this).getUser();
        if(user == null)
            return;
        tvFullName.setText(user.getFullName() != null ? user.getFullName() : "");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "");
    }

    public void showFragment(Fragment fragment, boolean stack){
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(stack ? fragment.getClass().getSimpleName() : null)
                .replace(R.id.content_main, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
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
        if (id == R.id.nav_reserves) {
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMyReservations(){
        if(SessionManager.getInstance(this).getUser() != null &&
                SessionManager.getInstance(this).getUser().getApiToken() != null){
            showFragment(ReservationsFragment.newInstance(), true);
        }else{
            showMessageNeedSignUp();
        }
    }

    private void logout(){
        if(SessionManager.getInstance(this).getUser() == null){
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
                    SessionManager.getInstance(MainActivity.this).clearSession();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.create().show();
        }
    }

    /** IMPLEMENTATION FRAGMENT COURSE LISTENER */

    @Override
    public void onCourseClick(Course course) {
    }

    @Override
    public void onImportantNewsClick(News news) {
        showFragment(NewsFragment.newInstance(news), true);
    }

    /** IMPLEMENTATION FRAGMENT MENU LISTENER */
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
        if(SessionManager.getInstance(this).getUser() != null &&
                SessionManager.getInstance(this).getUser().getApiToken() != null){
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
        if(SessionManager.getInstance(this).getUser() != null && SessionManager.getInstance(this).getUser().getApiToken() != null){
            ApiService apiClient = RestApiAdapter.getInstance().startConnection();
            Call<Bill> billCall = apiClient.getBill(SessionManager.getInstance(this).getUser().getApiToken());
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

    /** IMPLEMENTATION CONTRACT VIEW */
    @Override
    public void startOnBoardingActivity() {

    }

    @Override
    public void showBill(Bill bill) {

    }

    @Override
    public void showBillFailed() {

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return injector;
    }
}
