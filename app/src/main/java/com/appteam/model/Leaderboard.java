package com.appteam.model;


/**
 * Created by LENOVO on 26-09-2018.
 */
public class Leaderboard {

    String name;
    int candies;
    String gender;
    String image_url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCandies() {
        return candies;
    }

    public void setCandies(int candies) {
        this.candies = candies;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Leaderboard(String name, int candies, String gender, String image_url) {
        this.name = name;
        this.candies = candies;
        this.gender = gender;
        this.image_url = image_url;
    }
}
