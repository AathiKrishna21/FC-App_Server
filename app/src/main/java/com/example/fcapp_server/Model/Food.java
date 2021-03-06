package com.example.fcapp_server.Model;

public class Food {
    private String Image;
    private String MenuId;
    private String Name;
    private String Price;
    private Boolean Availability;



    public Food(String image, String menuId, String name, String price, Boolean availability) {
        Image = image;
        MenuId = menuId;
        Name = name;
        Price = price;
        Availability=availability;
    }
    public Boolean getAvailability() {
        return Availability;
    }

    public void setAvailability(Boolean availability) {
        Availability = availability;
    }

    public Food() {

    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }




}
