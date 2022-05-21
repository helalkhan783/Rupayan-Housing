package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReconcilationType {
    @SerializedName("Damage")
    @Expose
    private String damage;
    @SerializedName("Increase")
    @Expose
    private String increase;
    @SerializedName("Process Lost")
    @Expose
    private String lost;
    @SerializedName("Expire")
    @Expose
    private String expire;

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getLost() {
        return lost;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
