package moises.com.appcoer.ui.home.hotel;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.session.SessionManager;
import moises.com.appcoer.model.Hotel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelPresenter implements HotelContract.Presenter{
    private final HotelContract.View hotelView;

    public HotelPresenter(HotelContract.View hotelView){
        this.hotelView = hotelView;
        this.hotelView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        hotelView.showLoading(true);
    }

    @Override
    public void getHotelDescription(int hotelId){
        Call<List<Hotel>> lodgingListCall = RestApiAdapter.getInstance()
                .startConnection().getLodgingList(SessionManager.getInstance(hotelView.getFragment().getActivity()).getUser().getApiToken());
        lodgingListCall.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if(response.body() != null && response.body().size() > 0){
                    hotelView.showLoading(false);
                    success(response.body(), hotelId);
                }else{
                    failed(R.string.message_something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                failed(R.string.message_something_went_wrong);
            }
        });
    }

    private void success(List<Hotel> hotelList, int hotelId){
        for (Hotel hotel : hotelList){
            if(hotel.getId() == hotelId){
                hotelView.showHotelDetail(hotel); break;
            }
        }
    }

    private void failed(int stringId){
        hotelView.showMessageError(stringId);
    }
}
