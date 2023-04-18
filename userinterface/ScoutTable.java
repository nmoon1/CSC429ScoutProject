package userinterface;

import javafx.beans.property.SimpleStringProperty;
import model.Scout;

public class ScoutTable {
	private final SimpleStringProperty scoutID, firstName, middleName, lastName, dateOfBirth, phoneNumber, email, troopID;

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

    public String getScoutID() {
        return scoutID.get();
    }

    public void getScoutID(String id) {
    	scoutID.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void getFirstName(String name) {
        firstName.set(name);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public void getMiddleName(String name) {
        middleName.set(name);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void getLastName(String name) {
        lastName.set(name);
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String dob) {
    	dateOfBirth.set(dob);
    }

    public String getPhoneNumber() {
    	String phone = phoneNumber.get();
        return phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
    }

    public void setPhoneNumber(String phone) {
        phoneNumber.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTroopID() {
        return troopID.get();
    }

    public void setTroopID(String troopID) {
        this.troopID.set(troopID);
    }
}
