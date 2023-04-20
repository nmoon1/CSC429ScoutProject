package model;

import java.util.Properties;
import java.util.Vector;

import impresario.IView;
import userinterface.MessageView;

public class ScoutCollection extends EntityBase implements IView
{
	private static final String myTableName = "Scout";
	public Vector<Scout> scouts;
	protected MessageView statusLog;
	
	/**
	 * Creates an empty ScoutCollection
	 */
	public ScoutCollection()
	{
		super(myTableName);
		scouts = new Vector<Scout>();
	}
	
	/**
	 * Queries the database and stores all matching Scouts
	 * @param condition The WHERE condition of the SQL query to be sent to the database 
	 */
	public void lookupAll(String condition)
	{
		storeQueryResults("SELECT * FROM " + myTableName + " WHERE (" + condition + ")");
	}
	
	/**
	 * Retrieves and stores all Scouts in the database
	 */
	public void lookupAll()
	{
		storeQueryResults("SELECT * FROM " + myTableName);
	}
	
	public Object getState(String key)
	{
		switch (key)
		{
			case "Scouts": return scouts;
			case "ScoutList": return this;
			default: return null;
		}
	}

	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}
	
	/**
	 * Searches the ScoutCollection for a Scout with a property of name "key" matching "value" and returns the first match or null if none are found
	 * @param key The property name to check
	 * @param value The value the property must match
	 * @return The first Scout matching the key and value or null if none are found
	 */
	public Scout getScoutByInfo(String key, String value)
	{
		for (int i = 0; i < scouts.size(); i++)
		{
			Scout scout = scouts.get(i);
			Object state = scout.getState(key); 
			if (state != null && ((String)state).equals(value))
				return scout; 
		}
		
		return null;
	}

	protected void initializeSchema(String tableName)
	{
		if (mySchema == null) mySchema = getSchemaInfo(tableName);
	}
	
	/**
	 * Sends an SQL query to the database and stores all results
	 * @param query The full SQL query to send to the database
	 */
	private void storeQueryResults(String query)
	{
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved == null) return;
		scouts = new Vector<Scout>();

		for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
		{
			Properties nextScoutData = (Properties)allDataRetrieved.elementAt(cnt);
			Scout scout = new Scout(nextScoutData);
			if (scout != null) scouts.add(scout);
		}
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
