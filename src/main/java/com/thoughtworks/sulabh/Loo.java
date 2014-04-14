package com.thoughtworks.sulabh;

import com.example.R;

import java.util.List;

public class Loo {
    private String name;
    private List<Integer> coordinates;
    private String operational;
    private String hygienic;
    private String paid;
    private String kind;
    private String compatibility;


    public String getName() {
        return name;
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public String getOperational() {
        return operational;
    }

    public String getHygienic() {
        return hygienic;
    }

    public String getPaid() {
        return paid;
    }

    public String getKind() {
        return kind;
    }

    public String getCompatibility() {
        return compatibility;
    }

    @Override
    public String toString() {
        return "Loo{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", operational='" + operational + '\'' +
                ", hygienic='" + hygienic + '\'' +
                ", paid='" + paid + '\'' +
                ", kind='" + kind + '\'' +
                ", compatibility='" + compatibility + '\'' +
                '}';
    }
}