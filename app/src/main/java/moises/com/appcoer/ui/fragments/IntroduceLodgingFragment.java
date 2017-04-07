package moises.com.appcoer.ui.fragments;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Lodging;
import moises.com.appcoer.ui.base.BaseFragment;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroduceLodgingFragment extends BaseFragment{
    private static final String TAG = IntroduceLodgingFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "idLodging";

    private View view;
    private int idLodging;
    private Lodging mLodging;
    private LinearLayout mContentDetail;
    private LoadingView mLoadingView;
    private ImageView mImage;
    private TextView mTitle, mRate, mRateFrom, mContent, mInfo, mWarning;

    public IntroduceLodgingFragment() {
        // Required empty public constructor
    }

    public static IntroduceLodgingFragment newInstance(int idLodging) {
        IntroduceLodgingFragment fragment = new IntroduceLodgingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idLodging);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            idLodging = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_introduce_lodging, container, false);
            setupView(view);
        }
        setTitle(getString(R.string.app_name));
        return view;
    }

    private void setupView(View view){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mContentDetail = (LinearLayout)view.findViewById(R.id.content_detail);
        mImage = (ImageView)view.findViewById(R.id.iv_lodging);
        mTitle = (TextView)view.findViewById(R.id.tv_title);
        mRate = (TextView)view.findViewById(R.id.tv_rate);
        mRateFrom = (TextView)view.findViewById(R.id.tv_rate_from);
        mContent = (TextView)view.findViewById(R.id.tv_content);
        mInfo = (TextView)view.findViewById(R.id.tv_info);
        mWarning = (TextView)view.findViewById(R.id.tv_warning);
        LinearLayout mReserve = (LinearLayout) view.findViewById(R.id.ly_reserve);
        mReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(ReserveRoomFragment.newInstance(mLodging.getId()), true);
            }
        });
        getDescriptionLodging();
    }

    private void getDescriptionLodging(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<List<Lodging>> lodgingListCall = apiClient.getLodgingList(Session.getInstance().getUser().getApiToken());
        lodgingListCall.enqueue(new Callback<List<Lodging>>() {
            @Override
            public void onResponse(Call<List<Lodging>> call, Response<List<Lodging>> response) {
                //Log.d(TAG, " SUCCESS >>> " + response.body().toString());
                if(response.body() != null && response.body().size() > 0){
                    for (Lodging lodging: response.body()){
                        if(lodging.getId() == idLodging)
                            mLodging = lodging;
                    }
                    showDetail();
                }else{
                    mLoadingView.hideLoading(getString(R.string.message_something_went_wrong), mContentDetail);
                }
            }

            @Override
            public void onFailure(Call<List<Lodging>> call, Throwable t) {
                mLoadingView.hideLoading(getString(R.string.message_something_went_wrong), mContentDetail);
            }
        });
    }

    private void showDetail(){
        if(mLodging == null){
            mLoadingView.hideLoading("Error", mContentDetail);
            return;
        }

        Picasso.with(getContext())
                .load(mLodging.getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.mipmap.image_load)
                .into(mImage);
        mLoadingView.hideLoading("", mContentDetail);
        mTitle.setText(mLodging.getTitle());
        CharSequence text = Html.fromHtml(mLodging.getRate());
        mRate.setText(text);
        mRateFrom.setText((mLodging.getRateFrom()));
        mContent.setText((mLodging.getContent()));
        mInfo.setText(mLodging.getInfo());
        mWarning.setText(mLodging.getWarning());
    }
}
