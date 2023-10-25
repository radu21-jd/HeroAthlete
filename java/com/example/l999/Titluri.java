package com.example.l999;

public class Titluri {
    String name;
    int points;
    Boolean isBuy;
    String id;
    Boolean isActive;
    Boolean tokenCM;
    Boolean tokenJO;
    Boolean tokenObj;

    public Titluri(){
        //
    }

    public Titluri(String name, int points, Boolean isBuy, String id, Boolean isActive, Boolean tokenCM, Boolean tokenJO, Boolean tokenObj){
        this.name=name;
        this.points=points;
        this.isBuy=isBuy;
        this.id=id;
        this.isActive=isActive;
        this.tokenCM=tokenCM;
        this.tokenJO=tokenJO;
        this.tokenObj=tokenObj;
    }

    public String getName(){return name;}
    public int getPoints(){return points;}
    public Boolean getIsBuy(){return isBuy;}
    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Boolean getIsActive(){return isActive;}
    public Boolean getTokenCM(){return tokenCM;}
    public Boolean getTokenJO(){return tokenJO;}
    public Boolean getTokenObj(){return tokenObj;}
}
