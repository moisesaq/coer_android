package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import moises.com.appcoer.ui.view.TextImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = MenuFragment.class.getSimpleName();

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        LinearLayout mNews = (LinearLayout) view.findViewById(R.id.ly_news);
        mNews.setOnClickListener(this);
        LinearLayout mCourses = (LinearLayout)view.findViewById(R.id.ly_courses);
        mCourses.setOnClickListener(this);
        LinearLayout mAccount = (LinearLayout)view.findViewById(R.id.ly_account);
        mAccount.setOnClickListener(this);
        LinearLayout mLodging = (LinearLayout)view.findViewById(R.id.ly_lodging_house);
        mLodging.setOnClickListener(this);
        LinearLayout mTimbue = (LinearLayout)view.findViewById(R.id.ly_timbue);
        mTimbue.setOnClickListener(this);
        LinearLayout mPayment = (LinearLayout)view.findViewById(R.id.ly_method_payment);
        mPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_news:
                replaceFragment(NewsListFragment.newInstance(), true);
                break;
            case R.id.ly_courses:
                replaceFragment(CourseListFragment.newInstance(), true);
                break;
            case R.id.ly_account:
                Utils.showToastMessage("Próximamente");
                break;
            case R.id.ly_lodging_house:
                replaceFragment(ReserveListFragment.newInstance(), true);
                break;
            case R.id.ly_timbue:
                replaceFragment(ReserveListFragment.newInstance(), true);
                break;
            case R.id.ly_method_payment:
                replaceFragment(MethodPaymentsFragment.newInstance(), true);
                break;
        }
    }

    /*-Noticias
    -Cursos
    -Lista de Cuotas
    Reserva de habitaciones
    -Alojamiento Coer
    -Los Timbues
    -Formas de pago*/
}
