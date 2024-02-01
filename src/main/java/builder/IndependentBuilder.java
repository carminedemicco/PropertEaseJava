package builder;

import java.lang.UnsupportedOperationException;

public class IndependentBuilder implements IBuilder {
    @Override
    public void setAddress(String address) {
        _address = address;
    }

    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for IndependentBuilder");
    }

    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for IndependentBuilder");
    }

    @Override
    public void setBalconies(int balconies) { _balconies = balconies; }

    @Override
    public void setTerrace(int terrace) { _terrace = terrace; }

    @Override
    public void setGarden(int garden) { _garden = garden; }

    @Override
    public void setAccessories(int accessories) { _accessories = accessories; }

    @Override
    public void setBedrooms(int bedrooms) { _bedrooms = bedrooms; }

    @Override
    public void setSqm(int sqm) {
        _sqm = sqm;
    }

    @Override
    public House getResult() {
        return new House(
                _address,
                0,
                false,
                _balconies,
                _terrace,
                _garden,
                _accessories,
                _bedrooms,
                _sqm
        );
    }

    private String _address;
    private int _balconies;
    private int _terrace;
    private int _garden;
    private int _accessories;
    private int _bedrooms;
    private int _sqm;
}