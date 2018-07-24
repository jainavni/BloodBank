package com.example.gagan.bloodbank;

public class Userinfo {
    String name, email, address, state, city, gender, bloodgroup, mob, donor,userid;

    public Userinfo() {
    }

    public Userinfo(String name, String email, String address, String state, String city, String gender, String bloodgroup, String mob, String donor, String userid) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.state = state;
        this.city = city;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.mob = mob;
        this.donor = donor;
        this.userid=userid;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
