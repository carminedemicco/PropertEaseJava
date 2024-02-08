package main.propertease.builder;

import main.propertease.decorator.HouseInterface;
import javafx.scene.image.Image;
import main.propertease.memento.Memento;

// House class
public class House implements HouseInterface {
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

    public int getId() {
        return id;
    }

    public HouseType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public int getFloor() {
        return floor;
    }

    public boolean hasElevator() {
        return elevator;
    }

    public int getBalconies() {
        return balconies;
    }

    public int getTerrace() {
        return terrace;
    }

    public int getGarden() {
        return garden;
    }

    public int getAccessories() {
        return accessories;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getSqm() {
        return sqm;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Image getPics(int index) {
        return this.pics[index];
    }

    public String getName() {
        return getType().toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(HouseType type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public void setBalconies(int balconies) {
        this.balconies = balconies;
    }

    public void setTerrace(int terrace) {
        this.terrace = terrace;
    }

    public void setGarden(int garden) {
        this.garden = garden;
    }

    public void setAccessories(int accessories) {
        this.accessories = accessories;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPics(Image[] pics) {
        this.pics = pics;
    }

    // Pattern Memento
    public Memento createMemento() {
        return new HouseMemento();
    }

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