package moises.com.appcoer.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.model.Process;
import moises.com.appcoer.ui.view.TextImageView;

public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.NewsViewHolder>{
    private List<Process> processList;
    private CallBack mCallBack;

    public ProcessListAdapter(CallBack callBack){
        this.processList = new ArrayList<>();
        mCallBack = callBack;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.mTitle.setText1(processList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(processList != null)
            return processList.size();
        return 0;
    }

    public void addItems(List<Process> processes){
        processList.addAll(processes);
        notifyDataSetChanged();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextImageView mTitle;
        public NewsViewHolder(View view) {
            super(view);
            mTitle = (TextImageView) view.findViewById(R.id.tiv_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mCallBack != null)
                mCallBack.onProcessClick(processList.get(getAdapterPosition()));
        }
    }

    public interface CallBack{
        void onProcessClick(Process process);
    }
}
