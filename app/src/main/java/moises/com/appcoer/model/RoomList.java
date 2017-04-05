package moises.com.appcoer.model;

import com.google.gson.Gson;

import java.util.List;

public class RoomList {
    private List<Room> rooms;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
