package main.propertease.builder;

import javafx.scene.image.Image;

import java.lang.UnsupportedOperationException;

public class GarageBuilder implements HouseBuilder {
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for GarageBuilder");
    }

    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for GarageBuilder");
    }

    @Override
    public void setBalconies(int balconies) {
        throw new UnsupportedOperationException("setBalconies() not allowed for GarageBuilder");
    }

    @Override
    public void setTerrace(int terrace) {
        throw new UnsupportedOperationException("setTerrace() not allowed for GarageBuilder");
    }

    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for GarageBuilder");
    }

    @Override
    public void setAccessories(int accessories) {
        throw new UnsupportedOperationException("setAccessories() not allowed for GarageBuilder");
    }

    @Override
    public void setBedrooms(int bedrooms) {
        throw new UnsupportedOperationException("setBedrooms() not allowed for GarageBuilder");
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
    public void setDescription(String description) {
        this.description = description;
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
            0,
            0,
            0,
            0,
            0,
            sqm,
            price,
            description,
            pics
        );
    }

    private int id;
    private HouseType type;
    private String address;
    private int sqm;
    private int price;
    private String description;
    private Image[] pics = new Image[3];
}
