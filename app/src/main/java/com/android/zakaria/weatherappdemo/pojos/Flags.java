
package com.android.zakaria.weatherappdemo.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Flags {

    @SerializedName("sources")
    @Expose
    private List<String> sources = null;
    @SerializedName("isd-stations")
    @Expose
    private List<String> isdStations = null;
    @SerializedName("units")
    @Expose
    private String units;

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getIsdStations() {
        return isdStations;
    }

    public void setIsdStations(List<String> isdStations) {
        this.isdStations = isdStations;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
