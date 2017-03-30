package moises.com.appcoer.model;


import com.google.gson.Gson;

import java.io.Serializable;

public class ImageCoer implements Serializable{
    private String image;
    private String thumbnail;
    private String slide;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSlide() {
        return slide;
    }

    public void setSlide(String slide) {
        this.slide = slide;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
