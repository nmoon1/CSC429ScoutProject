package model;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class RegisterScoutAction extends Action {

	private static final String viewName = "RegisterScoutView";
	private String errorMessage = "";
	
	protected RegisterScoutAction() throws Exception {
		super();
		
	}

	@Override
	protected void setDependencies() { }

	@Override
	protected Scene createView() {
		Scene currentScene = (Scene)myViews.get(viewName);
			
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView(viewName, this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put(viewName, currentScene);
		}
		
		return currentScene;
	}

	@Override
	public Object getState(String key) {
		switch (key) {
			case "Error": return errorMessage;
		}
		
		return null;
	}

	@Override
	public void stateChangeRequest(String key, Object value) {
		switch (key) {
			case "DoYourJob":
				doYourJob();
				break;
			case "Submit":
				errorMessage = "";
				Properties scoutInfo = (Properties)value;
				if (scoutInfo.getProperty("FirstName").length() == 0) {
					errorMessage = "First name cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	if (scoutInfo.getProperty("LastName").length() == 0) {
   		    		errorMessage = "Last name cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	String dateOfBirth = scoutInfo.getProperty("DateOfBirth");
   		    	if (dateOfBirth.length() == 0) {
   		    		errorMessage = "Date of birth cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	String phoneNumber = scoutInfo.getProperty("PhoneNumber");
   		    	if (phoneNumber.length() == 0) {
   		    		errorMessage = "Phone number cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	if (scoutInfo.getProperty("Email").length() == 0) {
   		    		errorMessage = "Email cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	String troopID = scoutInfo.getProperty("TroopID");
   		    	if (troopID.length() == 0) {
   		    		errorMessage = "Troop ID cannot be empty";
   		    		return;
   		    	}
   		    	
   		    	// Validate DOB format
   		    	if (!Pattern.matches("^\\d{4}\\-\\d{2}\\-\\d{2}$", dateOfBirth)) {
   		    		errorMessage = "Enter date of birth as YYYY-MM-DD";
   		    		return;
   		    	}
   		    	
   		    	// Check DOB ranges
   		    	LocalDate currentDate = LocalDate.now();
   		    	int month = Integer.parseInt(dateOfBirth.substring(5, 7));
   		    	if (month < 1 || month > 12) {
   		    		errorMessage = "Birth month must be between 1 and 12";
   		    		return;
   		    	}

   		    	int day = Integer.parseInt(dateOfBirth.substring(8));
   		    	if (day < 1 || day > 31) {
   		    		errorMessage = "Birth day must be between 1 and 31";
   		    		return;
   		    	}
   		    	
   		    	// Check if DOB exceeds the current date
   		    	int year = Integer.parseInt(dateOfBirth.substring(0, 4));
   		    	if (year > currentDate.getYear() ||
   		    		(year == currentDate.getYear() &&
   		    		(month > currentDate.getMonthValue() ||
   		    		(month == currentDate.getMonthValue() &&
   		    		day > currentDate.getDayOfMonth()))))
   		    	{
   		    		errorMessage = "Date of birth cannot be after current date";
   		    		return;
   		    	}
   		    	
   		    	// Validate phone number
   		    	if (Pattern.matches("^\\d{3}\\-\\d{3}\\-\\d{4}$", phoneNumber))
   		    		scoutInfo.setProperty("PhoneNumber", phoneNumber.substring(0, 3) + phoneNumber.substring(4, 7) + phoneNumber.substring(8));
   		    	else if (Pattern.matches("^\\(\\d{3}\\)\\d{3}\\-\\d{4}$", phoneNumber))
   		    		scoutInfo.setProperty("PhoneNumber", phoneNumber.substring(1, 4) + phoneNumber.substring(5, 8) + phoneNumber.substring(9));
   		    	else if (!Pattern.matches("^\\d{10}$", phoneNumber)) {
   		    		errorMessage = "Phone number must be of the form XXXXXXXXXX or (XXX)XXX-XXXX or XXX-XXX-XXXX";
   		    		return;
   		    	}
   		    	
   		    	// Check if a scout with the same troop ID exists yet
   		    	Scout tempScout = new Scout();
   		    	try {
   		    		tempScout.lookupAndStore("TroopID = " + troopID);
   		    		errorMessage = "Scout with troop ID \"" + troopID + "\" already exists";
   		    		return;
   		    	} catch (InvalidPrimaryKeyException ex) { }
   		    	
   		    	new Scout(scoutInfo).update();
				break;
		}
	}
	
}
