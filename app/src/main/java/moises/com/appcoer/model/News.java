package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable{
    private int id;
    @SerializedName("id_user")
    private int idUser;
    @SerializedName("titulo")
    private String title;
    private String alias;
    @SerializedName("contenido")
    private String content;
    @SerializedName("fecha")
    private String date;
    @SerializedName("publico")
    private int mPublic;
    @SerializedName("destacado")
    private int outstanding;
    private int published;
    @SerializedName("image")
    private ImageCoer image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getmPublic() {
        return mPublic;
    }

    public void setmPublic(int mPublic) {
        this.mPublic = mPublic;
    }

    public int getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(int outstanding) {
        this.outstanding = outstanding;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public ImageCoer getImage() {
        return image;
    }

    public void setImage(ImageCoer image) {
        this.image = image;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
