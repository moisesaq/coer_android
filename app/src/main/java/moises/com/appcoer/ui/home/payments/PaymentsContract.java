package moises.com.appcoer.ui.home.payments;

import java.util.List;

import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public class PaymentsContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showMessageError(int stringId);

        void showPayments(List<MethodPayment> methodPayments);
    }

    interface Presenter extends BasePresenter{
    }
}
