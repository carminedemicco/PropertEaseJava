package main.propertease.builder;

import javafx.scene.image.Image;

public class HouseDirector {
    public HouseDirector(HouseBuilder builder) {
        this.builder = builder;
    }

    public House constructApartment(
        int id,
        HouseType type,
        String address,
        int floor,
        boolean elevator,
        int balconies,
        int terrace,
        int accessories,
        int bedrooms,
        int sqm,
        int price,
        String description,
        Image[] pics
    ) {
        builder.setId(id);
        builder.setType(type);
        builder.setAddress(address);
        builder.setFloor(floor);
        builder.setElevator(elevator);
        builder.setTerrace(terrace);
        builder.setBalconies(balconies);
        builder.setAccessories(accessories);
        builder.setBedrooms(bedrooms);
        builder.setSqm(sqm);
        builder.setPrice(price);
        builder.setDescription(description);
        builder.setPics(pics);

        return builder.getResult();
    }

    public House constructGarage(
        int id,
        HouseType type,
        String address,
        int sqm,
        int price,
        String description,
        Image[] pics
    ) {
        builder.setId(id);
        builder.setType(type);
        builder.setAddress(address);
        builder.setSqm(sqm);
        builder.setPrice(price);
        builder.setDescription(description);
        builder.setPics(pics);

        return builder.getResult();
    }

    public House constructIndependent(
        int id,
        HouseType type,
        String address,
        int balconies,
        int terrace,
        int accessories,
        int bedrooms,
        int sqm,
        int price,
        String description,
        Image[] pics
    ) {
        builder.setId(id);
        builder.setType(type);
        builder.setAddress(address);
        builder.setTerrace(terrace);
        builder.setBalconies(balconies);
        builder.setAccessories(accessories);
        builder.setBedrooms(bedrooms);
        builder.setSqm(sqm);
        builder.setPrice(price);
        builder.setDescription(description);
        builder.setPics(pics);

        return builder.getResult();
    }

    private final HouseBuilder builder;
}
