package main.propertease.decorator;

public class HouseDecorator implements HouseInterface {
    private HouseInterface wrappee;

    HouseDecorator(HouseInterface source) {
        wrappee = source;
    }

    @Override
    public int getPrice() {
        return wrappee.getPrice();
    }
}