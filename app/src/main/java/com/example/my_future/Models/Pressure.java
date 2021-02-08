package com.example.my_future.Models;

public class Pressure {
    String namePressure, sis, dis;

    public Pressure(String namePressure, String sis, String dis) {
        this.namePressure = namePressure;
        this.sis = sis;
        this.dis = dis;
    }

    public String getNamePressure() {
        return namePressure;
    }

    public void setNamePressure(String namePressure) {
        this.namePressure = namePressure;
    }

    public String getSis() {
        return sis;
    }

    public void setSis(String sis) {
        this.sis = sis;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }
}
