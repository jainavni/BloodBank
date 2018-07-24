package com.example.gagan.bloodbank;

public class ListItem {
    String name, city,bloodgroup,mob;
    public ListItem()
    {

    }

    public ListItem(String name, String city, String bloodgroup, String mob) {
        this.name = name;
        this.city = city;
        this.bloodgroup = bloodgroup;
        this.mob = mob;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public String getMob() {
        return mob;
    }
}
