package main.propertease.builder;

import javafx.scene.image.Image;
import main.propertease.House;

/**
 * Interface for building houses.
 */
public interface HouseBuilder {
    /**
     * Sets the address of the house.
     *
     * @param address The address of the house.
     */
    void setAddress(String address);

    /**
     * Sets the floor number of the house.
     *
     * @param floor The floor number of the house.
     */
    void setFloor(int floor);

    /**
     * Sets whether the house has an elevator.
     *
     * @param elevator True if the house has an elevator, false otherwise.
     */
    void setElevator(boolean elevator);

    /**
     * Sets the number of balconies in the house.
     *
     * @param balconies The number of balconies in the house.
     */
    void setBalconies(int balconies);

    /**
     * Sets the terrace area of the house.
     *
     * @param terrace The terrace area of the house.
     */
    void setTerrace(int terrace);

    /**
     * Sets the garden area of the house.
     *
     * @param garden The garden area of the house.
     */
    void setGarden(int garden);

    /**
     * Sets the number of accessories in the house.
     *
     * @param accessories The number of accessories in the house.
     */
    void setAccessories(int accessories);

    /**
     * Sets the number of bedrooms in the house.
     *
     * @param bedrooms The number of bedrooms in the house.
     */
    void setBedrooms(int bedrooms);

    /**
     * Sets the square meters (sqm) of the house.
     *
     * @param sqm The square meters (sqm) of the house.
     */
    void setSqm(int sqm);

    /**
     * Sets the price of the house.
     *
     * @param price The price of the house.
     */
    void setPrice(int price);

    /**
     * Sets the description of the house.
     *
     * @param description The description of the house.
     */
    void setDescription(String description);

    /**
     * Sets the pictures of the house.
     *
     * @param pics The pictures of the house.
     */
    void setPics(Image[] pics);

    /**
     * Sets the ID of the house.
     *
     * @param id The ID of the house.
     */
    void setId(int id);

    /**
     * Sets the type of the house.
     *
     * @param type The type of the house.
     */
    void setType(HouseType type);

    /**
     * Returns the built house.
     *
     * @return The built house.
     */
    House getResult();
}
