package moises.com.appcoer.ui.home.process.detail;

import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface ProcessContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showMessageError(int stringId);

        void showProcessUpdated(Process process);
    }

    interface Presenter extends BasePresenter{
        void updateProcess(int processId);
    }
}
