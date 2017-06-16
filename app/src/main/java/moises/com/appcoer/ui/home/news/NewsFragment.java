package moises.com.appcoer.ui.home.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

public class NewsFragment extends Fragment {
    private static final String TAG = NewsFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "news";

    private News news;
    private WebView mContent;

    public NewsFragment() {
    }

    public static NewsFragment newInstance(News news) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, news);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            news = (News)getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        ImageView mImage = (ImageView)view.findViewById(R.id.iv_news);
        Picasso.with(getContext())
                .load(news.getImage().getSlide())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_coer)
                .into(mImage);
        TextView mTitle = (TextView)view.findViewById(R.id.tv_title);
        mTitle.setText(news.getTitle().trim());
        TextView mDate = (TextView)view.findViewById(R.id.tv_date);
        mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(news.getDate(), Utils.DATE_FORMAT_INPUT_2)));
        mContent = (WebView) view.findViewById(R.id.wv_content);
        showContent(news.getContent());
        getDescription();
    }

    private void getDescription(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        String urlNews = API.NEWS + "/" + news.getId();
        Call<News> newsCall = apiClient.getNewsDescription(urlNews, Session.getInstance().getUser() == null ? null : Session.getInstance().getUser().getApiToken());
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    showContent(response.body().getContent());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
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
