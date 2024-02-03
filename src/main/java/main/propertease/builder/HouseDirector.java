package main.propertease.builder;

import javafx.scene.image.Image;

public class HouseDirector {
    public House constructApartment(IBuilder builder, int id, String address, int floor, boolean elevator, int balconies, int terrace,
                                    int garden, int accessories, int bedrooms, int sqm, int price, Image[] pics) {
        builder.setId(id);
        builder.setAddress(address);
        builder.setFloor(floor);
        builder.setElevator(elevator);
        builder.setTerrace(terrace);
        builder.setBalconies(balconies);
        builder.setAccessories(accessories);
        builder.setBedrooms(bedrooms);
        builder.setSqm(sqm);
        builder.setPrice(price);
        builder.setPics(pics);

        return builder.getResult();
    }

    public House constructGarage(IBuilder builder) {
        builder.setAddress("Via Don Giuseppe");
        builder.setSqm(120);
        builder.setPrice(18000);
        //builder.setPics();

        return builder.getResult();
    }

    public House constructIndependent(IBuilder builder) {
        builder.setAddress("Via Don Giuseppe");
        builder.setTerrace(23);
        builder.setBalconies(3);
        builder.setGarden(32);
        builder.setAccessories(5);
        builder.setBedrooms(6);
        builder.setSqm(120);
        builder.setPrice(260000);
        //builder.setPics();

        return builder.getResult();
    }
}
