package com.example.my_future.Models;

public class User {
    // Данные пользователя
    private String email, password, avatar, nickname, weight, growth, gender, target;
    // Объёмы пользователя
    private String waist, neck, chest, biceps, forearm, hip, shin;

    public User() {
    }

    // Данные пользователя
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Объёмы пользователя

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getBiceps() {
        return biceps;
    }

    public void setBiceps(String biceps) {
        this.biceps = biceps;
    }

    public String getForearm() {
        return forearm;
    }

    public void setForearm(String forearm) {
        this.forearm = forearm;
    }

    public String getHip() {
        return hip;
    }

    public void setHip(String hip) {
        this.hip = hip;
    }

    public String getShin() {
        return shin;
    }

    public void setShin(String shin) {
        this.shin = shin;
    }
}
