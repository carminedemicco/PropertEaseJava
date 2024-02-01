package builder;

import java.lang.UnsupportedOperationException;

public class GarageBuilder implements IBuilder {
    @Override
    public void setAddress(String address) {
        _address = address;
    }

    @Override
    public void setFloor(int floor) {
        throw new UnsupportedOperationException("setFloor() not allowed for GarageBuilder");
    }

    @Override
    public void setElevator(boolean elevator) {
        throw new UnsupportedOperationException("setElevator() not allowed for GarageBuilder");
    }

    @Override
    public void setBalconies(int balconies) {
        throw new UnsupportedOperationException("setBalconies() not allowed for GarageBuilder");
    }

    @Override
    public void setTerrace(int terrace) {
        throw new UnsupportedOperationException("setTerrace() not allowed for GarageBuilder");
    }

    @Override
    public void setGarden(int garden) {
        throw new UnsupportedOperationException("setGarden() not allowed for GarageBuilder");
    }

    @Override
    public void setAccessories(int accessories) {
        throw new UnsupportedOperationException("setAccessories() not allowed for GarageBuilder");
    }

    @Override
    public void setBedrooms(int bedrooms) {
        throw new UnsupportedOperationException("setBedrooms() not allowed for GarageBuilder");
    }

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
            0,
            0,
            0,
            0,
            0,
            _sqm
        );
    }

    private String _address;
    private int _sqm;
}
