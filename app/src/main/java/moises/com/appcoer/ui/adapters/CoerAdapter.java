package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import moises.com.appcoer.R;

public abstract class CoerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;

    CoerAdapter(Context context) {
        this.context = context;
    }

    View inflateView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    public abstract void addItems(List<T> items);

    void loadImage(String urlImage, ImageView imageView) {
        Glide.with(context)
                .load(urlImage)
                .apply(createOptions())
                .into(imageView);
    }

    private RequestOptions createOptions() {
        return new RequestOptions().error(R.mipmap.image_load).placeholder(R.mipmap.image_load);
    }

    String getFormHtml(String text) {
        return text.replace("\n", " ")
                .replace("\r", "")
                .replace("&nbsp;", "");
    }
}
