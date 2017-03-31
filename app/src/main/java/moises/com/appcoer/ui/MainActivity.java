package moises.com.appcoer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import moises.com.appcoer.R;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.User;
import moises.com.appcoer.ui.fragments.CourseListFragment;
import moises.com.appcoer.ui.fragments.MethodPaymentsFragment;
import moises.com.appcoer.ui.fragments.NewsListFragment;
import moises.com.appcoer.ui.fragments.ProcessesFragment;
import moises.com.appcoer.ui.fragments.ReserveListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                        NewsListFragment.OnFragmentInteractionListener, CourseListFragment.OnCoursesFragmentListener{

    private CircleImageView mImageUser;
    private TextView mFullName;
    private TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupNavMenu(toolbar);
        showFragment(NewsListFragment.newInstance(), false);
    }

    private void setupNavMenu(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mImageUser = (CircleImageView)headerView.findViewById(R.id.civ_image_user);
        mFullName = (TextView)headerView.findViewById(R.id.tv_fullname);
        mEmail = (TextView)headerView.findViewById(R.id.tv_email);
        loadUser();
    }

    public void loadUser(){
        User user = Session.getInstance().getUser();
        if(user == null)
            return;
        mFullName.setText(user.getFullName());
        mEmail.setText(user.getEmail());
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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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
            showFragment(ReserveListFragment.newInstance(), true);
        } else if (id == R.id.nav_news) {
            showFragment(NewsListFragment.newInstance(), true);
        } else if (id == R.id.nav_courses) {
            showFragment(CourseListFragment.newInstance(), true);
        } else if (id == R.id.nav_method_payments) {
            showFragment(MethodPaymentsFragment.newInstance(), true);
        } else if (id == R.id.nav_processes) {
            showFragment(ProcessesFragment.newInstance(), true);
        } else if(id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you safe do you want exit?");
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

    /*FRAGMENT LISTENERS*/
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCourseClick(Course course) {

    }
}
