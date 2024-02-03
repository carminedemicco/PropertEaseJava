package main.propertease.builder;

public enum HouseType {
    APARTMENT(0),
    GARAGE(1),
    INDEPENDENT(2);

    HouseType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "Apartment";
            case 1 -> "Garage";
            case 2 -> "Independent";
            default -> "Unknown";
        };
    }

    private final int value;
}
