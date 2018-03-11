package com.example.yc.midterm.DataStruct;

import java.io.Serializable;


public class people implements Serializable{
    private int imageId;//头像
    private String name;//名
    private String sex;//性别
    private String birthday;//生卒
    private String zi;//字
    private String birthPlace;//籍贯
    private String birthPlace_now;//当今的籍贯所在处
    private String power;//势力

    public people(int imageId, String name, String sex, String birthday, String zi, String birthPlace, String birthPlace_now, String power){
        this.imageId = imageId;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.zi = zi;
        this.birthPlace = birthPlace;
        this.birthPlace_now = birthPlace_now;
        this.power = power;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getZi() {
        return zi;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getBirthPlace_now() {
        return birthPlace_now;
    }

    public String getPower() {
        return power;
    }

}
