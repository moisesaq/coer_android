package moises.com.appcoer.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Reservation;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {
    private List<Reservation> reservations = new ArrayList<>();
    private Callback callback;

    public ReservationsAdapter(Callback callback) {
        this.callback = callback;
    }

    @Override
    @NonNull
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations != null ? reservations.size() : 0;
    }

    public void addItems(List<Reservation> reservations) {
        this.reservations.addAll(reservations);
        notifyDataSetChanged();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_full_name)
        protected TextView tvFullName;
        @BindView(R.id.tv_lodging)
        protected TextView tvLodging;
        @BindView(R.id.tv_room_description)
        protected TextView tvRoomDescription;
        @BindView(R.id.tv_email)
        protected TextView tvEmail;
        @BindView(R.id.tv_state)
        protected TextView tvState;

        ReservationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void bind(Reservation reservation) {
            tvState.setText(reservation.getState());
            tvLodging.setText(reservation.getLodging());
            tvRoomDescription.setText(reservation.getRoomDescription());
            tvFullName.setText(String.format("%s %s", reservation.getName(), reservation.getLastName()));
            tvEmail.setText(reservation.getEmail());
        }

        @Override
        public void onClick(View view) {
            if (callback != null)
                callback.onReservationClick(reservations.get(getAdapterPosition()));
        }
    }

    public interface Callback {
        void onReservationClick(Reservation reservation);
    }
}
