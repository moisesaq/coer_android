package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.CourseList;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.adapters.ReservationListAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListFragment extends BaseFragment implements ReservationListAdapter.CallBack{

    private static final String TAG = ReservationListFragment.class.getSimpleName();

    private View view;
    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private FloatingActionButton mAddReserve;
    private ReservationListAdapter mReservationListAdapter;

    public ReservationListFragment() {
        // Required empty public constructor
    }

    public static ReservationListFragment newInstance() {
        return new ReservationListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_base_list, container, false);
            setupView();
        }
        return view;
    }

    private void setupView(){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mReservationListAdapter = new ReservationListAdapter(new ArrayList<Reservation>(), this);
        mRecyclerView.setAdapter(mReservationListAdapter);
        mAddReserve = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        mAddReserve.setVisibility(View.VISIBLE);
        getReservations();
        //showUserGuide();
    }

    private void showComingSoon(){
        mLoadingView.hideLoading("Mis reservaciones\npróximamente", mRecyclerView, R.mipmap.working);
    }

    private void showUserGuide(){
        UserGuide.getInstance(GlobalManager.getActivityGlobal()).showStageWithView(UserGuide.StageGuide.STAGE_2, mAddReserve, new UserGuide.CallBack() {
            @Override
            public void onUserGuideOnClick() {
                Utils.showToastMessage("Próximamente");
            }
        });
    }

    private void getReservations(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<Reservation>> listCall = apiClient.getReservations(Session.getInstance().getUser().getApiToken());
        listCall.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                Log.d(TAG, " Success >>>> " + response.message() + "code " +response.code());
                if(response.body() != null && response.body().size() > 0){
                    mLoadingView.hideLoading("", mRecyclerView);
                    mReservationListAdapter.addItems(response.body());
                }else{
                    mLoadingView.hideLoading(getString(R.string.message_withot_reservation), mRecyclerView);
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                mLoadingView.hideLoading(getString(R.string.message_something_went_wrong), mRecyclerView);
            }
        });
    }

    @Override
    public void onReservationClick(Reservation reservation) {

    }
}
