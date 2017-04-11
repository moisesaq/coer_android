package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import moises.com.appcoer.R;
import moises.com.appcoer.api.API;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFragment extends Fragment {
    private static final String TAG = CourseFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "course";

    private WebView mContent;
    private Course course;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(Course course) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            course = (Course) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        ImageView mImage = (ImageView)view.findViewById(R.id.iv_course);
        Picasso.with(getContext())
                .load(course.getImage().getSlide())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_course)
                .into(mImage);
        TextView mTitle = (TextView)view.findViewById(R.id.tv_title);
        mTitle.setText(course.getTitle().trim());
        TextView mDate = (TextView)view.findViewById(R.id.tv_date);
        mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(course.getDate(), Utils.DATE_FORMAT_INPUT)));
        TextView mCost = (TextView)view.findViewById(R.id.tv_cost);
        mCost.setText(String.format("%s %s", "$", course.getCost()));

        LinearLayout lyDiscount = (LinearLayout)view.findViewById(R.id.ly_discount);
        TextView mDiscount = (TextView)view.findViewById(R.id.tv_discount);
        if(course.getDiscount() != null || course.getDiscountToDate() != null)
            lyDiscount.setVisibility(View.VISIBLE);

        if(course.getDiscount() != null && !course.getDiscount().isEmpty()){
            mDiscount.setVisibility(View.VISIBLE);
            mDiscount.setText(course.getDiscount());
        }

        TextView mDiscountToDate = (TextView)view.findViewById(R.id.tv_discount_to_date);
        if(course.getDiscountToDate() != null && !course.getDiscountToDate().isEmpty()){
            mDiscountToDate.setVisibility(View.VISIBLE);
            mDiscountToDate.setText(course.getDiscountToDate());
        }

        mContent = (WebView) view.findViewById(R.id.wv_content);
        showContent(course.getContent());
        getDescription();
    }

    private void getDescription(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        String urlCourse = API.COURSES + "/" + course.getId();
        Call<Course> courseCall = apiClient.getCourseDescription(urlCourse);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    CharSequence content = Html.fromHtml(response.body().getContent());
                    showContent(response.body().getContent());
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.d(TAG, " SUCCESS >>> " + t.toString());
            }
        });
    }

    private void showContent(String content){
        mContent.loadData(content, "text/html; charset=utf-8","UTF-8");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
