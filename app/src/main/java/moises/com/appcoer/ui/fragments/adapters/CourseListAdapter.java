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
import moises.com.appcoer.model.News;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>{

    private Context mContext;
    private List<Course> courseList;
    private CallBack mCallBack;

    public CourseListAdapter(Context context, List<Course> courses, CallBack callBack){
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
        holder.mTitle.setText(course.getTitle());
        holder.mDate.setText(course.getDate());
        holder.mContent.setText(course.getContent());
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
        TextView mTitle, mDate, mContent;
        public CourseViewHolder(View view) {
            super(view);
            mImage = (ImageView)view.findViewById(R.id.iv_course);
            mTitle = (TextView)view.findViewById(R.id.tv_title);
            mDate = (TextView)view.findViewById(R.id.tv_date);
            mContent = (TextView)view.findViewById(R.id.tv_content);
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
