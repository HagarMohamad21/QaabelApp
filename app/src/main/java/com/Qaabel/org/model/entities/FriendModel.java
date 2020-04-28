package com.Qaabel.org.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FriendModel implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("desciption")
    @Expose
    private String desciption;
    @SerializedName("location")
    @Expose
    private LocationModel location;
    @SerializedName("dist")
    @Expose
    private DistanceModel dist;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("birthdayDate")
    @Expose
    private String birthdayDate;
    @SerializedName("agePrivate")
    @Expose
    private Boolean agePrivate;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("isflashed")
    @Expose
    private Boolean isflashed;
    @SerializedName("isfriend")
    @Expose
    private Boolean isfriend;
    @SerializedName("isflashedyou")
    @Expose
    private Boolean isflashedyou;

    @SerializedName("chat")
    @Expose
    private String chat;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
    protected FriendModel(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            sex = null;
        } else {
            sex = in.readInt();
        }
        birthdayDate = in.readString();
        byte tmpAgePrivate = in.readByte();
        agePrivate = tmpAgePrivate == 0 ? null : tmpAgePrivate == 1;
        job = in.readString();
        company = in.readString();
        image = in.readString();
        name = in.readString();
        username = in.readString();
        phone = in.readString();
        email = in.readString();
        byte tmpIsflashed = in.readByte();
        isflashed = tmpIsflashed == 0 ? null : tmpIsflashed == 1;
        byte tmpIsfriend = in.readByte();
        isfriend = tmpIsfriend == 0 ? null : tmpIsfriend == 1;
        byte tmpIsflashedyou = in.readByte();
        isflashedyou = tmpIsflashedyou == 0 ? null : tmpIsflashedyou == 1;
    }

    public static final Creator<FriendModel> CREATOR = new Creator<FriendModel>() {
        @Override
        public FriendModel createFromParcel(Parcel in) {
            return new FriendModel(in);
        }

        @Override
        public FriendModel[] newArray(int size) {
            return new FriendModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public Boolean getAgePrivate() {
        return agePrivate;
    }

    public void setAgePrivate(Boolean agePrivate) {
        this.agePrivate = agePrivate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public LocationModel getLocation()
    {
        return location;
    }

    public void setLocation(LocationModel location)
    {
        this.location = location;
    }

    public DistanceModel getDist()
    {
        return dist;
    }
    public void setDist(DistanceModel dist)
    {
        this.dist = dist;
    }
    public Boolean getIsflashed() {
        return isflashed;
    }

    public void setIsflashed(Boolean isflashed) {
        this.isflashed = isflashed;
    }

    public Boolean getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Boolean isfriend) {
        this.isfriend = isfriend;
    }

    public Boolean getIsflashedyou() {
        return isflashedyou;
    }

    public void setIsflashedyou(Boolean isflashedyou) {
        this.isflashedyou = isflashedyou;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (sex == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sex);
        }
        dest.writeString(birthdayDate);
        dest.writeByte((byte) (agePrivate == null ? 0 : agePrivate ? 1 : 2));
        dest.writeString(job);
        dest.writeString(company);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeByte((byte) (isflashed == null ? 0 : isflashed ? 1 : 2));
        dest.writeByte((byte) (isfriend == null ? 0 : isfriend ? 1 : 2));
        dest.writeByte((byte) (isflashedyou == null ? 0 : isflashedyou ? 1 : 2));
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
