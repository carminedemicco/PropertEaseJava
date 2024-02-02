package builder;

import javafx.scene.image.Image;

import java.lang.UnsupportedOperationException;

public class ApartmentBuilder implements IBuilder {
    @Override
    public void setAddress(String address) {
        _address = address;
    }

    @Override
    public void setFloor(int floor) {
        _floor = floor;
    }

    @Override
    public void setElevator(boolean elevator) {
        _elevator = elevator;
    }

    @Override
    public void setBalconies(int balconies) {
        _balconies = balconies;
    }

    @Override
    public void setTerrace(int terrace) {
        _terrace = terrace;
    }

    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for ApartmentBuilder");
    }

    @Override
    public void setAccessories(int accessories) {
        _accessories = accessories;
    }

    @Override
    public void setBedrooms(int bedrooms) {
        _bedrooms = bedrooms;
    }

    @Override
    public void setSqm(int sqm) {
        _sqm = sqm;
    }

    @Override
    public void setPrice(int price) {
        _price = price;
    }

    @Override
    public void setPics(Image[] pics) {
        _pics = pics;
    }

    @Override
    public House getResult() {
        return new House(
                _id,
                _address,
                _floor,
                _elevator,
                _balconies,
                _terrace,
                0,
                _accessories,
                _bedrooms,
                _sqm,
                _price,
                _pics
        );
    }

    private int _id;
    private String _address;
    private int _floor;
    private boolean _elevator;
    private int _balconies;
    private int _terrace;
    private int _accessories;
    private int _bedrooms;
    private int _sqm;
    private int _price;
    private Image[] _pics = new Image[3];
}
