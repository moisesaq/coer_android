package moises.com.appcoer.ui.home.reserve;

import java.util.List;

import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface ReserveRoomContract {

    interface View extends BaseView<Presenter>{
        void setTitle();

        void showLoading(boolean show);

        void showProgress(int visibility);

        void showRoomsAvailable(List<Room> rooms);

        Reservation prepareReservation();

        boolean isAllDataValid();

        void showReservationSuccess();

        void showMessageError(int stringId);

        void setUpTextDatesNoAvailable(List<String> listDateNoAvailable);
    }

    interface Presenter extends BasePresenter{
        void loadRooms(int hotelId);

        void loadDatesBusyOfRoom(Room room);

        void reserveRoom();
    }
}
