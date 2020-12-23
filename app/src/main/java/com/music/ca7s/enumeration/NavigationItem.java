package com.music.ca7s.enumeration;


public enum NavigationItem {

    NOTIFICATION(0),HOME(1), SETTINGS(2), ABOUT(3), HELP(4), T_AND_C(5), SHARE(6), RATE_US(7)/*, SHARE(7)*/, LOGOUT(8);

    private int id;

    NavigationItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
