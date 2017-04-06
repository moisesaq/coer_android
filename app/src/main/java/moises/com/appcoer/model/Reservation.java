package moises.com.appcoer.model;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Serializable{
    private int id;
    private int mp;
    @SerializedName("id_habitacion")
    private String idRoom;
    @SerializedName("nombres")
    private String name;
    @SerializedName("apellidos")
    private String lastName;
    private String email;
    @SerializedName("telefono")
    private String phone;
    @SerializedName("cantidad")
    private int amountPeople;
    @SerializedName("detalle")
    private String detail;
    @SerializedName("motivo")
    private String reason;
    @SerializedName("estado")
    private String state;
    @SerializedName("pagado")
    private int payed;
    @SerializedName("fechapago")
    private String datePay;
    @SerializedName("metodopago")
    private String methodPay;
    @SerializedName("monto")
    private String amount;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("habitacion")
    private String roomDescription;
    @SerializedName("alojamiento")
    private String lodging;

    /*@SerializedName("fechas")
    @Nullable
    private List<String> dates;*/

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*@Nullable
    public List<String> getDates() {
        return dates;
    }

    public void setDates(@Nullable List<String> dates) {
        this.dates = dates;
    }*/

    public int getAmountPeople() {
        return amountPeople;
    }

    public void setAmountPeople(int amountPeople) {
        this.amountPeople = amountPeople;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public String getDatePay() {
        return datePay;
    }

    public void setDatePay(String datePay) {
        this.datePay = datePay;
    }

    public String getMethodPay() {
        return methodPay;
    }

    public void setMethodPay(String methodPay) {
        this.methodPay = methodPay;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getLodging() {
        return lodging;
    }

    public void setLodging(String lodging) {
        this.lodging = lodging;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
