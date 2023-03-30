package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import impresario.IView;

public class Shift extends EntityBase implements IView {
     
    private static final String myTableName = "Shift";
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public Shift(Properties props) {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true) {
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) {
				persistentState.setProperty(nextKey, nextValue);
			}
		}
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {
        if(key.equals("UpdateStatusMessage")) return updateStatusMessage;
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    protected void initializeSchema(String tableName) {
        if(mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public void save() {
        updateStateInDatabase();
    }

    private void updateStateInDatabase() {
        try {
            String shiftID = persistentState.getProperty("ID");
            boolean shiftExists = shiftID != null;
            if(shiftExists) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", shiftID);
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Data for shift " + shiftID + " saved successfully";
            } else {
                //insert
                Integer newShiftID = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + newShiftID.intValue());
                updateStatusMessage = "Data for new shift " + shiftID + " saved successfully";
            }
        } catch (SQLException e) {
            updateStatusMessage = "Error in saving shift data to the database.";
        }
    }
}
