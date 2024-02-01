package builder;

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
    public House getResult() {
        return new House(
            _address,
            _floor,
            _elevator,
            _balconies,
            _terrace,
            0,
            _accessories,
            _bedrooms,
            _sqm
        );
    }

    private String _address;
    private int _floor;
    private boolean _elevator;
    private int _balconies;
    private int _terrace;
    private int _accessories;
    private int _bedrooms;
    private int _sqm;
}
