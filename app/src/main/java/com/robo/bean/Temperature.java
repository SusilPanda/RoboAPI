package com.robo.bean;

public class Temperature {

   public String topTemp;
   public String btmTemp;
   public String topHumid;
   public String btmHumid;
   public String smokeSensor;
   public String sound;

    public Temperature () {

    }

    public Temperature(String topTemp, String btmTemp, String topHumid, String btmHumid, String smokeSensor, String sound) {
        this.topTemp = topTemp;
        this.btmTemp = btmTemp;
        this.topHumid = topHumid;
        this.btmHumid = btmHumid;
        this.smokeSensor = smokeSensor;
        this.sound = sound;
    }

    public String getTopTemp() {
        return topTemp;
    }

    public void setTopTemp(String topTemp) {
        this.topTemp = topTemp;
    }

    public String getBtmTemp() {
        return btmTemp;
    }

    public void setBtmTemp(String btmTemp) {
        this.btmTemp = btmTemp;
    }

    public String getTopHumid() {
        return topHumid;
    }

    public void setTopHumid(String topHumid) {
        this.topHumid = topHumid;
    }

    public String getBtmHumid() {
        return btmHumid;
    }

    public void setBtmHumid(String btmHumid) {
        this.btmHumid = btmHumid;
    }

    public String getSmokeSensor() {
        return smokeSensor;
    }

    public void setSmokeSensor(String smokeSensor) {
        this.smokeSensor = smokeSensor;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
