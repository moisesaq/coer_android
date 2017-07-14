package moises.com.appcoer.ui.home.reservations;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsPresenter implements ReservationsContract.Presenter{

    private final ReservationsContract.View reservationsView;

    public ReservationsPresenter(ReservationsContract.View reservationsView){
        this.reservationsView = reservationsView;
        reservationsView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        reservationsView.showUserGuide();
        loadReservations();
    }

    private void loadReservations(){
        reservationsView.showLoading(true);
        Call<List<Reservation>> listCall = RestApiAdapter.getInstance().startConnection()
                .getReservations(SessionManager.getInstance(
                        reservationsView.getFragment().getActivity()).getUser().getApiToken());
        listCall.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if(response.body() != null && response.body().size() > 0)
                    success(response.body());
                else
                    failed(R.string.message_withot_reservation);
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                failed(R.string.message_something_went_wrong);
            }
        });
    }

    private void success(List<Reservation> reservations){
        reservationsView.showLoading(false);
        reservationsView.showReservations(reservations);
    }

    private void failed(int stringId){
        reservationsView.showError(stringId);
    }
}
