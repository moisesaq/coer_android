package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.adapters.ProcessListAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessListFragment extends BaseFragment implements ProcessListAdapter.CallBack{

    private static final String TAG = ProcessListFragment.class.getSimpleName();

    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private ProcessListAdapter processListAdapter;

    public ProcessListFragment() {
    }

    public static ProcessListFragment newInstance() {
        return new ProcessListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            setupView();
        }
        setTitle(getString(R.string.nav_processes), R.id.nav_processes);
        return view;
    }

    private void setupView(){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        processListAdapter = new ProcessListAdapter(new ArrayList<Process>(), this);
        mRecyclerView.setAdapter(processListAdapter);
        mLoadingView.showLoading(mRecyclerView);
        loadListProcess();
    }

    private void loadListProcess(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<Process>> listCall = apiClient.getProcesses();
        listCall.enqueue(new Callback<List<Process>>() {
            @Override
            public void onResponse(Call<List<Process>> call, Response<List<Process>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0){
                    Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                    mLoadingView.hideLoading("", mRecyclerView);
                    processListAdapter.addItems(response.body());
                }else{
                    mLoadingView.hideLoading(getSafeString(R.string.message_without_processes), mRecyclerView);
                }
            }

            @Override
            public void onFailure(Call<List<Process>> call, Throwable t) {
                mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mRecyclerView);
            }
        });
    }

    @Override
    public void onProcessClick(Process process) {
        replaceFragment(ProcessFragment.newInstance(process), true);
    }
}
