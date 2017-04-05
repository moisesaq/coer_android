package moises.com.appcoer.model;

import com.google.gson.Gson;

import java.util.List;

public class LodgingList {
    private List<Lodging> lodgings;

    public List<Lodging> getLodgings() {
        return lodgings;
    }

    public void setLodgings(List<Lodging> lodgings) {
        this.lodgings = lodgings;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
