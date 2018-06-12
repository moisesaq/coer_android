package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.News;
import moises.com.appcoer.tools.Utils;

public class NewsAdapter extends CoerAdapter<News> {
    private List<News> news = new ArrayList<>();
    private Callback callback;

    public NewsAdapter(Context context, Callback callBack) {
        super(context);
        this.callback = callBack;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(inflateView(parent, R.layout.news_item));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News news1 = news.get(position);
        ((NewsViewHolder)holder).bind(news1);
    }

    @Override
    public int getItemCount() {
        return news != null ? news.size() : 0;
    }

    public void addItems(List<News> news) {
        this.news.addAll(news);
        notifyDataSetChanged();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.riv_news)
        RoundedImageView imageView;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        TextView tvContent;

        NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void bind(News news) {
            loadImage(news.getImage().getThumbnail(), imageView);
            tvTitle.setText(news.getTitle().trim());
            tvDate.setText(Utils.getCustomDate(Utils.parseStringToDate(news.getDate(), Utils.DATE_FORMAT_INPUT_2)));
            tvContent.setText(getFormHtml(news.getContent()));
        }

        @Override
        public void onClick(View view) {
            if (callback != null)
                callback.onNewsClick(news.get(getAdapterPosition()));
        }
    }

    public interface Callback {
        void onNewsClick(News news);
    }
}
