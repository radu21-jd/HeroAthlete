package com.example.l999;

public class Users {
    String userId;
    Boolean isSetup;

    public Users(){
        //firebase
    }

    public Users(Boolean isSetup){

        this.isSetup=isSetup;
    }

    //public String getUserId(){return userId;}
    public Boolean getIsSetup(){return isSetup;}
}
