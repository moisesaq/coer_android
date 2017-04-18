package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bill implements Serializable{
    @SerializedName("urlbilling")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
