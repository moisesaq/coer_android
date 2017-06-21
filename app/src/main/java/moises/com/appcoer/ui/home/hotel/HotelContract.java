package moises.com.appcoer.ui.home.hotel;

import moises.com.appcoer.model.Hotel;
import moises.com.appcoer.ui.base.BasePresenter;
import moises.com.appcoer.ui.base.BaseView;

public interface HotelContract {

    interface View extends BaseView<Presenter>{
        void showLoading(boolean show);

        void showHotelDetail(Hotel hotel);

        void showMessageError(int stringId);
    }

    interface Presenter extends BasePresenter{
        void getHotelDescription(int hotelId);
    }
}
