package main.propertease.builder;

import javafx.scene.image.Image;

import java.lang.UnsupportedOperationException;

public class IndependentBuilder implements IBuilder {
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for IndependentBuilder");
    }

    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for IndependentBuilder");
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
        this.garden = garden;
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
            0,
            false,
            balconies,
            terrace,
            garden,
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
    private int balconies;
    private int terrace;
    private int garden;
    private int accessories;
    private int bedrooms;
    private int sqm;
    private int price;
    private Image[] pics = new Image[3];
}