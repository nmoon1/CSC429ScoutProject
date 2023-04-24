package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import impresario.IView;


public class Session extends EntityBase implements IView {

    private static final String myTableName = "Session";
    protected Properties dependencies;
    private String updateStatusMessage = "";

    // empty constructor, used to instantiate and lookup open session

    public Session() {
        super(myTableName);
        setDependencies();
        findOpenSession();
    }

    public Session(Properties props) {
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

    private void findOpenSession() {
        String query = "SELECT * FROM " + myTableName + " WHERE EndTime IS NULL";

        Vector<Properties> allData = getSelectQueryResult(query);
        // TODO: might need to throw error if multiple entries found
        if(allData == null || allData.size() != 1)
            return;
        Properties data = allData.elementAt(0);
        persistentState = new Properties();
        Enumeration keys = data.propertyNames();
        while(keys.hasMoreElements()) {
            String nextKey = (String)keys.nextElement();
            String value = data.getProperty(nextKey);
            if(value != null) {
                persistentState.setProperty(nextKey, value);
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
        switch(key) {
            default:
                break;
        }
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

    public void save(Properties props) {
        Enumeration keys = props.propertyNames();
        while(keys.hasMoreElements()) {
            String nextKey = (String)keys.nextElement();
            String nextVal = props.getProperty(nextKey);
            persistentState.setProperty(nextKey, nextVal); // don't think we need a null check here
        }
        updateStateInDatabase();
    }

    public void save() {
        updateStateInDatabase();
    }

    private void updateStateInDatabase() {
        try {
            String sessionID = persistentState.getProperty("ID");
            boolean sessionExists = sessionID != null;
            if(sessionExists) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("ID", sessionID);
                updatePersistentState(mySchema, persistentState, whereClause);
                String startDate = (String)getState("StartDate");
                String startTime = (String)getState("StartTime");
                updateStatusMessage = "Data for session starting on " + startDate + " at " + startTime + "saved successfully";
            } else {
                // insert
                Integer newSessionID = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + newSessionID.intValue());
                String startDate = (String)getState("StartDate");
                String startTime = (String)getState("StartTime");
                updateStatusMessage = "Data for new session starting on " + startDate + " at " + startTime + "saved successfully";
            }
        } catch (SQLException e) {
            updateStatusMessage = "Error in saving session data to the database.";
        }
    }

    // public String stringify() {
    //     String str = "";
    //     String id = persistentState.getProperty("ID");
    //     String startDate = persistentState.getProperty("StartDate");
    //     String startTime = persistentState.getProperty("StartTime");
    //     String endTime = persistentState.getProperty("EndTime");
    //     String startCash = persistentState.
    //     return str;
    // }

}