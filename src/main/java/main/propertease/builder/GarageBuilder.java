package main.propertease.builder;

import javafx.scene.image.Image;
import main.propertease.House;

import java.lang.UnsupportedOperationException;

/**
 * Concrete builder class for building garages.
 */
public class GarageBuilder implements HouseBuilder {

    private int id;
    private HouseType type;
    private String address;
    private int sqm;
    private int price;
    private String description;
    private Image[] pics = new Image[3];

    /**
     * Sets the address of the garage.
     *
     * @param address The address of the garage.
     */
    @Override
    public void setAddress(String address) { this.address = address; }

    /**
     * Throws an UnsupportedOperationException as setting floor is not allowed for GarageBuilder.
     *
     * @param floor The floor of the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting floor is not allowed.
     */
    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting elevator is not allowed for GarageBuilder.
     *
     * @param elevator The presence of an elevator in the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting elevator is not allowed.
     */
    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting balconies is not allowed for GarageBuilder.
     *
     * @param balconies The number of balconies in the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting balconies is not allowed.
     */
    @Override
    public void setBalconies(int balconies) {
        throw new UnsupportedOperationException("setBalconies() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting terrace is not allowed for GarageBuilder.
     *
     * @param terrace The terrace area of the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting terrace is not allowed.
     */
    @Override
    public void setTerrace(int terrace) {
        throw new UnsupportedOperationException("setTerrace() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting garden is not allowed for GarageBuilder.
     *
     * @param garden The garden area of the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting garden is not allowed.
     */
    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting accessories is not allowed for GarageBuilder.
     *
     * @param accessories The number of accessories in the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting accessories is not allowed.
     */
    @Override
    public void setAccessories(int accessories) {
        throw new UnsupportedOperationException("setAccessories() not allowed for GarageBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting bedrooms is not allowed for GarageBuilder.
     *
     * @param bedrooms The number of bedrooms in the garage.
     * @throws UnsupportedOperationException Always thrown to indicate that setting bedrooms is not allowed.
     */
    @Override
    public void setBedrooms(int bedrooms) {
        throw new UnsupportedOperationException("setBedrooms() not allowed for GarageBuilder");
    }

    /**
     * Sets the square meters (sqm) of the garage.
     *
     * @param sqm The square meters (sqm) of the garage.
     */
    @Override
    public void setSqm(int sqm) { this.sqm = sqm; }

    /**
     * Sets the price of the garage.
     *
     * @param price The price of the garage.
     */
    @Override
    public void setPrice(int price) { this.price = price; }

    /**
     * Sets the description of the garage.
     *
     * @param description The description of the garage.
     */
    @Override
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the pictures of the garage.
     *
     * @param pics The pictures of the garage.
     */
    @Override
    public void setPics(Image[] pics) { this.pics = pics; }

    /**
     * Sets the ID of the garage.
     *
     * @param id The ID of the garage.
     */
    @Override
    public void setId(int id) { this.id = id; }

    /**
     * Sets the type of the house (Apartment, Villa, etc.).
     *
     * @param type The type of the house.
     */
    @Override
    public void setType(HouseType type) { this.type = type; }

    /**
     * Builds and returns the garage based on the provided information.
     *
     * @return The built garage.
     */
    @Override
    public House getResult() {
        return new House(
                id,
                type,
                address,
                0, // Garage doesn't have a floor
                false, // Garage doesn't have an elevator
                0, // Garage doesn't have balconies
                0, // Garage doesn't have a terrace
                0, // Garage doesn't have a garden
                0, // Garage doesn't have accessories
                0, // Garage doesn't have bedrooms
                sqm,
                price,
                description,
                pics
        );
    }
}