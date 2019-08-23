package com.eelection;

public class SavedLocationModel {

    public String lat,lng,landmark,comp_add,nickname,inst_name,instid;
    int id;

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

    public String getLandmark() {
        return landmark;
    }
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCompadd() {
        return comp_add;
    }
    public void setCompadd(String comp_add) {
        this.comp_add = comp_add;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInstname() {
        return inst_name;
    }
    public void setInstname(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getInstid() {
        return instid;
    }
    public void setInstid(String instid) {
        this.instid = instid;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}
