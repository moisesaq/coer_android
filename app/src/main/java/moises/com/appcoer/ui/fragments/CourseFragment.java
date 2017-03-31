package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private TextView mContent;
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
                .load(course.getImage().getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_course)
                .into(mImage);
        TextView mTitle = (TextView)view.findViewById(R.id.tv_title);
        mTitle.setText(course.getTitle().trim());
        TextView mDate = (TextView)view.findViewById(R.id.tv_date);
        mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(course.getDate(), Utils.DATE_FORMAT_INPUT)));
        TextView mCost = (TextView)view.findViewById(R.id.tv_cost);
        mCost.setText(course.getCost().equals("0") ? "Costo: Gratis": "Costo: " + course.getCost());

        /*TextView mDiscount = (TextView)view.findViewById(R.id.tv_discount);
        if(course.getDiscount().equals("")){
            mDiscount.setText(String.format("%s %s ", "Descuento: ", course.getDiscount()));
        }else {
            mDiscount.setVisibility(View.GONE);
        }

        TextView mDiscountToDate = (TextView)view.findViewById(R.id.tv_discount_to_date);
        if(course.getDiscount().equals("")){
            mDiscountToDate.setText(course.getDiscountToDate());
        }else {
            mDiscountToDate.setVisibility(View.GONE);
        }*/

        mContent = (TextView)view.findViewById(R.id.tv_content);
        mContent.setText(course.getContent());
        getDescription();
    }

    private void getDescription(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        String urlCourse = API.COURSES + "/" + course.getId();
        Call<Course> courseCall = apiClient.getCourseDescription(urlCourse);
        courseCall.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                CharSequence content = Html.fromHtml(response.body().getContent());
                mContent.setText(content);
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.d(TAG, " SUCCESS >>> " + t.toString());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
