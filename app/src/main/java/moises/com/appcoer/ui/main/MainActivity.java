package moises.com.appcoer.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Bill;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.login.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseActivity;
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

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector, MainContract.View,
        NavigationView.OnNavigationItemSelectedListener, CourseListFragment.OnCoursesFragmentListener,
        MenuFragment.OnMenuFragmentListener {

    @Inject
    DispatchingAndroidInjector<Fragment> injector;
    @Inject
    MenuContract.View menuView;

    protected TextView tvFullName;
    protected TextView tvEmail;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawer;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    private Unbinder unbinder;

    public static void startActivity(Context context) {
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

    private void setupNavMenu() {
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvFullName = headerView.findViewById(R.id.tv_fullname);
        tvEmail = headerView.findViewById(R.id.tv_email);
        loadUser();
        showUserGuide();
    }

    public void openNavigationView() {
        drawer.openDrawer(GravityCompat.START);
    }

    private void showUserGuide() {
        UserGuide.getInstance(GlobalManager.getActivityGlobal())
                .showStageWithToolbar(UserGuide.StageGuide.STAGE_1, toolbar, this::openNavigationView);
    }

    public void loadUser() {
        User user = SessionManager.getInstance(this).getUser();
        if (user == null)
            return;
        tvFullName.setText(user.getFullName() != null ? user.getFullName() : "");
        tvEmail.setText(user.getEmail() != null ? user.getEmail() : "");
    }

    public void showFragment(Fragment fragment, boolean stack) {
        replaceFragment(fragment, R.id.content_main, stack);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (!popBackStack())
            logout();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        } else if (id == R.id.nav_logout) {
            logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMyReservations() {
        if (SessionManager.getInstance(this).getUser() != null &&
                SessionManager.getInstance(this).getUser().getApiToken() != null) {
            showFragment(ReservationsFragment.newInstance(), true);
        } else {
            showMessageNeedSignUp();
        }
    }

    private void logout() {
        if (SessionManager.getInstance(this).getUser() == null) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.message_logout);
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                SessionManager.getInstance(MainActivity.this).clearSession();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
            builder.create().show();
        }
    }

    /**
     * IMPLEMENTATION FRAGMENT COURSE LISTENER
     */

    @Override
    public void onCourseClick(Course course) {
    }

    @Override
    public void onImportantNewsClick(News news) {
        showFragment(NewsFragment.newInstance(news), true);
    }

    /**
     * IMPLEMENTATION FRAGMENT MENU LISTENER
     */
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
        if (SessionManager.getInstance(this).getUser() != null &&
                SessionManager.getInstance(this).getUser().getApiToken() != null) {
            showFragment(HotelFragment.newInstance(id), true);
        } else {
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

    private void showBills() {
        GlobalManager.showProgressDialog();
        if (SessionManager.getInstance(this).getUser() != null && SessionManager.getInstance(this).getUser().getApiToken() != null) {
            ApiService apiClient = RestApiAdapter.getInstance().startConnection();
            Call<Bill> billCall = apiClient.getBill(SessionManager.getInstance(this).getUser().getApiToken());
            billCall.enqueue(new Callback<Bill>() {
                @Override
                public void onResponse(Call<Bill> call, Response<Bill> response) {
                    GlobalManager.dismissProgressDialog();
                    if (response.isSuccessful() && !response.body().getUrl().isEmpty()) {
                        GlobalManager.getCustomTabsIntent(MainActivity.this).launchUrl(MainActivity.this, Uri.parse(response.body().getUrl()));
                    } else {
                        Utils.showToastMessage(getString(R.string.message_something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<Bill> call, Throwable t) {
                    GlobalManager.dismissProgressDialog();
                    Utils.showToastMessage(getString(R.string.message_something_went_wrong));
                }
            });
        } else {
            showMessageNeedSignUp();
        }
    }

    private void showMessageNeedSignUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_not_logged);
        builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> finish());
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * IMPLEMENTATION CONTRACT VIEW
     */
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
