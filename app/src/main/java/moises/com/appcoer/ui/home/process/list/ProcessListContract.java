package moises.com.appcoer.ui.home.process.list;

import java.util.List;

import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface ProcessListContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showMessageError(int stringId);

        void showProcesses(List<Process> processes);
    }

    interface Presenter extends BasePresenter{
    }
}
