package com.example.l999;

public class User {
    String username;
    int totalPoints;
    String imageProfile;
    String country;
    String category;
    String name;

    public User()
    {
        //
    }

    public User(String username, int totalPoints, String imageProfile, String country, String category, String name){
        this.username=username;
        this.totalPoints=totalPoints;
        this.imageProfile=imageProfile;
        this.country=country;
        this.category=category;
        this.name=name;
    }

    public String getUsername(){return username;}
    public void setUsername(String username) {
        this.username = username;
    }
    public int getTotalPoints(){return totalPoints;}
    public String getImageProfile(){return imageProfile;}
    public String getCountry(){return country;}
    public String getCategory(){return category;}
    public String getName(){return name;}

}
