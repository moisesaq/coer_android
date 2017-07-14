package moises.com.appcoer.ui.home.reserve;
import android.view.View;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveRoomPresenter implements ReserveRoomContract.Presenter{

    private final ReserveRoomContract.View reserveRoomView;

    public ReserveRoomPresenter(ReserveRoomContract.View reserveRoomView){
        this.reserveRoomView = reserveRoomView;
        this.reserveRoomView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        reserveRoomView.setTitle();
    }

    @Override
    public void loadRooms(int hotelId) {
        getRooms(hotelId);
    }

    @Override
    public void loadDatesBusyOfRoom(Room room) {
        loadBusyDates(room);
    }

    @Override
    public void reserveRoom() {
        if(reserveRoomView.isAllDataValid())
            reserveRoom(reserveRoomView.prepareReservation());
    }

    private void getRooms(int hotelId){
        reserveRoomView.showProgress(View.VISIBLE);
        Call<List<Room>> listCall = RestApiAdapter.getInstance().startConnection()
                .getRoomList(hotelId, SessionManager.getInstance(reserveRoomView.getFragment().getActivity()).getUser().getApiToken());
        listCall.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(response.body() != null && response.body().size() > 0)
                    reserveRoomView.showRoomsAvailable(response.body());
                reserveRoomView.showProgress(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                reserveRoomView.showProgress(View.GONE);
            }
        });
    }

    private void loadBusyDates(Room room){
        reserveRoomView.showProgress(View.VISIBLE);
        Call<List<String>> listCall = RestApiAdapter.getInstance().startConnection()
                .getRoomBusyDate(room.getId(), SessionManager.getInstance(
                        reserveRoomView.getFragment().getActivity()).getUser().getApiToken());
        listCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    reserveRoomView.setUpTextDatesNoAvailable(response.body());
                }else{
                    reserveRoomView.showMessageError(R.string.message_something_went_wrong);
                }
                reserveRoomView.showProgress(View.GONE);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                reserveRoomView.showProgress(View.GONE);
                reserveRoomView.showMessageError(R.string.message_something_went_wrong);
            }
        });
    }

    private void reserveRoom(final Reservation reservation){
        reserveRoomView.showLoading(true);
        Call<Reservation> reservationCall = RestApiAdapter.getInstance().startConnection()
                .reserveRoom(reservation.getRoomId(), SessionManager.getInstance(
                        reserveRoomView.getFragment().getActivity()).getUser().getApiToken(),
                        reservation);
        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                reserveRoomView.showLoading(false);
                if(response.isSuccessful()){
                    reserveRoomView.showReservationSuccess();
                }else {
                    reserveRoomView.showMessageError(R.string.message_reservation_failed);
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                reserveRoomView.showLoading(false);
                reserveRoomView.showMessageError(R.string.message_something_went_wrong);
            }
        });
    }
}
