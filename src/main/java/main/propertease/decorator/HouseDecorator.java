package main.propertease.decorator;

/**
 * Base class for house decorators.
 */
public class HouseDecorator implements HouseInterface {

    /**
     * The wrapped HouseInterface object.
     */
    private final HouseInterface wrappee;

    /**
     * Constructor for HouseDecorator.
     *
     * @param source The HouseInterface object to be wrapped.
     */
    HouseDecorator(HouseInterface source) {
        wrappee = source;
    }

    /**
     * Gets the price of the house.
     *
     * @return The price of the house.
     */
    @Override
    public int getPrice() {
        return wrappee.getPrice();
    }
}