package moises.com.appcoer.ui.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moises.com.appcoer.R;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.tools.Utils;

public class MethodPaymentsAdapter extends RecyclerView.Adapter<MethodPaymentsAdapter.CourseViewHolder>{

    private Context mContext;
    private List<Course> courseList;
    private CallBack mCallBack;

    public MethodPaymentsAdapter(Context context, List<Course> courses, CallBack callBack){
        this.mContext = context;
        this.courseList = courses;
        mCallBack = callBack;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        Picasso.with(mContext)
                .load(course.getImage().getImage())
                .placeholder(R.mipmap.image_load)
                .error(R.drawable.example_course)
                .into(holder.mImage);
        holder.mTitle.setText(course.getTitle().trim());
        holder.mCost.setText(course.getCost().equals("0") ? "Gratis": "Costo: " + course.getCost());
        holder.mDate.setText(Utils.getCustomDate(Utils.parseStringToDate(course.getDate(), Utils.DATE_FORMAT_INPUT)));
        /*if(course.getDiscount().equals("")){
            holder.mDiscount.setText(String.format("%s %s ", "Descuento: ", course.getDiscount()));
        }else {
            holder.mDiscount.setVisibility(View.GONE);
        }

        if(course.getDiscount().equals("")){
            holder.mDiscountToDate.setText(course.getDiscountToDate());
        }else {
            holder.mDiscountToDate.setVisibility(View.GONE);
        }*/


        holder.mContent.setText(course.getContent().replace("\n", " ").replace("\r", "").replace("&nbsp;",""));
    }

    @Override
    public int getItemCount() {
        if(courseList != null)
            return courseList.size();
        return 0;
    }

    public void addItems(List<Course> courses){
        courseList.addAll(courses);
        notifyDataSetChanged();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImage;
        TextView mTitle, mCost, mDate, mContent, mDiscount, mDiscountToDate;
        public CourseViewHolder(View view) {
            super(view);
            mImage = (ImageView)view.findViewById(R.id.iv_course);
            mTitle = (TextView)view.findViewById(R.id.tv_title);
            mDate = (TextView)view.findViewById(R.id.tv_date);
            mContent = (TextView)view.findViewById(R.id.tv_content);
            mCost = (TextView)view.findViewById(R.id.tv_cost);
            mDiscount = (TextView)view.findViewById(R.id.tv_discount);
            mDiscountToDate = (TextView)view.findViewById(R.id.tv_discount_to_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mCallBack != null)
                mCallBack.onCourseClick(courseList.get(getAdapterPosition()));
        }
    }

    public interface CallBack{
        void onCourseClick(Course course);
    }
}
