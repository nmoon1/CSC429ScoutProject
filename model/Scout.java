package model;

import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

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
		persistentState.setProperty("LastName", "");
		persistentState.setProperty("FirstName", "");
		persistentState.setProperty("MiddleName", "");
		persistentState.setProperty("DateOfBirth", "");
		persistentState.setProperty("PhoneNumber", "");
		persistentState.setProperty("Email", "");
		persistentState.setProperty("TroopID", "");
	}
	
	public Scout(Properties props)
	{
		super(myTableName);
		
		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements())
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
		}
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
		persistentState.clear();
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
	
	public void updateStatusDate(Date date)
	{
		persistentState.setProperty("DateStatusUpdated", date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
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
