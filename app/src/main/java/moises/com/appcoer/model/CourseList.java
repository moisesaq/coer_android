package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseList extends BaseList{
    @SerializedName("data")
    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
