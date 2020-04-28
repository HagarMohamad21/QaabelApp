package com.Qaabel.org.model.entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Parcelable {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("desciption")
    @Expose
    private String desciption;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("oldpassword")
    @Expose
    private String oldPassword;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("devicetoken")
    @Expose
    private String devicetoken;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("surname")
    @Expose
    private String surname;


    @SerializedName("birthdayDate")
    @Expose
    private String birthdayDate;


    @SerializedName("agePrivate")
    @Expose
    private  Boolean agePrivate;

    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("location")
    @Expose
    private LocationModel location;
    private String freind_status;

    private String tempUserNameOrPhone;


    public UserModel(Location location) {
        this.location = new LocationModel(location.getLatitude(), location.getLongitude());
    }

    public UserModel(String name, String username, String email, String phone, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public UserModel(String user_id) {
        this.user_id = user_id;
    }

    protected UserModel(Parcel in) {
        username = in.readString();
        code = in.readString();
        _id = in.readString();
        desciption = in.readString();
        user_id = in.readString();
        email = in.readString();
        phone = in.readString();
        password = in.readString();
        oldPassword = in.readString();
        token = in.readString();
        devicetoken = in.readString();
        name = in.readString();
        title = in.readString();
        sex = in.readString();
        surname = in.readString();
        birthdayDate = in.readString();
        job = in.readString();
        image = in.readString();
        freind_status = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public UserModel() {
    }

    public String getDescription() {
        return desciption;
    }

    public void setDescription(String description) {
        this.desciption = description;
    }

    public UserModel(String email, String password) {
//        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
        if (true) {
            this.email = email;
        } else {
            this.username = email;
        }

        this.password = password;
    }

    public UserModel(String date, int status) {
        this.birthdayDate = date;
        this.sex = String.valueOf(status);
    }

    public UserModel(String name, String username, String phone, String password) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public UserModel(String username, String email, String phone, String name, String title, String birthdayDate, String job, String image) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.title = title;
        this.birthdayDate = birthdayDate;
        this.job = job;
        this.image = image;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFreind_status() {
        return freind_status;
    }

    public void setFreind_status(String freind_status) {
        this.freind_status = freind_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(code);
        dest.writeString(_id);
        dest.writeString(desciption);
        dest.writeString(user_id);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(oldPassword);
        dest.writeString(token);
        dest.writeString(devicetoken);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(sex);
        dest.writeString(surname);
        dest.writeString(birthdayDate);
        dest.writeString(job);
        dest.writeString(image);
        dest.writeString(freind_status);
    }

    public boolean isAgePrivate() {
        return agePrivate;
    }

    public void setAgePrivate(boolean agePrivate) {
        this.agePrivate = agePrivate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTempUserNameOrPhone() {
        return tempUserNameOrPhone;
    }

    public void setTempUserNameOrPhone(String tempUserNameOrPhone) {
        this.tempUserNameOrPhone = tempUserNameOrPhone;
    }
}
