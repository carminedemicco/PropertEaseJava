package main.propertease.decorator;

public class BaseDecorator implements IPoster {
    private IPoster wrappee;

    BaseDecorator(IPoster source) {
        wrappee = source;
    }

    @Override
    public int getPrice() {
        return wrappee.getPrice();
    }
}