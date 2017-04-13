package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Enrollment;
import moises.com.appcoer.model.News;
import moises.com.appcoer.model.NewsList;
import moises.com.appcoer.model.User;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Response;

public class MenuFragment extends BaseFragment{

    private static final String TAG = MenuFragment.class.getSimpleName();
    private Callback mCallback;
    private View view;
    private News mImportantNews;
    private boolean isLoadingNews, isLoadingEnrollment;

    protected @BindView(R.id.tv_date_enrollment) TextView mDateEnrollment;
    protected @BindView(R.id.tv_description_enrollment) TextView mDescriptionEnrollment;
    protected @BindView(R.id.cv_news) CardView cvNews;
    protected @BindView(R.id.iv_image_news) ImageView mImageNews;
    protected @BindView(R.id.tv_title_news) TextView mTitleNews;
    protected @BindView(R.id.tv_description_news) TextView mDescriptionNews;

    protected @BindView(R.id.ly_news) LinearLayout mNews;
    protected @BindView(R.id.ly_courses) LinearLayout mCourses;
    protected @BindView(R.id.ly_timbues) LinearLayout mTimbues;
    protected @BindView(R.id.ly_parana) LinearLayout mParana;
    protected @BindView(R.id.ly_process) LinearLayout mProcess;
    protected @BindView(R.id.ly_method_payment) LinearLayout mMethodPayment;

    public MenuFragment() {
    }

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            ButterKnife.bind(this, view);
        }
        setTitle(getString(R.string.app_name));
        loadInformation();
        return view;
    }

    @OnClick({R.id.cv_news, R.id.ly_news, R.id.ly_courses, R.id.ly_timbues, R.id.ly_parana, R.id.ly_process, R.id.ly_method_payment})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_news:
                if(mImportantNews != null)
                    mCallback.onImportantNewsClick(mImportantNews);
                break;
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
            case R.id.ly_process:
                mCallback.onProcessesClick();
                break;
            case R.id.ly_method_payment:
                mCallback.onMethodPaymentsClick();
                break;
        }
    }

    private void loadInformation(){
        if(!isLoadingEnrollment)
            verifyEnrollment();
        if(!isLoadingNews)
            getImportantNews();
    }

    private void verifyEnrollment(){
        isLoadingEnrollment = true;
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<Enrollment> enrollmentCall = apiClient.getEnrollmentDate();
        enrollmentCall.enqueue(new retrofit2.Callback<Enrollment>() {
            @Override
            public void onResponse(Call<Enrollment> call, Response<Enrollment> response) {
                isLoadingEnrollment = false;
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
                isLoadingEnrollment = false;
            }
        });
    }

    private void getImportantNews() {
        isLoadingNews = true;
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        User user = Session.getInstance().getUser();
        Call<NewsList> newsListCall = apiClient.getNews(1, null, 1, user == null ? null : Session.getInstance().getUser().getApiToken());
        newsListCall.enqueue(new retrofit2.Callback<NewsList>() {
            @Override
            public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                isLoadingNews = false;
                if(response.isSuccessful() && response.body() != null && response.body().getNews() != null && response.body().getNews().size() > 0){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    mImportantNews = response.body().getNews().get(0);
                    cvNews.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(mImportantNews.getImage().getThumbnail())
                            .placeholder(R.mipmap.image_load)
                            .error(R.drawable.example_coer)
                            .into(mImageNews);
                    mTitleNews.setText(mImportantNews.getTitle().trim());
                    mDescriptionNews.setText(mImportantNews.getContent());
                }
            }

            @Override
            public void onFailure(Call<NewsList> call, Throwable t) {
                isLoadingNews = false;
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
        void onImportantNewsClick(News news);
        void onNewsClick();
        void onCoursesClick();
        void onLodgingClick(int id);
        void onProcessesClick();
        void onMethodPaymentsClick();
    }
}
