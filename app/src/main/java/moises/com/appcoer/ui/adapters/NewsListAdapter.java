package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{

    private Context mContext;
    private List<News> newsList;
    private CallBack mCallBack;

    public NewsListAdapter(Context context, List<News> newsList, CallBack callBack){
        this.mContext = context;
        this.newsList = newsList;
        mCallBack = callBack;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        Picasso.with(mContext)
                .load(news.getImage().getThumbnail())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_coer)
                .into(holder.mImage);
        holder.mTitle.setText(news.getTitle().trim());
        holder.mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(news.getDate(), Utils.DATE_FORMAT_INPUT_2)));
        holder.mContent.setText(news.getContent().replace("\n", " ").replace("\r", "").replace("&nbsp;",""));
    }

    @Override
    public int getItemCount() {
        if(newsList != null)
            return newsList.size();
        return 0;
    }

    public void addItems(List<News> news){
        newsList.addAll(news);
        notifyDataSetChanged();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.riv_news) RoundedImageView mImage;
        @BindView(R.id.tv_title) TextView mTitle;
        @BindView(R.id.tv_date) TextView mDate;
        @BindView(R.id.tv_content) TextView mContent;

        public NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mCallBack != null)
                mCallBack.onNewsClick(newsList.get(getAdapterPosition()));
        }
    }

    public interface CallBack{
        void onNewsClick(News news);
    }
}
