package main.propertease.builder;

import javafx.scene.image.Image;
import main.propertease.House;

import java.lang.UnsupportedOperationException;

/**
 * Concrete builder class for building independent houses.
 */
public class IndependentBuilder implements HouseBuilder {

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
    private String description;
    private Image[] pics = new Image[3];

    /**
     * Sets the address of the independent house.
     *
     * @param address The address of the independent house.
     */
    @Override
    public void setAddress(String address) { this.address = address; }

    /**
     * Throws an UnsupportedOperationException as setting floor is not allowed for IndependentBuilder.
     *
     * @param floor The floor of the independent house.
     * @throws UnsupportedOperationException Always thrown to indicate that setting floor is not allowed.
     */
    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for IndependentBuilder");
    }

    /**
     * Throws an UnsupportedOperationException as setting elevator is not allowed for IndependentBuilder.
     *
     * @param elevator The presence of an elevator in the independent house.
     * @throws UnsupportedOperationException Always thrown to indicate that setting elevator is not allowed.
     */
    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for IndependentBuilder");
    }

    /**
     * Sets the number of balconies in the independent house.
     *
     * @param balconies The number of balconies in the independent house.
     */
    @Override
    public void setBalconies(int balconies) { this.balconies = balconies; }

    /**
     * Sets the terrace area of the independent house.
     *
     * @param terrace The terrace area of the independent house.
     */
    @Override
    public void setTerrace(int terrace) { this.terrace = terrace; }

    /**
     * Sets the garden area of the independent house.
     *
     * @param garden The garden area of the independent house.
     */
    @Override
    public void setGarden(int garden) { this.garden = garden; }

    /**
     * Sets the number of accessories in the independent house.
     *
     * @param accessories The number of accessories in the independent house.
     */
    @Override
    public void setAccessories(int accessories) { this.accessories = accessories; }

    /**
     * Sets the number of bedrooms in the independent house.
     *
     * @param bedrooms The number of bedrooms in the independent house.
     */
    @Override
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    /**
     * Sets the square meters (sqm) of the independent house.
     *
     * @param sqm The square meters (sqm) of the independent house.
     */
    @Override
    public void setSqm(int sqm) { this.sqm = sqm; }

    /**
     * Sets the price of the independent house.
     *
     * @param price The price of the independent house.
     */
    @Override
    public void setPrice(int price) { this.price = price; }

    /**
     * Sets the description of the independent house.
     *
     * @param description The description of the independent house.
     */
    @Override
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the pictures of the independent house.
     *
     * @param pics The pictures of the independent house.
     */
    @Override
    public void setPics(Image[] pics) { this.pics = pics; }

    /**
     * Sets the ID of the independent house.
     *
     * @param id The ID of the independent house.
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
     * Builds and returns the independent house based on the provided information.
     *
     * @return The built independent house.
     */
    @Override
    public House getResult() {
        return new House(
                id,
                type,
                address,
                0, // Independent house doesn't have a floor
                false, // Independent house doesn't have an elevator
                balconies,
                terrace,
                garden,
                accessories,
                bedrooms,
                sqm,
                price,
                description,
                pics
        );
    }
}