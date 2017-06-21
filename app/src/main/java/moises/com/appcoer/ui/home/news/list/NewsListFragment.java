package moises.com.appcoer.ui.home.news.list;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.EndlessRecyclerOnScrollListener;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.adapters.NewsListAdapter;
import moises.com.appcoer.ui.home.news.detail.NewsFragment;
import moises.com.appcoer.ui.view.LoadingView;

public class NewsListFragment extends BaseFragment implements NewsListAdapter.CallBack, NewsListContract.View{

    private static final String ARG_PARAM1 = "outstanding";

    private View view;
    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.loading_view) protected LoadingView mLoadingView;
    @BindView(R.id.progressBar) protected ProgressBar mProgressBar;

    private NewsListAdapter mNewsListAdapter;
    private boolean outstanding;
    private NewsListContract.Presenter newsListPresenter;
    private Unbinder unbinder;

    public static NewsListFragment newInstance(boolean outstanding) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PARAM1, outstanding);
        NewsListFragment newsListFragment =  new NewsListFragment();
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NewsListPresenter(this);
        if(getArguments() != null)
            outstanding = getArguments().getBoolean(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            setUp();
        }
        setTitle(outstanding ? getString(R.string.last_news) : getString(R.string.nav_news), R.id.nav_news);
        return view;
    }

    private void setUp(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNewsListAdapter = new NewsListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mNewsListAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                newsListPresenter.loadNews(currentPage);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO remove or move to onCreateView
        newsListPresenter.onFragmentStarted();
    }

    @Override
    public void onNewsClick(News news) {
        replaceFragment(NewsFragment.newInstance(news), true);
    }

    /**
     * IMPLEMENTATION NEWS LIST CONTRACT VIEW
     **/

    @Override
    public void setPresenter(NewsListContract.Presenter presenter) {
        if(presenter != null) this.newsListPresenter = presenter;
        else throw new RuntimeException("News list presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) mLoadingView.showLoading(mRecyclerView);
        else mLoadingView.hideLoading("", mRecyclerView);
    }

    @Override
    public void showProgress(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    @Override
    public void showMessageError(int stringId) {
        mLoadingView.hideLoading(getSafeString(stringId), mRecyclerView);
    }

    @Override
    public void showNews(List<News> news) {
        mNewsListAdapter.addItems(news);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
