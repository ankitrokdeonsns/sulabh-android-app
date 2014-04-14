package com.thoughtworks.sulabh;

import java.util.ArrayList;
import java.util.List;

public class LooList {
    private List<Loo> locations = new ArrayList<Loo>();


    public List<Loo> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        return "LooList{" +
                "locations=" + locations +
                '}';
    }
}