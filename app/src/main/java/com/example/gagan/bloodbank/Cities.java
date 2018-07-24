package com.example.gagan.bloodbank;

import java.util.List;

public class Cities {
    private String state;
    private List<String> cities;

    public String getState() {
        return state;
    }

    public List<String> getCities() {
        return cities;
    }

    public Cities(String state, List<String> cities) {

        this.state = state;
        this.cities = cities;
    }
}
