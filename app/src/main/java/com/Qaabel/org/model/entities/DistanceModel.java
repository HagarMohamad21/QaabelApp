package com.Qaabel.org.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistanceModel
{
    @SerializedName("calculated")
    @Expose
    private double calculated;

    public double getCalculated() {
        return calculated;
    }

    public void setCalculated(double calculated) {
        this.calculated = calculated;
    }
}
