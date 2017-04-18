package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import moises.com.appcoer.R;
import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.tools.Utils;

public class MethodPaymentsAdapter extends RecyclerView.Adapter<MethodPaymentsAdapter.CourseViewHolder>{

    private Context mContext;
    private List<MethodPayment> methodPaymentList;

    public MethodPaymentsAdapter(Context context, List<MethodPayment> methodPayments){
        this.mContext = context;
        this.methodPaymentList = methodPayments;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.method_payment_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        MethodPayment methodPayment = methodPaymentList.get(position);
        Picasso.with(mContext)
                .load(methodPayment.getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_course)
                .into(holder.mImage);
        holder.mTitle.setText(methodPayment.getTitle().trim());
        if(!methodPayment.getContent().isEmpty()){
            holder.mContent.setVisibility(View.VISIBLE);
            holder.mContent.setText(Utils.fromHtml(methodPayment.getContent()));
            holder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @Override
    public int getItemCount() {
        if(methodPaymentList != null)
            return methodPaymentList.size();
        return 0;
    }

    public void addItems(List<MethodPayment> methodPayments){
        methodPaymentList.addAll(methodPayments);
        notifyDataSetChanged();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mTitle, mContent;
        public CourseViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.riv_method_payment);
            mTitle = (TextView)view.findViewById(R.id.tv_title);
            mContent = (TextView)view.findViewById(R.id.tv_content);
        }
    }
}
