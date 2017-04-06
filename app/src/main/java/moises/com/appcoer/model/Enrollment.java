package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import moises.com.appcoer.model.base.BaseResponse;

public class Enrollment extends BaseResponse implements Serializable{
    @SerializedName("descripcion")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
