package moises.com.appcoer.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.ui.adapters.MethodPaymentsAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MethodPaymentsFragment extends BaseFragment{

    private static final String TAG = MethodPaymentsFragment.class.getSimpleName();
    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private MethodPaymentsAdapter mMethodPaymentsAdapter;

    public MethodPaymentsFragment() {
    }

    public static MethodPaymentsFragment newInstance() {
        return new MethodPaymentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            setupView();
        }
        setTitle(getString(R.string.nav_method_payments), R.id.nav_method_payments);
        return view;
    }

    private void setupView(){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMethodPaymentsAdapter = new MethodPaymentsAdapter(getContext(), new ArrayList<MethodPayment>());
        mRecyclerView.setAdapter(mMethodPaymentsAdapter);
        mLoadingView.showLoading(mRecyclerView);
        getMethodPayments();
    }

    private void getMethodPayments(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<MethodPayment>> listCall = apiClient.getMethodPayments();
        listCall.enqueue(new Callback<List<MethodPayment>>() {
            @Override
            public void onResponse(Call<List<MethodPayment>> call, Response<List<MethodPayment>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().size() > 0){
                    mLoadingView.hideLoading("", mRecyclerView);
                    mMethodPaymentsAdapter.addItems(response.body());
                }else{
                    mLoadingView.hideLoading(getSafeString(R.string.message_withot_method_payments), mRecyclerView);
                }
            }

            @Override
            public void onFailure(Call<List<MethodPayment>> call, Throwable t) {
                mLoadingView.hideLoading(getSafeString(R.string.message_something_went_wrong), mRecyclerView);
            }
        });
    }
}
