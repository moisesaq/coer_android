package moises.com.appcoer.ui.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.model.News;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{

    private List<News> newsList;
    private CallBack mCallBack;

    public NewsListAdapter(List<News> newsList, CallBack callBack){
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
        holder.mTitle.setText(news.getTitle());
        holder.mDate.setText(news.getDate());
        holder.mContent.setText(news.getContent());
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
        ImageView mImage;
        TextView mTitle, mDate, mContent;
        public NewsViewHolder(View view) {
            super(view);
            mImage = (ImageView)view.findViewById(R.id.iv_image);
            mTitle = (TextView)view.findViewById(R.id.tv_title);
            mDate = (TextView)view.findViewById(R.id.tv_date);
            mContent = (TextView)view.findViewById(R.id.tv_content);
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
