package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.model.Room;

public class SpinnerRoomsAdapter extends ArrayAdapter<Room> {

    private Context mContext;

    public SpinnerRoomsAdapter(Context context, List<Room> roomList) {
        super(context, R.layout.room_spinner_item, roomList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    private View getCustomView(int position, View view) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.room_spinner_item, null);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.tv_text_room);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Room room = getItem(position);
        holder.textView.setText(room.getRoomText());
        return view;
    }

    @Nullable
    @Override
    public Room getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void addAll(Room... items) {
        super.addAll(items);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        Room room = getItem(position);
        if (room != null)
            return room.getId();
        return 0;
    }

    public class ViewHolder {
        TextView textView;
    }
}
