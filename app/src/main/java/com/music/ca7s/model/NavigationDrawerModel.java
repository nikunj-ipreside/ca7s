package com.music.ca7s.model;


import com.music.ca7s.enumeration.NavigationItem;

public class NavigationDrawerModel {
    NavigationItem navigationItem;
    String itemText = "";
    int itemImage;

    public int getItemImage() {
        return itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public NavigationItem getNavigationItem() {
        return navigationItem;
    }

    public void setNavigationItem(NavigationItem navigationItem) {
        this.navigationItem = navigationItem;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
