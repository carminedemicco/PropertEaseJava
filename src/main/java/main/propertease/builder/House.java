package main.propertease.builder;

import main.propertease.decorator.HouseInterface;
import javafx.scene.image.Image;
import main.propertease.memento.Memento;

/**
 * Class representing a house.
 */
public class House implements HouseInterface {

    private int id;
    private HouseType type;
    private String address;
    private int floor;
    private boolean elevator;
    private int balconies;
    private int terrace;
    private int garden;
    private int accessories;
    private int bedrooms;
    private int sqm;
    private int price;
    private String description;
    private Image[] pics;

    /**
     * Constructor for creating a House object.
     *
     * @param id          The ID of the house.
     * @param type        The type of the house (Apartment, Garage, Independent).
     * @param address     The address of the house.
     * @param floor       The floor number of the house.
     * @param elevator    True if the house has an elevator, false otherwise.
     * @param balconies   The number of balconies in the house.
     * @param terrace     The terrace area of the house in sqm.
     * @param garden      The garden area of the house in sqm.
     * @param accessories The number of accessories in the house.
     * @param bedrooms    The number of bedrooms in the house.
     * @param sqm         The square meters (sqm) of the house.
     * @param price       The price of the house.
     * @param description The description of the house.
     * @param pics        The pictures of the house.
     */
    public House(
            int id,
            HouseType type,
            String address,
            int floor,
            boolean elevator,
            int balconies,
            int terrace,
            int garden,
            int accessories,
            int bedrooms,
            int sqm,
            int price,
            String description,
            Image[] pics
    ) {
        this.id = id;
        this.type = type;
        this.address = address;
        this.floor = floor;
        this.elevator = elevator;
        this.balconies = balconies;
        this.terrace = terrace;
        this.garden = garden;
        this.accessories = accessories;
        this.bedrooms = bedrooms;
        this.sqm = sqm;
        this.price = price;
        this.description = description;
        this.pics = pics;
    }

    /**
     * Returns the ID of the house.
     *
     * @return The ID of the house.
     */
    public int getId() { return id; }

    /**
     * Returns the type of the house.
     *
     * @return The type of the house.
     */
    public HouseType getType() { return type; }

    /**
     * Returns the address of the house.
     *
     * @return The address of the house.
     */
    public String getAddress() { return address; }

    /**
     * Returns the floor number of the house.
     *
     * @return The floor number of the house.
     */
    public int getFloor() { return floor; }

    /**
     * Returns true if the house has an elevator, otherwise false.
     *
     * @return True if the house has an elevator, otherwise false.
     */
    public boolean hasElevator() { return elevator; }

    /**
     * Returns the number of balconies in the house.
     *
     * @return The number of balconies in the house.
     */
    public int getBalconies() { return balconies; }

    /**
     * Returns the terrace area of the house.
     *
     * @return The terrace area of the house.
     */
    public int getTerrace() { return terrace; }

    /**
     * Returns the garden area of the house.
     *
     * @return The garden area of the house.
     */
    public int getGarden() { return garden; }

    /**
     * Returns the number of accessories in the house.
     *
     * @return The number of accessories in the house.
     */
    public int getAccessories() { return accessories; }

    /**
     * Returns the number of bedrooms in the house.
     *
     * @return The number of bedrooms in the house.
     */
    public int getBedrooms() { return bedrooms; }

    /**
     * Returns the square meters (sqm) of the house.
     *
     * @return The square meters (sqm) of the house.
     */
    public int getSqm() { return sqm; }

    /**
     * Returns the price of the house.
     *
     * @return The price of the house.
     */
    public int getPrice() { return price; }

    /**
     * Returns the description of the house.
     *
     * @return The description of the house.
     */
    public String getDescription() { return description; }

    /**
     * Returns the picture at the specified index.
     *
     * @param index The index of the picture.
     * @return The picture at the specified index.
     */
    public Image getPics(int index) { return this.pics[index]; }

    /**
     * Returns the name of the house type.
     *
     * @return The name of the house type.
     */
    public String getName() { return getType().toString(); }

    /**
     * Sets the ID of the house.
     *
     * @param id The ID of the house.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Sets the type of the house.
     *
     * @param type The type of the house.
     */
    public void setType(HouseType type) { this.type = type; }

    /**
     * Sets the address of the house.
     *
     * @param address The address of the house.
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the floor number of the house.
     *
     * @param floor The floor number of the house.
     */
    public void setFloor(int floor) { this.floor = floor; }

    /**
     * Sets whether the house has an elevator.
     *
     * @param elevator True if the house has an elevator, otherwise false.
     */
    public void setElevator(boolean elevator) { this.elevator = elevator; }

    /**
     * Sets the number of balconies in the house.
     *
     * @param balconies The number of balconies in the house.
     */
    public void setBalconies(int balconies) { this.balconies = balconies; }

    /**
     * Sets the terrace area of the house.
     *
     * @param terrace The terrace area of the house.
     */
    public void setTerrace(int terrace) { this.terrace = terrace; }

    /**
     * Sets the garden area of the house.
     *
     * @param garden The garden area of the house.
     */
    public void setGarden(int garden) { this.garden = garden; }

    /**
     * Sets the number of accessories in the house.
     *
     * @param accessories The number of accessories in the house.
     */
    public void setAccessories(int accessories) { this.accessories = accessories; }

    /**
     * Sets the number of bedrooms in the house.
     *
     * @param bedrooms The number of bedrooms in the house.
     */
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    /**
     * Sets the square meters (sqm) of the house.
     *
     * @param sqm The square meters (sqm) of the house.
     */
    public void setSqm(int sqm) { this.sqm = sqm; }

    /**
     * Sets the price of the house.
     *
     * @param price The price of the house.
     */
    public void setPrice(int price) { this.price = price; }

    /**
     * Sets the description of the house.
     *
     * @param description The description of the house.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the pictures of the house.
     *
     * @param pics The pictures of the house.
     */
    public void setPics(Image[] pics) { this.pics = pics; }

    /**
     * Creates a memento object representing the current state of the house.
     *
     * @return The memento object.
     */
    public Memento createMemento() { return new HouseMemento(); }

    /**
     * Class representing a memento object for the house.
     */
    public class HouseMemento implements Memento {
        private final int mem_id;
        private final HouseType mem_type;
        private final String mem_address;
        private final int mem_floor;
        private final boolean mem_elevator;
        private final int mem_balconies;
        private final int mem_terrace;
        private final int mem_garden;
        private final int mem_accessories;
        private final int mem_bedrooms;
        private final int mem_sqm;
        private final int mem_price;
        private final String mem_description;
        private final Image[] mem_pics;

        /**
         * Constructor for creating a memento object.
         */
        public HouseMemento() {
            mem_id = id;
            mem_type = type;
            mem_address = address;
            mem_floor = floor;
            mem_elevator = elevator;
            mem_balconies = balconies;
            mem_terrace = terrace;
            mem_garden = garden;
            mem_accessories = accessories;
            mem_bedrooms = bedrooms;
            mem_sqm = sqm;
            mem_price = price;
            mem_description = description;
            mem_pics = pics;
        }

        /**
         * Restores the state of the house using the values stored in the memento object.
         */
        @Override
        public void restoreState() {
            id = mem_id;
            type = mem_type;
            address = mem_address;
            floor = mem_floor;
            elevator = mem_elevator;
            balconies = mem_balconies;
            terrace = mem_terrace;
            garden = mem_garden;
            accessories = mem_accessories;
            bedrooms = mem_bedrooms;
            sqm = mem_sqm;
            price = mem_price;
            description = mem_description;
            pics = mem_pics;
        }
    }
}