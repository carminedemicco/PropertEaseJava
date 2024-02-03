package main.propertease.builder;

import main.propertease.decorator.IPoster;
import javafx.scene.image.Image;

// House class
public class House implements IPoster {
    public House(int id, String address, int floor, boolean elevator, int balconies, int terrace,
                 int garden, int accessories, int bedrooms, int sqm, int price, Image[] pics) {
        this.id = id;
        this.address = address;
        this.floor = floor;
        this.elevator = elevator;
        this.balconies = balconies;
        this.terrace = terrace;
        this.garden = garden;
        this.accessories = accessories;
        this.bedrooms = bedrooms;
        this.sqm = sqm;
        this.price = price;
        this.pics = pics;
    }

    private final int id;
    private final String address;
    private final int floor;
    private final boolean elevator;
    private final int balconies;
    private final int terrace;
    private final int garden;
    private final int accessories;
    private final int bedrooms;
    private final int sqm;
    private final int price;
    private Image[] pics = new Image[3];

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getFloor() {
        return floor;
    }

    public boolean hasElevator() {
        return elevator;
    }

    public int getBalconies() {
        return balconies;
    }

    public int getTerrace() {
        return terrace;
    }

    public int getGarden() {
        return garden;
    }

    public int getAccessories() {
        return accessories;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getSqm() {
        return sqm;
    }

    public int getPrice() {
        return price;
    }

    public Image getPics(Image[] pics, int index) {
        return this.pics[index];
    }
}