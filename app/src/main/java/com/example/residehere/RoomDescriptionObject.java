package com.example.residehere;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomDescriptionObject implements Parcelable {

    private String name;
    private String seater;
    private String ac;
    private String ab;
    private String price;

    public RoomDescriptionObject(String name, String seater, String ac, String ab, String price) {
        this.name = name;
        this.seater = seater;
        this.ac = ac;
        this.ab = ab;
        this.price = price;
    }

    protected RoomDescriptionObject(Parcel in) {
        name = in.readString();
        seater = in.readString();
        ac = in.readString();
        ab = in.readString();
        price = in.readString();
    }

    public static final Creator<RoomDescriptionObject> CREATOR = new Creator<RoomDescriptionObject>() {
        @Override
        public RoomDescriptionObject createFromParcel(Parcel in) {
            return new RoomDescriptionObject(in);
        }

        @Override
        public RoomDescriptionObject[] newArray(int size) {
            return new RoomDescriptionObject[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeater() {
        return seater;
    }

    public void setSeater(String seater) {
        this.seater = seater;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getAb() {
        return ab;
    }

    public void setAb(String ab) {
        this.ab = ab;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(seater);
        dest.writeString(ac);
        dest.writeString(ab);
        dest.writeString(price);
    }
}
