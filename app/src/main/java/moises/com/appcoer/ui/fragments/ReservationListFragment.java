package moises.com.appcoer.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.adapters.ReservationListAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListFragment extends BaseFragment implements ReservationListAdapter.CallBack, View.OnClickListener{

    private static final String TAG = ReservationListFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LoadingView mLoadingView;
    private FloatingActionMenu famReserve;
    private ReservationListAdapter mReservationListAdapter;

    public ReservationListFragment() {
    }

    public static ReservationListFragment newInstance() {
        return new ReservationListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
        setupView(view);
        setTitle(getString(R.string.nav_reservations));
        return view;
    }

    private void setupView(View view){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mReservationListAdapter = new ReservationListAdapter(new ArrayList<Reservation>(), this);
        mRecyclerView.setAdapter(mReservationListAdapter);
        famReserve = (FloatingActionMenu) view.findViewById(R.id.fam_reserve);
        famReserve.setVisibility(View.VISIBLE);
        FloatingActionButton fabTimbues = (FloatingActionButton)view.findViewById(R.id.fab_reserve_timbues);
        fabTimbues.setOnClickListener(this);
        FloatingActionButton fabParana = (FloatingActionButton)view.findViewById(R.id.fab_reserve_parana);
        fabParana.setOnClickListener(this);
        getReservations();
        //showUserGuide();
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

    private void showUserGuide(){
        UserGuide.getInstance(GlobalManager.getActivityGlobal()).showStageWithView(UserGuide.StageGuide.STAGE_2, famReserve, new UserGuide.CallBack() {
            @Override
            public void onUserGuideOnClick() {
                Utils.showToastMessage("Próximamente");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_reserve_timbues:
                replaceFragment(ReserveRoomFragment.newInstance(1), true);
                famReserve.close(true);
                break;
            case R.id.fab_reserve_parana:
                replaceFragment(ReserveRoomFragment.newInstance(2), true);
                famReserve.close(true);
                break;
        }
    }
}
