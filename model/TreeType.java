package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class TreeType extends EntityBase implements IView {
    private static final String myTableName = "TreeType";
    protected Properties dependencies;
    String updateStatusMessage = "";

    public TreeType(int treeTypeID) throws InvalidPrimaryKeyException 
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE ID = " + treeTypeID;

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one tree type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one tree type. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple tree types matching id : "
                        + treeTypeID + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedAccountData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedAccountData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedAccountData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No tree type matching id : "
                    + treeTypeID + " found.");
        }
    }

    public TreeType(Properties props) 
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    public void update()
	{
		updateStateInDatabase();
	}

    private void updateStateInDatabase() 
    {
        try
        {
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID",
                        persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree type data for tree type id: " + persistentState.getProperty("ID") + " updated successfully in database!";
            }
            else
            {
                Integer treeTypeID =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + treeTypeID.intValue());
                updateStatusMessage = "Tree type data for new tree type: " +  persistentState.getProperty("ID")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing book data in database!";
        }
    }

    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    public static int compare(TreeType a, TreeType b)
	{
		String aID = (String)a.getState("ID");
		String bID = (String)b.getState("ID");

		return aID.compareTo(bID);
	}

    public String toString()
	{
		return "Type description: " + persistentState.getProperty("TypeDescription") + "; Cost: " + persistentState.getProperty("Cost") 
			+ "; Barcode prefix: " + persistentState.getProperty("BarcodePrefix");
	}

    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
