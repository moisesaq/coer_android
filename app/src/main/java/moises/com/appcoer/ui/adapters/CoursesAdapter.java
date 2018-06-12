package moises.com.appcoer.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import moises.com.appcoer.R;
import moises.com.appcoer.model.Course;
import moises.com.appcoer.tools.Utils;

public class CoursesAdapter extends CoerAdapter<Course> {

    private List<Course> courses = new ArrayList<>();
    private Callback callback;

    public CoursesAdapter(Context context, Callback callback) {
        super(context);
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(inflateView(parent, R.layout.course_item));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Course course = courses.get(position);
        ((CourseViewHolder) holder).bind(course);
    }

    @Override
    public void addItems(List<Course> items) {
        courses.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.riv_course)
        protected RoundedImageView roundedImageView;
        @BindView(R.id.tv_title)
        protected TextView tvTitle;
        @BindView(R.id.tv_cost)
        protected TextView tvCost;
        @BindView(R.id.tv_date)
        protected TextView tvDate;
        @BindView(R.id.tv_content)
        protected TextView tvContent;
        @BindView(R.id.tv_discount)
        protected TextView tvDiscount;
        @BindView(R.id.tv_discount_to_date)
        protected TextView tvDiscountToDate;
        @BindView(R.id.fl_discount)
        protected FrameLayout tvContentDiscount;

        @BindString(R.string.free)
        String free;

        CourseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bind(Course course) {
            loadImage(course.getImage().getThumbnail(), roundedImageView);
            tvTitle.setText(course.getTitle().trim());
            tvCost.setText(course.getCost().equals("0") ? free : "$ " + course.getCost());
            tvDate.setText(Utils.getCustomDate(Utils.parseStringToDate(course.getDate(), Utils.DATE_FORMAT_INPUT)));

            tvContentDiscount.setVisibility(hasDiscount(course) ? View.VISIBLE : View.GONE);
            tvDiscount.setText(course.getDiscount() != null ? course.getDiscount() : "");
            tvDiscountToDate.setText(course.getDiscountToDate() != null ? course.getDiscountToDate() : "");
            tvContent.setText(getFormHtml(course.getReferences()));
        }

        private boolean hasDiscount(Course course) {
            return course.getDiscount() != null || course.getDiscountToDate() != null;
        }

        @Override
        public void onClick(View view) {
            if (callback != null)
                callback.onCourseClick(courses.get(getAdapterPosition()));
        }
    }

    public interface Callback {
        void onCourseClick(Course course);
    }
}
