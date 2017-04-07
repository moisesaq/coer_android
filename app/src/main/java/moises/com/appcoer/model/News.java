package moises.com.appcoer.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import moises.com.appcoer.model.base.BaseResponse;

public class News extends BaseResponse implements Serializable{
    @SerializedName("id_user")
    private int idUser;
    @SerializedName("publico")
    private int mPublic;
    @SerializedName("destacado")
    private int outstanding;
    @SerializedName("archivo")
    private String file;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
