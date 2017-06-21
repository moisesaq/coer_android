package moises.com.appcoer.ui.home.payments;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.MethodPayment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsPresenter implements PaymentsContract.Presenter{

    private final PaymentsContract.View paymentsView;

    public PaymentsPresenter(PaymentsContract.View paymentsView){
        this.paymentsView = paymentsView;
        this.paymentsView.setPresenter(this);
    }

    @Override
    public void onFragmentStarted() {
        paymentsView.showLoading(true);
        getMethodPayments();
    }

    private void getMethodPayments(){
        Call<List<MethodPayment>> listCall = RestApiAdapter.getInstance()
                .startConnection().getMethodPayments();
        listCall.enqueue(new Callback<List<MethodPayment>>() {
            @Override
            public void onResponse(Call<List<MethodPayment>> call,
                                   Response<List<MethodPayment>> response) {
                if(response.isSuccessful() && response.body() != null)
                    success(response.body());
                else
                    failed(R.string.message_withot_method_payments);
            }

            @Override
            public void onFailure(Call<List<MethodPayment>> call, Throwable t) {
                failed(R.string.message_something_went_wrong);
            }
        });
    }

    private void success(List<MethodPayment> methodPayments){
        paymentsView.showLoading(false);
        paymentsView.showPayments(methodPayments);
    }

    private void failed(int stringId){
        paymentsView.showMessageError(stringId);
    }
}
