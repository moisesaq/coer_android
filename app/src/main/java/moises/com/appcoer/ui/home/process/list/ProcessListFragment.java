package moises.com.appcoer.ui.home.process.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.adapters.ProcessListAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.home.process.detail.ProcessFragment;
import moises.com.appcoer.ui.customviews.LoadingView;

public class ProcessListFragment extends BaseFragment implements ProcessListAdapter.CallBack, ProcessListContract.View{

    private View view;
    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @BindView(R.id.loading_view) protected LoadingView loadingView;

    private ProcessListAdapter processListAdapter;
    private Unbinder unbinder;
    private ProcessListContract.Presenter processListPresenter;

    public static ProcessListFragment newInstance() {
        return new ProcessListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ProcessListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            setUp();
        }
        setTitle(getString(R.string.nav_processes), R.id.nav_processes);
        return view;
    }

    private void setUp(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        processListAdapter = new ProcessListAdapter(this);
        recyclerView.setAdapter(processListAdapter);
        processListPresenter.onFragmentStarted();
    }



    @Override
    public void onProcessClick(Process process) {
        replaceFragment(ProcessFragment.newInstance(process), true);
    }

    /**
     * IMPLEMENTATION PROCESS LIST CONTRACT VIEW
     **/
    @Override
    public void setPresenter(ProcessListContract.Presenter presenter) {
        if (presenter != null) this.processListPresenter = presenter;
        else throw new RuntimeException("Process list presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) loadingView.showLoading(recyclerView);
        else loadingView.hideLoading("", recyclerView);
    }

    @Override
    public void showMessageError(int stringId) {
        loadingView.hideLoading(getSafeString(stringId), recyclerView);
    }

    @Override
    public void showProcesses(List<Process> processes) {
        processListAdapter.addItems(processes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
