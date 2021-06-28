package com.izhar.usertip;

public class Game {
    String league, home, away, date, status, tip, tip_type, odd, time;

    public Game(String league, String home, String away, String date, String status, String tip, String tip_type, String odd, String time) {
        this.league = league;
        this.home = home;
        this.away = away;
        this.date = date;
        this.status = status;
        this.tip = tip;
        this.tip_type = tip_type;
        this.odd = odd;
        this.time = time;
    }


    public Game(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip_type() {
        return tip_type;
    }

    public void setTip_type(String tip_type) {
        this.tip_type = tip_type;
    }

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }
}
