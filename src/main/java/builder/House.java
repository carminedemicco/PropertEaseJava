package builder;
// House class
public class House {

    public House (String address, int floor, boolean elevator, int balconies, int terrace,
                  int garden, int accessories, int bedrooms, int sqm){
        this.address = address;
        this.floor = floor;
        this.elevator = elevator;
        this.balconies = balconies;
        this.terrace = terrace;
        this.garden = garden;
        this.accessories = accessories;
        this.bedrooms = bedrooms;
        this.sqm = sqm;
    }
    private final String address;
    private final int floor;
    private final boolean elevator;
    private final int balconies;
    private final int terrace;
    private final int garden;
    private final int accessories;
    private final int bedrooms;
    private final int sqm;

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
}