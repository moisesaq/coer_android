package moises.com.appcoer.ui.home.process.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessFragment extends BaseFragment implements ProcessContract.View{
    private static final String ARG_PARAM1 = "process";

    protected @BindView(R.id.loading_view) LoadingView mLoadingView;
    protected @BindView(R.id.cv_content) CardView mCardViewContent;
    protected @BindView(R.id.content_detail) LinearLayout mContentDetail;
    protected @BindView(R.id.tv_title) TextView mTitle;
    protected @BindView(R.id.b_download_document) Button bDownloadDocument;
    protected @BindView(R.id.wv_content) WebView mContent;

    private Process process;
    private Unbinder unbinder;
    private ProcessContract.Presenter processPresenter;

    public static ProcessFragment newInstance(Process process) {
        ProcessFragment fragment = new ProcessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, process);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ProcessPresenter(this);
        if (getArguments() != null)
            process = (Process) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processPresenter.onFragmentStarted();
        processPresenter.updateProcess(process.getId());
    }

    @OnClick(R.id.b_download_document)
    public void onClick(View view){
        if (view.getId() == bDownloadDocument.getId()){
            showDocument(process.getUrl());
        }
    }

    private void showDocument(String url){
        GlobalManager.getCustomTabsIntent(getActivity()).launchUrl(getActivity(), Uri.parse("https://docs.google.com/viewerng/viewer?url=" + url));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * IMPLEMENTATION PROCESS CONTRACT VIEW
     **/
    @Override
    public void setPresenter(ProcessContract.Presenter presenter) {
        if(presenter != null) this.processPresenter = presenter;
        else throw new RuntimeException("Process presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) mLoadingView.showLoading(mContentDetail);
        else mLoadingView.hideLoading("", mContentDetail);
    }

    @Override
    public void showMessageError(int stringId) {
        mLoadingView.hideLoading(getSafeString(stringId), mContentDetail);
    }

    @Override
    public void showProcessUpdated(Process process) {
        showDetail(process);
    }

    private void showDetail(Process process){
        mTitle.setText(process.getTitle().trim());

        if(process.getUrl() != null && !process.getUrl().isEmpty())
            bDownloadDocument.setVisibility(View.VISIBLE);

        if(process.getContent() != null && !process.getContent().isEmpty()){
            mCardViewContent.setVisibility(View.VISIBLE);
            mContent.loadData(process.getContent(), "text/html; charset=utf-8","UTF-8");
        }
    }
}
