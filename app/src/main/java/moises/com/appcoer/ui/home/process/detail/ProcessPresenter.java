package moises.com.appcoer.ui.home.process.detail;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.Process;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessPresenter implements ProcessContract.Presenter{

    private final ProcessContract.View processView;

    public ProcessPresenter(ProcessContract.View processView){
        this.processView = processView;
        this.processView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        processView.showLoading(true);
    }

    @Override
    public void updateProcess(int processId){
        final Call<Process> processCall = RestApiAdapter.getInstance().startConnection().getProcessDetail(processId);
        processCall.enqueue(new Callback<Process>() {
            @Override
            public void onResponse(Call<Process> call, Response<Process> response) {
                if(response.isSuccessful())
                    success(response.body());
                else
                    failed(R.string.message_something_went_wrong);
            }

            @Override
            public void onFailure(Call<Process> call, Throwable t) {
                failed(R.string.message_something_went_wrong);
            }
        });
    }

    private void success(Process process){
        processView.showLoading(false);
        processView.showProcessUpdated(process);
    }

    private void failed(int stringId){
        processView.showMessageError(stringId);
    }
}
