package moises.com.appcoer.ui.home.hotel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Hotel;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.home.reserve.ReserveRoomFragment;
import moises.com.appcoer.ui.customviews.LoadingView;

public class HotelFragment extends BaseFragment implements HotelContract.View{
    private static final String ARG_PARAM1 = "hotelId";

    private View view;
    @BindView(R.id.loading_view) protected LoadingView loadingView;
    @BindView(R.id.content_detail) protected LinearLayout contentDetail;
    @BindView(R.id.iv_image_hotel) protected ImageView mImage;
    @BindView(R.id.tv_title) protected TextView tvTitle;
    @BindView(R.id.tv_rate_from) protected TextView mRateFrom;
    @BindView(R.id.tv_content) protected TextView tvContent;
    @BindView(R.id.tv_info) protected TextView tvInfo;
    @BindView(R.id.tv_warning) protected TextView tvWarning;
    @BindView(R.id.webView) protected WebView webView;
    @BindView(R.id.btn_reserve) protected Button btnReserve;

    private int hotelId;
    private Hotel hotel;
    private HotelContract.Presenter hotelPresenter;
    private Unbinder unbinder;

    public static HotelFragment newInstance(int idLodging) {
        HotelFragment fragment = new HotelFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idLodging);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HotelPresenter(this);
        if (getArguments() != null)
            hotelId = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_hotel, container, false);
            unbinder = ButterKnife.bind(this, view);

        }
        setTitle(getString(R.string.app_name));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hotelPresenter.onFragmentStarted();
        hotelPresenter.getHotelDescription(hotelId);
    }

    @OnClick(R.id.btn_reserve)
    public void onClick(View view) {
        if(hotel != null)
            replaceFragment(ReserveRoomFragment.newInstance(hotel.getId()), true);
    }

    /**
     * IMPLEMENTATION HOTEL CONTRACT VIEW
     * */

    @Override
    public void setPresenter(HotelContract.Presenter presenter) {
        if (presenter != null) this.hotelPresenter = presenter;
        else throw new RuntimeException("Hotel presenter can not be null");
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading(boolean show) {
        if(show) loadingView.showLoading(contentDetail);
        else loadingView.hideLoading("", contentDetail);
    }

    @Override
    public void showHotelDetail(Hotel hotel) {
        this.hotel = hotel;
        showHotelImage(hotel);
        showDetail(hotel);
    }

    @Override
    public void showMessageError(int stringId) {
        loadingView.hideLoading("Error", contentDetail);
    }

    private void showHotelImage(Hotel hotel){
        Picasso.with(getContext())
                .load(hotel.getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.mipmap.image_load)
                .into(mImage);
    }

    private void showDetail(Hotel hotel){
        tvTitle.setText(hotel.getTitle());
        tvContent.setText((hotel.getContent()));
        tvInfo.setText(hotel.getInfo());
        tvWarning.setText(hotel.getWarning());
        try{
            mRateFrom.setText(String.format("%s %s", getString(R.string.rate_from), Utils.getCustomDate(Utils.parseStringToDate(hotel.getRateFrom(), Utils.DATE_FORMAT_INPUT))));
        }catch (Exception e){
            e.printStackTrace();
        }
        webView.loadData(hotel.getRate() , "text/html; charset=utf-8","UTF-8");
    }
}
