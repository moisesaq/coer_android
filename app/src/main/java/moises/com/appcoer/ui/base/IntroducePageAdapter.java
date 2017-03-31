package moises.com.appcoer.ui.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.tools.Utils;

public class IntroducePageAdapter extends PagerAdapter {
    private View view;
    private List<IntroduceItem> listItems;
    private int colorText;

    public IntroducePageAdapter(List<IntroduceItem> listItems){
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pager_layout, container, false);
        ImageView imageOnBoarding = (ImageView)view.findViewById(R.id.imageOnBoarding);
        TextView title = (TextView)view.findViewById(R.id.titleOnBoarding);
        TextView subtitle = (TextView)view.findViewById(R.id.subtitleOnBoarding);
        IntroduceItem item = listItems.get(position);
        imageOnBoarding.setImageResource(item.getImage());
        title.setText(item.getTitle());
        subtitle.setText(item.getSubtitle());
        if(colorText > 0){
            title.setTextColor(Utils.getColor(colorText));
            subtitle.setTextColor(Utils.getColor(colorText));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }

    public IntroduceItem getItem(int position){
        if(listItems != null)
            return listItems.get(position);
        return null;
    }

    public void setColorText(int color){
        this.colorText = color;
    }
}
