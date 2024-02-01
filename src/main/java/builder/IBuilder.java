package builder;

public interface IBuilder {
    void setAddress(String address);
    //public void addImage123(); forse va fatto con decorator TODO
    void setFloor(int floor);
    void setElevator(boolean elevator);
    void setBalconies(int balconies);
    void setTerrace(int terrace);
    void setGarden(int garden);
    void setAccessories(int accessories);
    void setBedrooms(int beedrooms);
    void setSqm(int sqm);

    House getResult();
}
