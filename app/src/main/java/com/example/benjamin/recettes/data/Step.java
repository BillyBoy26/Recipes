package com.example.benjamin.recettes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Step implements Serializable,HasName,Parcelable {
    private Long id;
    private String name;
    private Integer rank;
    private Long idRec;

    public Step() {
    }


    public Step(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Long getIdRec() {
        return idRec;
    }

    public void setIdRec(Long idRec) {
        this.idRec = idRec;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    //constructor used for parcel
    public Step(Parcel parcel){
        id = parcel.readLong();
        name = parcel.readString();
        rank = parcel.readInt();
        idRec = parcel.readLong();
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(rank);
        dest.writeLong(idRec);
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
