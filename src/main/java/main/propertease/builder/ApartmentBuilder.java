package main.propertease.builder;

import javafx.scene.image.Image;

import java.lang.UnsupportedOperationException;

public class ApartmentBuilder implements IBuilder {
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    @Override
    public void setBalconies(int balconies) {
        this.balconies = balconies;
    }

    @Override
    public void setTerrace(int terrace) {
        this.terrace = terrace;
    }

    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for ApartmentBuilder");
    }

    @Override
    public void setAccessories(int accessories) {
        this.accessories = accessories;
    }

    @Override
    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    @Override
    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setPics(Image[] pics) {
        this.pics = pics;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setType(HouseType type) {
        this.type = type;
    }

    @Override
    public House getResult() {
        return new House(
            id,
            type,
            address,
            floor,
            elevator,
            balconies,
            terrace,
            0,
            accessories,
            bedrooms,
            sqm,
            price,
            pics
        );
    }

    private int id;
    private HouseType type;
    private String address;
    private int floor;
    private boolean elevator;
    private int balconies;
    private int terrace;
    private int accessories;
    private int bedrooms;
    private int sqm;
    private int price;
    private Image[] pics = new Image[3];
}