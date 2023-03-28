package com.example.residehere;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PgObject implements Parcelable {

    private String accountVerify;
    private String address;
    private String city;
    private String food;
    private String laundry;
    private String geohash;
    private String lat;
    private String lng;
    private String managerContact;
    private String managerEmail;
    private String managerName;
    private String ownerContact;
    private String paymentStatus;
    private String pgName;
    private String pgStatus;
    private String state;

    public PgObject(String accountVerify, String address, String city, String food, String laundry, String geohash, String lat, String lng, String managerContact, String managerEmail, String managerName, String ownerContact, String paymentStatus, String pgName, String pgStatus, String state) {
        this.accountVerify = accountVerify;
        this.address = address;
        this.city = city;
        this.food = food;
        this.laundry = laundry;
        this.geohash = geohash;
        this.lat = lat;
        this.lng = lng;
        this.managerContact = managerContact;
        this.managerEmail = managerEmail;
        this.managerName = managerName;
        this.ownerContact = ownerContact;
        this.paymentStatus = paymentStatus;
        this.pgName = pgName;
        this.pgStatus = pgStatus;
        this.state = state;
    }

    protected PgObject(Parcel in) {
        accountVerify = in.readString();
        address = in.readString();
        city = in.readString();
        food = in.readString();
        laundry = in.readString();
        geohash = in.readString();
        lat = in.readString();
        lng = in.readString();
        managerContact = in.readString();
        managerEmail = in.readString();
        managerName = in.readString();
        ownerContact = in.readString();
        paymentStatus = in.readString();
        pgName = in.readString();
        pgStatus = in.readString();
        state = in.readString();
    }

    public static final Creator<PgObject> CREATOR = new Creator<PgObject>() {
        @Override
        public PgObject createFromParcel(Parcel in) {
            return new PgObject(in);
        }

        @Override
        public PgObject[] newArray(int size) {
            return new PgObject[size];
        }
    };

    public String getAccountVerify() {
        return accountVerify;
    }

    public void setAccountVerify(String accountVerify) {
        this.accountVerify = accountVerify;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPgStatus() {
        return pgStatus;
    }

    public void setPgStatus(String pgStatus) {
        this.pgStatus = pgStatus;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getLaundry() {
        return laundry;
    }

    public void setLaundry(String laundry) {
        this.laundry = laundry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountVerify);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(food);
        dest.writeString(laundry);
        dest.writeString(geohash);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(managerContact);
        dest.writeString(managerEmail);
        dest.writeString(managerName);
        dest.writeString(ownerContact);
        dest.writeString(paymentStatus);
        dest.writeString(pgName);
        dest.writeString(pgStatus);
        dest.writeString(state);
    }
}
