package builder;
// garage, loft, apartment, independent house TODO elimina commento
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
    private String address;
    private int floor;
    private boolean elevator;
    private int balconies;
    private int terrace;
    private int garden;
    private int accessories;
    private int bedrooms;
    private int sqm;

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