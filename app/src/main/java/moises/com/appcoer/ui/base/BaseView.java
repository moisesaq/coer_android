package moises.com.appcoer.ui.base;

import android.support.v4.app.Fragment;

public interface BaseView<T> {
    void setPresenter(T presenter);
    Fragment getFragment();
}
