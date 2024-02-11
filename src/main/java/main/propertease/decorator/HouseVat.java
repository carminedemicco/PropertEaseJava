package main.propertease.decorator;

/**
 * Decorator class for applying VAT to the price of a house.
 */
public class HouseVat extends HouseDecorator {

    /**
     * Constructor for HouseVat.
     *
     * @param source The HouseInterface object to be wrapped.
     */
    public HouseVat(HouseInterface source) {
        super(source);
    }

    /**
     * Gets the price of the house including VAT.
     *
     * @return The price of the house with VAT applied.
     */
    @Override
    public int getPrice() {
        return getWithoutVAT(super.getPrice());
    }

    /**
     * Calculates the price of the house without VAT.
     *
     * @param price The price of the house with VAT.
     * @return The price of the house without VAT.
     */
    private int getWithoutVAT(int price) {
        return (int)(price * 0.78); // Assuming VAT rate of 22%
    }
}
