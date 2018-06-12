package moises.com.appcoer.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.customviews.TextImageView;

public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessViewHolder> {
    private List<Process> processes = new ArrayList<>();
    private Callback callback;

    public ProcessListAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_item, parent, false);
        return new ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
        Process process = processes.get(position);
        holder.bind(process);
    }

    @Override
    public int getItemCount() {
        return processes != null ? processes.size() : 0;
    }

    public void addItems(List<Process> processes) {
        this.processes.addAll(processes);
        notifyDataSetChanged();
    }

    public class ProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tiv_title)
        TextImageView tivTitle;

        ProcessViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        private void bind(Process process) {
            tivTitle.setText1(process.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (callback != null)
                callback.onProcessClick(processes.get(getAdapterPosition()));
        }
    }

    public interface Callback {
        void onProcessClick(Process process);
    }
}
