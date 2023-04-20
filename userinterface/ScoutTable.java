package userinterface;

import javafx.beans.property.SimpleStringProperty;
import model.Scout;

public class ScoutTable {
	private final SimpleStringProperty scoutID, firstName, middleName, lastName, dateOfBirth, phoneNumber, email, troopID;

	/**
	 * Creates a new ScoutTable from the given Scout
	 * @param scout The Scout
	 */
    public ScoutTable(Scout scout) {
    	scoutID = new SimpleStringProperty((String)scout.getState("ID"));
    	firstName = new SimpleStringProperty((String)scout.getState("FirstName"));
    	middleName = new SimpleStringProperty((String)scout.getState("MiddleName"));
    	lastName = new SimpleStringProperty((String)scout.getState("LastName"));
    	dateOfBirth = new SimpleStringProperty((String)scout.getState("DateOfBirth"));
    	phoneNumber = new SimpleStringProperty(scout.getFormattedPhoneNumber());
    	email = new SimpleStringProperty((String)scout.getState("Email"));
    	troopID = new SimpleStringProperty((String)scout.getState("TroopID"));
    }

    /**
     * Gets the Scout's ID
     * @return The Scout's ID
     */
    public String getScoutID() {
        return scoutID.get();
    }

    /**
     * Sets the Scout's ID
     * @param id The Scout's ID
     */
    public void setScoutID(String id) {
    	scoutID.set(id);
    }

    /**
     * Gets the Scout's first name
     * @return The Scout's first name
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Sets the Scout's first name
     * @param name The Scout's first name
     */
    public void setFirstName(String name) {
        firstName.set(name);
    }

    /**
     * Gets the Scout's middle name
     * @return The Scout's middle name
     */
    public String getMiddleName() {
        return middleName.get();
    }

    /**
     * Sets the Scout's middle name
     * @param name The Scout's middle name
     */
    public void setMiddleName(String name) {
        middleName.set(name);
    }

    /**
     * Gets the Scout's last name
     * @return The Scout's last name
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Sets the Scout's last name
     * @param name The Scout's last name
     */
    public void setLastName(String name) {
        lastName.set(name);
    }

    /**
     * Gets the Scout's date of birth
     * @return The Scout's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    /**
     * Sets the Scout's date of birth
     * @param dob The Scout's date of birth
     */
    public void setDateOfBirth(String dob) {
    	dateOfBirth.set(dob);
    }

    /**
     * Gets the Scout's phone number
     * @return The Scout's phone number
     */
    public String getPhoneNumber() {
    	String phone = phoneNumber.get();
        return phone;
    }

    /**
     * Sets the Scout's phone number
     * @param phone The Scout's phone number
     */
    public void setPhoneNumber(String phone) {
        phoneNumber.set(phone);
    }

    /**
     * Gets the Scout's email
     * @return The Scout's email
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Sets the Scout's email
     * @param email The Scout's email
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * Gets the Scout's troop ID
     * @return The Scout's troop ID
     */
    public String getTroopID() {
        return troopID.get();
    }

    /**
     * Sets the Scout's troop ID
     * @param troopID The Scout's troop ID
     */
    public void setTroopID(String troopID) {
        this.troopID.set(troopID);
    }
}
