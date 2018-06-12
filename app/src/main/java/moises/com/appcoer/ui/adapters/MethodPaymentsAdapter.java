package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.MethodPayment;
import moises.com.appcoer.tools.Utils;

public class MethodPaymentsAdapter extends CoerAdapter<MethodPayment> {

    private List<MethodPayment> payments = new ArrayList<>();

    public MethodPaymentsAdapter(Context context) {
        super(context);
    }

    @Override
    public void addItems(List<MethodPayment> items) {
        payments.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentHolder(inflateView(parent, R.layout.method_payment_item));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MethodPayment methodPayment = payments.get(position);
        ((PaymentHolder) holder).bind(methodPayment);
    }

    @Override
    public int getItemCount() {
        return payments != null ? payments.size() : 0;
    }

    class PaymentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.riv_method_payment)
        protected ImageView imageView;
        @BindView(R.id.tv_title)
        protected TextView tvTitle;
        @BindView(R.id.tv_content)
        protected TextView tvContent;

        PaymentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(MethodPayment payment) {
            loadImage(payment.getImage(), imageView);
            tvTitle.setText(payment.getTitle().trim());
            if (payment.getContent().isEmpty())
                return;
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(Utils.fromHtml(payment.getContent()));
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
