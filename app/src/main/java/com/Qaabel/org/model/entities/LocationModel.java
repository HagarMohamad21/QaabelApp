package com.Qaabel.org.model.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LocationModel
{
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;

    public LocationModel(double latitude, double longitude)
    {
        coordinates=new ArrayList<>();
        coordinates.add(0,latitude);
        coordinates.add(1,longitude);
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
