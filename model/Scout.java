package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import userinterface.MessageView;

public class Scout extends EntityBase implements IView
{
	private static final String myTableName = "Scout";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	protected MessageView statusLog;
	
	public Scout(String scoutId) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		lookupAndStore("ID = " + scoutId);
	}
	
	public Scout()
	{
		super(myTableName);
		
		setDependencies();
		persistentState = new Properties();
		persistentState.setProperty("FirstName", "");
		persistentState.setProperty("MiddleName", "");
		persistentState.setProperty("LastName", "");
		persistentState.setProperty("DateOfBirth", "");
		persistentState.setProperty("PhoneNumber", "");
		persistentState.setProperty("Email", "");
		persistentState.setProperty("TroopID", "");
		persistentState.setProperty("Status", "Active");
	}
	
	public Scout(Properties props)
	{
		super(myTableName);
		
		setDependencies();
		persistentState = new Properties();
		persistentState.setProperty("FirstName", "");
		persistentState.setProperty("MiddleName", "");
		persistentState.setProperty("LastName", "");
		persistentState.setProperty("DateOfBirth", "");
		persistentState.setProperty("PhoneNumber", "");
		persistentState.setProperty("Email", "");
		persistentState.setProperty("TroopID", "");
		persistentState.setProperty("Status", "Active");
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements())
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
		}
	}
	
	public static String validate(Properties scoutInfo)
	{
		String firstName = scoutInfo.getProperty("FirstName"); 
		if (firstName != null && firstName.length() == 0)
			return "First name cannot be empty";
		if (firstName.length() > 25) return "First name is too long";
		
		String middleName = scoutInfo.getProperty("MiddleName");
		if (middleName != null && middleName.length() > 25)
			return "Middle name is too long";
    	
		String lastName = scoutInfo.getProperty("LastName"); 
    	if (lastName != null && lastName.length() == 0)
    		return "Last name cannot be empty";
    	if (firstName.length() > 25) return "Last name is too long";
    	
    	String dateOfBirth = scoutInfo.getProperty("DateOfBirth");
    	if (dateOfBirth != null && dateOfBirth.length() == 0)
    		return "Date of birth cannot be empty";
    	
    	String phoneNumber = scoutInfo.getProperty("PhoneNumber");
    	if (phoneNumber != null && phoneNumber.length() == 0)
    		return "Phone number cannot be empty";
    	
    	String email = scoutInfo.getProperty("Email"); 
    	if (email != null && email.length() == 0)
    		return "Email cannot be empty";
    	
    	String troopID = scoutInfo.getProperty("TroopID");
    	if (!Pattern.matches("^\\d{5}$", troopID)) return "Troop ID must be of the form XXXXX";
    	
    	// Validate DOB format
    	if (!Pattern.matches("^\\d{4}\\-\\d{2}\\-\\d{2}$", dateOfBirth))
    		return "Enter date of birth as YYYY-MM-DD";
    	
    	// Check DOB ranges
    	LocalDate currentDate = LocalDate.now();
    	int month = Integer.parseInt(dateOfBirth.substring(5, 7));
    	if (month < 1 || month > 12) return "Birth month must be between 1 and 12";

    	int year = Integer.parseInt(dateOfBirth.substring(0, 4));
    	int daysInMonth = 0;
    	switch (month) {
    		case 1:
    		case 3:
    		case 5:
    		case 7:
    		case 8:
    		case 10:
    		case 12: daysInMonth = 31; break;
    		case 4:
    		case 6:
    		case 9:
    		case 11: daysInMonth = 30; break;
    		case 2: daysInMonth = (year % 4 == 0) ? 29 : 28; break;
    	}
    	int day = Integer.parseInt(dateOfBirth.substring(8));
    	if (day < 1 || day > daysInMonth) return "Birth day must be between 1 and " + daysInMonth;
    	
    	// Check if DOB exceeds the current date
    	if (year > currentDate.getYear() ||
    		(year == currentDate.getYear() &&
    		(month > currentDate.getMonthValue() ||
    		(month == currentDate.getMonthValue() &&
    		day > currentDate.getDayOfMonth()))))
    	{
    		return "Date of birth cannot be after current date";
    	}
    	
    	// Validate phone number
    	if (Pattern.matches("^\\d{3}\\-\\d{3}\\-\\d{4}$", phoneNumber))
    		scoutInfo.setProperty("PhoneNumber", phoneNumber.substring(0, 3) + phoneNumber.substring(4, 7) + phoneNumber.substring(8));
    	else if (Pattern.matches("^\\(\\d{3}\\)\\d{3}\\-\\d{4}$", phoneNumber))
    		scoutInfo.setProperty("PhoneNumber", phoneNumber.substring(1, 4) + phoneNumber.substring(5, 8) + phoneNumber.substring(9));
    	else if (!Pattern.matches("^\\d{10}$", phoneNumber))
    		return "Phone number must be of the form XXXXXXXXXX or (XXX)XXX-XXXX or XXX-XXX-XXXX";
    	
    	// Verify email
    	if (!email.contains("@")) return "Invalid email";
    	
    	return null;
	}
	
	public String getFullName()
	{
		return persistentState.getProperty("FirstName") + " " + persistentState.getProperty("LastName"); 
	}
	
	public void lookupAndStore(String condition) throws InvalidPrimaryKeyException
	{
		Vector<Properties> allDataRetrieved = getSelectQueryResult("SELECT * FROM " + myTableName + " WHERE (" + condition + ")");

		// You must get one scout at least
		if (allDataRetrieved == null) throw new InvalidPrimaryKeyException("No scout matching condition \"" + condition + "\" found.");
		
		// There should be EXACTLY one scout. More than that is an error
		if (allDataRetrieved.size() != 1) throw new InvalidPrimaryKeyException("Multiple scouts matching condition \"" + condition + "\" found.");
		
		// copy all the retrieved data into persistent state
		Properties retrievedScoutData = allDataRetrieved.elementAt(0);
		persistentState.clear();

		Enumeration allKeys = retrievedScoutData.propertyNames();
		while (allKeys.hasMoreElements())
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = retrievedScoutData.getProperty(nextKey);

			if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
		}
	}
	
	public void update() 
	{
		try
		{
			String id = persistentState.getProperty("ID"); 
			if (id != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("ID", id);
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Scout data for scout " + getFullName() + " updated successfully in database!";
				return;
			}
			
			id = insertAutoIncrementalPersistentState(mySchema, persistentState).toString();
			persistentState.setProperty("ID", id);
			updateStatusMessage = "Scout data for new scout " + getFullName() + "installed successfully in database!";
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing scout data in database!";
		}
	}
	
	public void setScout(Properties props)
	{
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements())
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
		}
	}
	
	public void setState(String key, String value)
	{
		persistentState.setProperty(key, value);
	}

	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage")) return updateStatusMessage;
		return persistentState.getProperty(key);
	}

	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}
	
	public void updateStatusDate()
	{
		updateStatusDate(LocalDate.now());
	}
	
	public void updateStatusDate(LocalDate date)
	{
		String year = date.getYear() + "";
		while (year.length() < 4) year = "0" + year;
		String month = date.getMonthValue() + "";
		if (month.length() < 2) month = "0" + month;
		String day = date.getDayOfMonth() + "";
		if (day.length() < 2) day = "0" + day;
		persistentState.setProperty("DateStatusUpdated", year + "-" + month + "-" + day);
	}

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null) mySchema = getSchemaInfo(tableName);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		myRegistry.setDependencies(dependencies);
	}

	public void updateState(String key, Object value)
	{
		clearErrorMessage();
	}
	
	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}
