package main.propertease.builder;

import javafx.scene.image.Image;

import java.lang.UnsupportedOperationException;

/**
 * Concrete builder class for building apartments.
 */
public class ApartmentBuilder implements HouseBuilder {
    /**
     * Sets the address of the apartment.
     *
     * @param address The address of the apartment.
     */
    @Override
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the floor number of the apartment.
     *
     * @param floor The floor number of the apartment.
     */
    @Override
    public void setFloor(int floor) { this.floor = floor; }

    /**
     * Sets whether the apartment has an elevator.
     *
     * @param elevator True if the apartment has an elevator, false otherwise.
     */
    @Override
    public void setElevator(boolean elevator) { this.elevator = elevator; }

    /**
     * Sets the number of balconies in the apartment.
     *
     * @param balconies The number of balconies in the apartment.
     */
    @Override
    public void setBalconies(int balconies) { this.balconies = balconies; }

    /**
     * Sets the terrace area of the apartment.
     *
     * @param terrace The terrace area of the apartment.
     */
    @Override
    public void setTerrace(int terrace) { this.terrace = terrace; }

    /**
     * Throws an UnsupportedOperationException as setting a garden is not allowed for ApartmentBuilder.
     *
     * @param garden The area of the garden.
     * @throws UnsupportedOperationException Always thrown to indicate that setting a garden is not allowed.
     */
    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for ApartmentBuilder");
    }

    /**
     * Sets the number of accessories in the apartment.
     *
     * @param accessories The number of accessories in the apartment.
     */
    @Override
    public void setAccessories(int accessories) { this.accessories = accessories; }

    /**
     * Sets the number of bedrooms in the apartment.
     *
     * @param bedrooms The number of bedrooms in the apartment.
     */
    @Override
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    /**
     * Sets the square meters (sqm) of the apartment.
     *
     * @param sqm The square meters (sqm) of the apartment.
     */
    @Override
    public void setSqm(int sqm) { this.sqm = sqm; }

    /**
     * Sets the price of the apartment.
     *
     * @param price The price of the apartment.
     */
    @Override
    public void setPrice(int price) { this.price = price; }

    /**
     * Sets the description of the apartment.
     *
     * @param description The description of the apartment.
     */
    @Override
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the pictures of the apartment.
     *
     * @param pics The pictures of the apartment.
     */
    @Override
    public void setPics(Image[] pics) { this.pics = pics; }

    /**
     * Sets the ID of the apartment.
     *
     * @param id The ID of the apartment.
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
     * Builds and returns the apartment based on the provided information.
     *
     * @return The built apartment.
     */
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
                0, // Garden is not applicable for apartments
                accessories,
                bedrooms,
                sqm,
                price,
                description,
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
    private String description;
    private Image[] pics = new Image[3];
}
