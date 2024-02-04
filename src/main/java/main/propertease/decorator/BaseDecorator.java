package main.propertease.decorator;

public class BaseDecorator implements Poster {
    private Poster wrappee;

    BaseDecorator(Poster source) {
        wrappee = source;
    }

    @Override
    public int getPrice() {
        return wrappee.getPrice();
    }
}