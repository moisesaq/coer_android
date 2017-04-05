package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import moises.com.appcoer.R;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;

public class MenuFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = MenuFragment.class.getSimpleName();
    private Callback mCallback;
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
        LinearLayout mNews = (LinearLayout)view.findViewById(R.id.ly_news);
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
                mCallback.onNewsClick();
                break;
            case R.id.ly_courses:
                mCallback.onCoursesClick();
                break;
            case R.id.ly_account:
                Utils.showToastMessage("Próximamente");
                break;
            case R.id.ly_lodging_house:
                mCallback.onLodgingClick(1);
                break;
            case R.id.ly_timbue:
                mCallback.onLodgingClick(2);
                break;
            case R.id.ly_method_payment:
                mCallback.onMethodPaymentsClick();
                break;
        }
    }

    private void showDetailLodgingFragment(int id){
        if(Session.getInstance().getUser() != null){
            replaceFragment(IntroduceLodgingFragment.newInstance(2), true);
        }else {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.Callback) {
            mCallback = (MenuFragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface Callback{
        void onNewsClick();
        void onCoursesClick();
        void onLodgingClick(int id);
        void onMethodPaymentsClick();
    }

    /*-Noticias
    -Cursos
    -Lista de Cuotas
    Reserva de habitaciones
    -Alojamiento Coer
    -Los Timbues
    -Formas de pago*/
}
