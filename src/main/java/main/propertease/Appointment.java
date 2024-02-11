package main.propertease;

/**
 * Represents an appointment for visiting a house.
 */
public class Appointment {
    private int id;
    private String houseName;
    private String houseAddress;
    private String administratorName;
    private String appointmentDate;

    /**
     * Retrieves the unique identifier of the appointment.
     *
     * @return The appointment ID.
     */
    public int getId() { return id; }

    /**
     * Sets the unique identifier of the appointment.
     *
     * @param id The appointment ID to set.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Retrieves the address of the house for the appointment.
     *
     * @return The address of the house.
     */
    public String getHouseAddress() { return houseAddress; }

    /**
     * Sets the address of the house for the appointment.
     *
     * @param houseAddress The address of the house to set.
     */
    public void setHouseAddress(String houseAddress) { this.houseAddress = houseAddress; }

    /**
     * Retrieves the name of the house for the appointment.
     *
     * @return The name of the house.
     */
    public String getHouseName() { return houseName; }

    /**
     * Sets the name of the house for the appointment.
     *
     * @param houseName The name of the house to set.
     */
    public void setHouseName(String houseName) { this.houseName = houseName; }

    /**
     * Retrieves the name of the administrator managing the appointment.
     *
     * @return The name of the administrator.
     */
    public String getAdministratorName() { return administratorName; }

    /**
     * Sets the name of the administrator managing the appointment.
     *
     * @param administratorName The name of the administrator to set.
     */
    public void setAdministratorName(String administratorName) { this.administratorName = administratorName; }

    /**
     * Retrieves the date of the appointment.
     *
     * @return The appointment date.
     */
    public String getAppointmentDate() { return appointmentDate; }

    /**
     * Sets the date of the appointment.
     *
     * @param appointmentDate The appointment date to set.
     */
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
}