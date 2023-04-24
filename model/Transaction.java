package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import userinterface.MessageView;

public class Transaction extends EntityBase implements IView
{
    private static final String myTableName = "Transaction";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";
    protected MessageView statusLog;

    public Transaction(String ID) throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        lookupAndStore("ID = " + ID);
    }

    public Transaction()
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        SetDefaultValues();
    }

    public Transaction(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        SetDefaultValues();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) persistentState.setProperty(nextKey, nextValue);
        }
    }

    private void SetDefaultValues()
    {
        persistentState.setProperty("SessionID", "");
        persistentState.setProperty("TransactionType", "");
        persistentState.setProperty("Barcode", "");
        persistentState.setProperty("TransactionAmount", "");
        persistentState.setProperty("PaymentMethod", "");
        persistentState.setProperty("CustomerName", "");
        persistentState.setProperty("CustomerEmail", "");
        persistentState.setProperty("TransactionDate", "");
        persistentState.setProperty("TransactionTime", "");
        persistentState.setProperty("DateStatusUpdated", "");
    }

    public void lookupAndStore(String condition) throws InvalidPrimaryKeyException
    {
        Vector<Properties> allDataRetrieved = getSelectQueryResult("SELECT * FROM " + myTableName + " WHERE (" + condition + ")");

        // You must get one transaction at least
        if (allDataRetrieved == null) throw new InvalidPrimaryKeyException("No transaction matching condition \"" + condition + "\" found.");

        // There should be EXACTLY one transaction. More than that is an error
        if (allDataRetrieved.size() != 1) throw new InvalidPrimaryKeyException("Multiple transactions matching condition \"" + condition + "\" found.");

        // copy all the retrieved data into persistent state
        Properties retrievedTransactionData = allDataRetrieved.elementAt(0);
        persistentState.clear();

        Enumeration allKeys = retrievedTransactionData.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = retrievedTransactionData.getProperty(nextKey);

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
                updateStatusMessage = "Transaction data for ID " + id + " updated successfully in database!";
                return;
            }

            id = insertAutoIncrementalPersistentState(mySchema, persistentState).toString();
            persistentState.setProperty("ID", id);
            updateStatusMessage = "Transaction data for ID " + id + "installed successfully in database!";
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing transaction data in database!";
        }
    }

    public void setProperties(Properties props)
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

    public static String dateToString(LocalDate date)
    {
        String year = date.getYear() + "";
        while (year.length() < 4) year = "0" + year;
        String month = date.getMonthValue() + "";
        if (month.length() < 2) month = "0" + month;
        String day = date.getDayOfMonth() + "";
        if (day.length() < 2) day = "0" + day;
        return year + "-" + month + "-" + day;
    }

    public void updateStatusDate(LocalDate date)
    {
        persistentState.setProperty("DateStatusUpdated", dateToString(date));
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