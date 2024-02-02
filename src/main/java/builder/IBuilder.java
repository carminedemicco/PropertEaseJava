package builder;

import javafx.scene.image.Image;

public interface IBuilder {
    void setAddress(String address);

    void setFloor(int floor);

    void setElevator(boolean elevator);

    void setBalconies(int balconies);

    void setTerrace(int terrace);

    void setGarden(int garden);

    void setAccessories(int accessories);

    void setBedrooms(int beedrooms);

    void setSqm(int sqm);

    void setPrice(int price);

    void setPics(Image[] pics);

    void setId(int id);

    House getResult();
}
