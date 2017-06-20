package moises.com.appcoer.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.model.Reservation;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationViewHolder>{
    private List<Reservation> reservationList;
    private CallBack mCallBack;

    public ReservationListAdapter(CallBack callBack){
        this.reservationList = new ArrayList<>();
        mCallBack = callBack;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.mState.setText(reservation.getState());
        holder.mLodging.setText(reservation.getLodging());
        holder.mRoomDescription.setText(reservation.getRoomDescription());
        holder.mFullName.setText(String.format("%s %s", reservation.getName(), reservation.getLastName()));
        holder.mEmail.setText(reservation.getEmail());
    }

    @Override
    public int getItemCount() {
        if(reservationList != null)
            return reservationList.size();
        return 0;
    }

    public void addItems(List<Reservation> reservations){
        reservationList.addAll(reservations);
        notifyDataSetChanged();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mFullName, mLodging, mRoomDescription, mEmail, mState;
        public ReservationViewHolder(View view) {
            super(view);
            mState = (TextView)view.findViewById(R.id.tv_state);
            mLodging = (TextView)view.findViewById(R.id.tv_lodging);
            mRoomDescription = (TextView)view.findViewById(R.id.tv_room_description);
            mFullName = (TextView)view.findViewById(R.id.tv_full_name);
            mEmail = (TextView)view.findViewById(R.id.tv_email);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mCallBack != null)
                mCallBack.onReservationClick(reservationList.get(getAdapterPosition()));
        }
    }

    public interface CallBack{
        void onReservationClick(Reservation reservation);
    }
}
