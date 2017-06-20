package moises.com.appcoer.ui.home.reservations;

import java.util.List;

import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface ReservationsContract {

    interface View extends BaseView<Presenter>{
        void showUserGuide();

        void showLoading(boolean show);

        void showError(int stringId);

        void showReservations(List<Reservation> reservations);
    }

    interface Presenter extends BasePresenter{
    }
}
