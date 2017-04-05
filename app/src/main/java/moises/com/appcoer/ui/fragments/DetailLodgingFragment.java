package moises.com.appcoer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import moises.com.appcoer.R;
import moises.com.appcoer.api.ApiClient;
import moises.com.appcoer.api.RestApiAdapter;
import moises.com.appcoer.global.Session;
import moises.com.appcoer.model.Lodging;
import moises.com.appcoer.model.LodgingList;
import moises.com.appcoer.tools.Utils;
import moises.com.appcoer.ui.view.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class DetailLodgingFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = DetailLodgingFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "idLodging";

    private int idLodging;
    private Lodging mLodging;
    private LinearLayout mContentDetail;
    private LoadingView mLoadingView;
    private ImageView mImage;
    private TextView mTitle, mRate, mRateFrom, mContent, mWarning;
    private FloatingTextButton mFloatingTextButton;

    public DetailLodgingFragment() {
        // Required empty public constructor
    }

    public static DetailLodgingFragment newInstance(int idLodging) {
        DetailLodgingFragment fragment = new DetailLodgingFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail_lodging, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        mLoadingView = (LoadingView)view.findViewById(R.id.loading_view);
        mContentDetail = (LinearLayout)view.findViewById(R.id.content_detail);
        mImage = (ImageView)view.findViewById(R.id.iv_news);
        mTitle = (TextView)view.findViewById(R.id.tv_title);
        mRate = (TextView)view.findViewById(R.id.tv_rate);
        mRateFrom = (TextView)view.findViewById(R.id.tv_rate_from);
        mContent = (TextView)view.findViewById(R.id.tv_content);
        mWarning = (TextView)view.findViewById(R.id.tv_warning);
        mFloatingTextButton = (FloatingTextButton)view.findViewById(R.id.ftb_reserve);
        mFloatingTextButton.setOnClickListener(this);
        getDescriptionLodging();
    }

    private void getDescriptionLodging(){
        ApiClient apiClient = RestApiAdapter.getInstance().startConnection();
        Call<LodgingList> lodgingListCall = apiClient.getLodgingList(Session.getInstance().getUser().getApiToken());
        lodgingListCall.enqueue(new Callback<LodgingList>() {
            @Override
            public void onResponse(Call<LodgingList> call, Response<LodgingList> response) {
                Log.d(TAG, " SUCCESS >>> " + response.body().getLodgings().toString());
                if(response.body() != null && response.body().getLodgings() != null && response.body().getLodgings().size() > 0){
                    for (Lodging lodging: response.body().getLodgings()){
                        if(lodging.getId() == idLodging)
                            mLodging = lodging;
                    }
                    showDetail();
                }else{
                    mLoadingView.hideLoading("Error", mContentDetail);
                }
            }

            @Override
            public void onFailure(Call<LodgingList> call, Throwable t) {
                Log.d(TAG, " SUCCESS >>> " + t.toString());
            }
        });
    }

    private void showDetail(){
        if(mLodging == null){
            mLoadingView.hideLoading("Error", mContentDetail);
            return;
        }
        /*Picasso.with(getContext())
                .load(news.getImage().getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_coer)
                .into(mImage);*/
        mLoadingView.hideLoading("", mContentDetail);
        mTitle.setText(mLodging.getTitle());
        mRate.setText(mLodging.getRate());
        mRateFrom.setText((mLodging.getRateFrom()));
        mContent.setText((mLodging.getContent()));
        mWarning.setText(mLodging.getWarning());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mFloatingTextButton.getId()){
            Utils.showToastMessage("Reserve");
        }
    }
}
