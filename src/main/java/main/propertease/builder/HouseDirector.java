package main.propertease.builder;

import javafx.scene.image.Image;
import main.propertease.House;

/**
 * Director class responsible for constructing houses using a given builder.
 */
public class HouseDirector {

    private final HouseBuilder builder;

    /**
     * Constructor for HouseDirector.
     *
     * @param builder The builder to be used for constructing houses.
     */
    public HouseDirector(HouseBuilder builder) { this.builder = builder; }

    /**
     * Constructs an apartment using the provided parameters.
     *
     * @param id          The ID of the apartment.
     * @param type        The type of the apartment.
     * @param address     The address of the apartment.
     * @param floor       The floor number of the apartment.
     * @param elevator    True if the apartment has an elevator, false otherwise.
     * @param balconies   The number of balconies in the apartment.
     * @param terrace     The terrace area of the apartment.
     * @param accessories The number of accessories in the apartment.
     * @param bedrooms    The number of bedrooms in the apartment.
     * @param sqm         The square meters (sqm) of the apartment.
     * @param price       The price of the apartment.
     * @param description The description of the apartment.
     * @param pics        The pictures of the apartment.
     * @return The constructed apartment.
     */
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

    /**
     * Constructs a garage using the provided parameters.
     *
     * @param id          The ID of the garage.
     * @param type        The type of the garage.
     * @param address     The address of the garage.
     * @param sqm         The square meters (sqm) of the garage.
     * @param price       The price of the garage.
     * @param description The description of the garage.
     * @param pics        The pictures of the garage.
     * @return The constructed garage.
     */
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

    /**
     * Constructs an independent house using the provided parameters.
     *
     * @param id          The ID of the independent house.
     * @param type        The type of the independent house.
     * @param address     The address of the independent house.
     * @param balconies   The number of balconies in the independent house.
     * @param terrace     The terrace area of the independent house.
     * @param accessories The number of accessories in the independent house.
     * @param bedrooms    The number of bedrooms in the independent house.
     * @param sqm         The square meters (sqm) of the independent house.
     * @param price       The price of the independent house.
     * @param description The description of the independent house.
     * @param pics        The pictures of the independent house.
     * @return The constructed independent house.
     */
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
}