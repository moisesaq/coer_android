package moises.com.appcoer.ui.home.payments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.ui.adapters.MethodPaymentsAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.customviews.LoadingView;

public class PaymentsFragment extends BaseFragment implements PaymentsContract.View{
    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @BindView(R.id.loading_view) protected LoadingView loadingView;
    private View view;

    private MethodPaymentsAdapter methodPaymentsAdapter;
    private Unbinder unbinder;
    private PaymentsContract.Presenter paymentsPresenter;

    public static PaymentsFragment newInstance() {
        return new PaymentsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PaymentsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            setUp();
        }
        setTitle(getString(R.string.nav_method_payments), R.id.nav_method_payments);
        return view;
    }

    private void setUp(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        methodPaymentsAdapter = new MethodPaymentsAdapter(getContext());
        recyclerView.setAdapter(methodPaymentsAdapter);
        paymentsPresenter.onFragmentStarted();
    }

    /**
     * IMPLEMENTATION PAYMENTS CONTRACT VIEW
     **/
    @Override
    public void setPresenter(PaymentsContract.Presenter presenter) {
        if(presenter != null) this.paymentsPresenter = presenter;
        else throw new RuntimeException("Payments presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) loadingView.showLoading(recyclerView);
        else loadingView.hideLoading("", recyclerView);
    }

    @Override
    public void showMessageError(int stringId) {
        loadingView.hideLoading(getSafeString(stringId), recyclerView);
    }

    @Override
    public void showPayments(List<MethodPayment> methodPayments) {
        methodPaymentsAdapter.addItems(methodPayments);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
