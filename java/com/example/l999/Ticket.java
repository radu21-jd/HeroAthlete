package com.example.l999;

public class Ticket {

    private String userName;
    private String details;
    private String category;
    private String numeCompetitie;
    private int qrCodeImageResource;

    public Ticket() {
        //
    }

    public Ticket(String userName, String details, String category, String numeCompetitie) {
        this.userName = userName;
        this.details = details;
        this.category = category;
        this.numeCompetitie = numeCompetitie;
        this.qrCodeImageResource = R.drawable.qr;
    }

    public int getQrCodeImageResource() {
        return qrCodeImageResource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumeCompetitie() {
        return numeCompetitie;
    }

    public void setNumeCompetitie(String numeCompetitie) {
        this.numeCompetitie = numeCompetitie;
    }

}
