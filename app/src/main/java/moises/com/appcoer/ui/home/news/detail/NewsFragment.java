package moises.com.appcoer.ui.home.news.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;

public class NewsFragment extends BaseFragment implements NewsContract.View {
    private static final String ARG_PARAM1 = "news";

    @BindView(R.id.iv_image_news)
    protected ImageView imageView;
    @BindView(R.id.tv_title)
    protected TextView tvTitle;
    @BindView(R.id.tv_date)
    protected TextView tvDate;
    @BindView(R.id.wv_content)
    protected WebView webView;

    private News news;
    private NewsContract.Presenter newsPresenter;
    private Unbinder unbinder;

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
        new NewsPresenter(this);
        if (getArguments() != null)
            news = (News) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsPresenter.onFragmentStarted();
    }

    /**
     * IMPLEMENTATION NEWS CONTRACT VIEW
     **/
    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        if (presenter != null) this.newsPresenter = presenter;
        else throw new RuntimeException("News presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showNews() {
        loadImage(news.getImage().getSlide(), imageView);
        showNewsDetail(news);
        showNewsContent(news.getContent());
        newsPresenter.updateNews(news.getId());
    }

    @Override
    public void showNewsUpdated(News news) {
        showNewsContent(news.getContent());
    }

    private void showNewsDetail(News news) {
        tvTitle.setText(news.getTitle().trim());
        tvDate.setText(Utils.getCustomDate(Utils.parseStringToDate(news.getDate(), Utils.DATE_FORMAT_INPUT_2)));
    }

    private void showNewsContent(String content) {
        webView.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
