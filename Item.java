//Robert Bajan 17/05/22
package com.example.lostandfound.model;

public class Item {
    private int item_id;
    private String type;
    private String name;
    private String phone;
    private String desc;
    private String date;
    private String location;

    public Item(String type, String name, String phone, String desc, String date, String location){
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.desc = desc;
        this.date = date;
        this.location = location;
    }

    public Item() {
    }

    public int getItem_id() {
        return item_id;
    }
    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
