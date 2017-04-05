package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lodging extends BaseResponse implements Serializable{
    @SerializedName("title")
    private String title;
    @SerializedName("info")
    private String info;
    private String warning;
    @SerializedName("tarifa")
    private String rate;
    @SerializedName("tarifa_desde")
    private String rateFrom;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateFrom() {
        return rateFrom;
    }

    public void setRateFrom(String rateFrom) {
        this.rateFrom = rateFrom;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
