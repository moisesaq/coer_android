package moises.com.appcoer.ui.home.process.list;

import android.util.Log;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Process;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessListPresenter implements ProcessListContract.Presenter{

    private ProcessListContract.View processListView;

    public ProcessListPresenter(ProcessListContract.View processListView){
        this.processListView = processListView;
        this.processListView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        processListView.showLoading(true);
        loadListProcess();
    }

    private void loadListProcess(){
        Call<List<Process>> listCall = RestApiAdapter.getInstance()
                .startConnection().getProcesses();
        listCall.enqueue(new Callback<List<Process>>() {
            @Override
            public void onResponse(Call<List<Process>> call, Response<List<Process>> response) {
                if(response.isSuccessful() && response.body() != null
                        && response.body().size() > 0){
                    success(response.body());
                }else{
                    failed(R.string.message_without_processes);
                }
            }

            @Override
            public void onFailure(Call<List<Process>> call, Throwable t) {
                failed(R.string.message_something_went_wrong);
            }
        });
    }

    private void success(List<Process> processes){
        processListView.showLoading(false);
        processListView.showProcesses(processes);
    }

    private void failed(int stringId){
        processListView.showMessageError(stringId);
    }
}
