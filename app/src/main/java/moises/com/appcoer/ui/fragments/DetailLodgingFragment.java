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
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLodgingFragment extends Fragment {
    private static final String TAG = DetailLodgingFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "typeLodging";

    private int typeLodging;
    private TextView mContent;

    public DetailLodgingFragment() {
        // Required empty public constructor
    }

    public static DetailLodgingFragment newInstance(int typeLodging) {
        DetailLodgingFragment fragment = new DetailLodgingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typeLodging);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            typeLodging = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        ImageView mImage = (ImageView)view.findViewById(R.id.iv_news);
        /*Picasso.with(getContext())
                .load(news.getImage().getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_coer)
                .into(mImage);*/
        TextView mTitle = (TextView)view.findViewById(R.id.tv_title);
        TextView mDate = (TextView)view.findViewById(R.id.tv_date);
        //mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(news.getDate(), Utils.DATE_FORMAT_INPUT_2)));
        mContent = (TextView)view.findViewById(R.id.tv_content);


        //mContent.setText(news.getContent());
        getDescription();
    }

    private void getDescription(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        String urlNews = API.NEWS + "/";// + news.getId();
        Call<News> newsCall = apiClient.getNewsDescription(urlNews, Session.getInstance().getUser() == null ? null : Session.getInstance().getUser().getApiToken());
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                CharSequence content = Html.fromHtml(response.body().getContent());
                mContent.setText(content);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG, " SUCCESS >>> " + t.toString());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
