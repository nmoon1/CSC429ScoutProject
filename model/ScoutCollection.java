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
	
	public ScoutCollection()
	{
		super(myTableName);
		scouts = new Vector<Scout>();
	}
	
	public void lookupAll(String condition)
	{
		storeQueryResults("SELECT * FROM " + myTableName + " WHERE (" + condition + ")");
	}
	
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
