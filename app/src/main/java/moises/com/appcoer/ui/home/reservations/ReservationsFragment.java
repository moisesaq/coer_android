package moises.com.appcoer.ui.home.reservations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.global.GlobalManager;
import moises.com.appcoer.global.UserGuide;
import moises.com.appcoer.model.Reservation;
import moises.com.appcoer.ui.adapters.ReservationListAdapter;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.dialogs.ReserveDetailDialog;
import moises.com.appcoer.ui.home.reserve.ReserveRoomFragment;
import moises.com.appcoer.ui.view.LoadingView;

public class ReservationsFragment extends BaseFragment implements ReservationListAdapter.CallBack, ReservationsContract.View{

    @BindView(R.id.recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.loading_view) protected LoadingView mLoadingView;
    @BindView(R.id.fam_reserve) protected FloatingActionMenu famReserve;
    @BindView(R.id.fab_reserve_timbues) protected FloatingActionButton fabTimbues;
    @BindView(R.id.fab_reserve_parana) protected FloatingActionButton fabParana;

    private ReservationListAdapter mReservationListAdapter;
    private ReservationsContract.Presenter reservationsPresenter;
    private Unbinder unbinder;

    public static ReservationsFragment newInstance() {
        return new ReservationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ReservationsPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUp();
        return view;
    }

    private void setUp(){
        setTitle(getString(R.string.nav_reservations), R.id.nav_reserves);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //TODO Change to Dependency injection Dagger2
        mReservationListAdapter = new ReservationListAdapter(this);
        mRecyclerView.setAdapter(mReservationListAdapter);

        famReserve.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationsPresenter.onFragmentStarted();
    }

    @Override
    public void onReservationClick(Reservation reservation) {
        ReserveDetailDialog.newInstance(reservation).show(getFragmentManager(), ReserveDetailDialog.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * IMPLEMENTATION RESERVATIONS CONTRACT VIEW
     **/
    @Override
    public void showUserGuide(){
        UserGuide.getInstance(GlobalManager.getActivityGlobal()).showStageWithView(UserGuide.StageGuide.STAGE_2, famReserve.getMenuIconView(), new UserGuide.CallBack() {
            @Override
            public void onUserGuideOnClick() {
                famReserve.open(true);
            }
        });
    }

    @Override
    public void showLoading(boolean show) {
        if(show) mLoadingView.showLoading(mRecyclerView);
        else mLoadingView.hideLoading("", mRecyclerView);
    }

    @Override
    public void showError(int stringId) {
        mLoadingView.hideLoading(getSafeString(stringId), mRecyclerView);
    }

    @Override
    public void showReservations(List<Reservation> reservations) {
        mReservationListAdapter.addItems(reservations);
    }

    @OnClick({ R.id.fab_reserve_timbues, R.id.fab_reserve_parana})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_reserve_timbues:
                replaceFragment(ReserveRoomFragment.newInstance(ReserveRoomFragment.ID_TIMBUES), true);
                famReserve.close(true);
                break;
            case R.id.fab_reserve_parana:
                replaceFragment(ReserveRoomFragment.newInstance(ReserveRoomFragment.ID_PARANA), true);
                famReserve.close(true);
                break;
        }
    }

    @Override
    public void setPresenter(ReservationsContract.Presenter presenter) {
        if(presenter != null) this.reservationsPresenter = presenter;
        else throw new RuntimeException("Reservations Presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
