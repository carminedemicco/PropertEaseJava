package main.propertease.decorator;

public class HouseVat extends HouseDecorator {
    public HouseVat(HouseInterface source) {
        super(source);
    }

    @Override
    public int getPrice() {
        return getWithoutVAT(super.getPrice());
    }

    private int getWithoutVAT(int price) {
        return (int)(price * 0.78);
    }
}