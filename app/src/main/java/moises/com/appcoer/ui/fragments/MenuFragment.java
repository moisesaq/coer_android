package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Response;

public class MenuFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = MenuFragment.class.getSimpleName();
    private Callback mCallback;

    private View view;
    private TextView mDateEnrollment, mDescriptionEnrollment;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            setupView(view);
        }
        setTitle(getString(R.string.app_name));
        return view;
    }

    private void setupView(View view){
        mDateEnrollment = (TextView)view.findViewById(R.id.tv_date_enrollment);
        mDescriptionEnrollment = (TextView)view.findViewById(R.id.tv_description_enrollment);
        LinearLayout mNews = (LinearLayout)view.findViewById(R.id.ly_news);
        mNews.setOnClickListener(this);
        LinearLayout mCourses = (LinearLayout)view.findViewById(R.id.ly_courses);
        mCourses.setOnClickListener(this);
        LinearLayout mTimbues = (LinearLayout)view.findViewById(R.id.ly_timbues);
        mTimbues.setOnClickListener(this);
        LinearLayout mParana = (LinearLayout)view.findViewById(R.id.ly_parana);
        mParana.setOnClickListener(this);
        LinearLayout mPayment = (LinearLayout)view.findViewById(R.id.ly_method_payment);
        mPayment.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        verifyEnrollment();
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
            case R.id.ly_timbues:
                mCallback.onLodgingClick(1);
                break;
            case R.id.ly_parana:
                mCallback.onLodgingClick(2);
                break;
            case R.id.ly_method_payment:
                mCallback.onMethodPaymentsClick();
                break;
        }
    }

    private void verifyEnrollment(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<Enrollment> enrollmentCall = apiClient.getEnrollmentDate();
        enrollmentCall.enqueue(new retrofit2.Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                try{
                    Log.d(TAG, " SUCCESS >> " + response.body().toString());
                    if(response.body() != null){
                        Date date = Utils.parseStringToDate(response.body().getDate(), Utils.DATE_FORMAT_INPUT);
                        mDateEnrollment.setText(Utils.getCustomizedDate(Utils.DATE_FORMAT_DAY, date));
                        if(response.body().getDescription() != null && !response.body().getDescription().isEmpty())
                            mDescriptionEnrollment.setText(response.body().getDescription());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Enrollment> call, Throwable t) {
                mDateEnrollment.setText("--/--/----");
            }
        });
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
}
