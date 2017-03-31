package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Course extends BaseResponse implements Serializable{
    @SerializedName("afiche")
    private String poster;
    @SerializedName("costo")
    private String cost;
    @SerializedName("descuento")
    private String discount;
    @SerializedName("descuento_hasta")
    private String discountToDate;
    @SerializedName("referencias")
    private String references;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountToDate() {
        return discountToDate;
    }

    public void setDiscountToDate(String discountToDate) {
        this.discountToDate = discountToDate;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
