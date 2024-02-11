package main.propertease.builder;

/**
 * Enum representing different types of houses.
 */
public enum HouseType {
    APARTMENT(0),
    GARAGE(1),
    INDEPENDENT(2);

    private final int value;

    /**
     * Constructor for HouseType enum.
     *
     * @param value The numerical value associated with the house type.
     */
    HouseType(int value) { this.value = value; }

    /**
     * Returns the HouseType enum corresponding to the given numerical value.
     *
     * @param value The numerical value representing the house type.
     * @return The HouseType enum.
     * @throws IllegalArgumentException if the value is unknown.
     */
    public static HouseType fromValue(int value) {
        return switch (value) {
            case 0 -> APARTMENT;
            case 1 -> GARAGE;
            case 2 -> INDEPENDENT;
            default -> throw new IllegalArgumentException("Unknown value: " + value);
        };
    }

    /**
     * Gets the numerical value associated with the house type.
     *
     * @return The numerical value.
     */
    public int getValue() { return value; }

    /**
     * Returns a string representation of the house type.
     *
     * @return A string representation of the house type.
     */
    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "Apartment";
            case 1 -> "Garage";
            case 2 -> "Independent";
            default -> "Unknown";
        };
    }
}