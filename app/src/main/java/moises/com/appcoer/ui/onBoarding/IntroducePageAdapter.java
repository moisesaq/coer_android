package moises.com.appcoer.ui.onBoarding;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.ui.base.IntroduceItem;

public class IntroducePageAdapter extends PagerAdapter {
    private View view;

    private List<IntroduceItem> items;
    private int colorText;

    IntroducePageAdapter(List<IntroduceItem> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        view = inflater.inflate(R.layout.pager_layout, container, false);
        setViews(position);
        container.addView(view);
        return view;
    }

    private void setViews(int position){
        ImageView imageOnBoarding = view.findViewById(R.id.imageOnBoarding);
        TextView title = view.findViewById(R.id.titleOnBoarding);
        TextView subtitle = view.findViewById(R.id.subtitleOnBoarding);
        IntroduceItem item = items.get(position);
        imageOnBoarding.setImageResource(item.getImage());
        title.setText(item.getTitle());
        subtitle.setText(item.getSubtitle());
        if(colorText > 0){
            title.setTextColor(colorText);
            subtitle.setTextColor(colorText);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout)object);
    }

    void setColorText(int color){
        this.colorText = color;
    }
}
