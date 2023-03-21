package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;

public class Scout extends EntityBase {
	private static final String myTableName = "Scout";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	
	public Scout(String scoutId) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + scoutId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one scout at least
		if (allDataRetrieved == null) throw new InvalidPrimaryKeyException("No scout matching id : " + scoutId + " found.");
		
		// There should be EXACTLY one scout. More than that is an error
		if (allDataRetrieved.size() != 1) throw new InvalidPrimaryKeyException("Multiple scouts matching id : " + scoutId + " found.");
		
		// copy all the retrieved data into persistent state
		Properties retrievedScoutData = allDataRetrieved.elementAt(0);
		persistentState = new Properties();

		Enumeration allKeys = retrievedScoutData.propertyNames();
		while (allKeys.hasMoreElements())
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = retrievedScoutData.getProperty(nextKey);

			if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
		}
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
	
	public Scout(String key, String match) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (" + key + " = " + match + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one scout at least
		if (allDataRetrieved == null) throw new InvalidPrimaryKeyException("No scout matching " + key + " : " + match + " found.");
		
		// There should be EXACTLY one scout. More than that is an error
		if (allDataRetrieved.size() != 1) throw new InvalidPrimaryKeyException("Multiple scouts matching " + key + " : " + match + " found.");
		
		// copy all the retrieved data into persistent state
		Properties retrievedScoutData = allDataRetrieved.elementAt(0);
		persistentState = new Properties();

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
			if (persistentState.getProperty("ID") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("ID",
				persistentState.getProperty("ID"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Scout data for scout ID : " + persistentState.getProperty("ID") + " updated successfully in database!";
			}
			else
			{
				Integer bookNumber = insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("ID", "" + bookNumber.intValue());
				updateStatusMessage = "Scout data for new scout : " +  persistentState.getProperty("ID") + "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing scout data in database!";
		}
	}

	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage")) return updateStatusMessage;
		return persistentState.getProperty(key);
	}

	public void stateChangeRequest(String key, Object value) {
		myRegistry.updateSubscribers(key, this);
	}

	protected void initializeSchema(String tableName) {
		if (mySchema == null) mySchema = getSchemaInfo(tableName);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		myRegistry.setDependencies(dependencies);
	}
}
