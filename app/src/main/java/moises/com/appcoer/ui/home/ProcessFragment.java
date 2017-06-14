package moises.com.appcoer.ui.home;

import android.net.Uri;
import android.os.Bundle;
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

public class ProcessFragment extends BaseFragment {
    private static final String TAG = ProcessFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "process";

    private Process process;
    protected @BindView(R.id.loading_view) LoadingView mLoadingView;
    protected @BindView(R.id.cv_content) CardView mCardViewContent;
    protected @BindView(R.id.content_detail) LinearLayout mContentDetail;
    protected @BindView(R.id.tv_title) TextView mTitle;
    protected @BindView(R.id.b_download_document) Button bDownloadDocument;
    protected @BindView(R.id.wv_content) WebView mContent;

    public ProcessFragment() {
    }

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
        if (getArguments() != null)
            process = (Process) getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        ButterKnife.bind(this, view);
        loadData();
        return view;
    }

    private void loadData(){
        mLoadingView.showLoading(mContentDetail);
        mTitle.setText(process.getTitle().trim());
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        final Call<Process> processCall = apiClient.getProcessDetail(process.getId());
        processCall.enqueue(new Callback<Process>() {
            @Override
            public void onResponse(Call<Process> call, Response<Process> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "SUCCESS >>> " + response.body().toString());
                    mLoadingView.hideLoading("", mContentDetail);
                    process = response.body();
                    if(process.getUrl() != null && !process.getUrl().isEmpty())
                        bDownloadDocument.setVisibility(View.VISIBLE);

                    if(process.getContent() != null && !process.getContent().isEmpty()){
                        mCardViewContent.setVisibility(View.VISIBLE);
                        mContent.loadData(response.body().getContent(), "text/html; charset=utf-8","UTF-8");
                    }

                }else{
                    mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mContentDetail);
                }
            }

            @Override
            public void onFailure(Call<Process> call, Throwable t) {
                mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mContentDetail);
            }
        });
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
    public void onDetach() {
        super.onDetach();
    }
}
