package decorator;

public class PriceDecorator
        extends BaseDecorator {
    PriceDecorator(IPoster source) {
        super(source);
    }

    @Override
    public int getPrice() {
        return generatePromo(super.getPrice());
    }

    private int generatePromo(int price) {
        return (int) (price * 0.8);
    }
}