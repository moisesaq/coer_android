package moises.com.appcoer.ui.main;

import moises.com.appcoer.model.Bill;

public interface MainContract {

    interface View {
        void startOnBoardingActivity();

        void showBill(Bill bill);

        void showBillFailed();
    }

    interface Presenter {
        void onActivityCreated();

        void getBill();

        void onActivityDestroyed();
    }
}
