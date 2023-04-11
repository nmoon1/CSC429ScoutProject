package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IModel;
import impresario.IView;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class Tree extends EntityBase implements IView {

	private static final String myTableName = "Tree";
	protected Properties dependencies;
	private String updateStatusMessage = "";
	private String removeStatusMessage = "";
	private Boolean newTree = false;

	// Empty Contstructor
	// --------------------------------------------------------------------
	public Tree() {
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		persistentState.setProperty("Barcode", "");
		persistentState.setProperty("TreeType", "");
		persistentState.setProperty("Notes", "");
		persistentState.setProperty("DateStatusUpdated", "");
		persistentState.setProperty("Status", "Available");
	}

	// Constructor for new tree
	// ---------------------------------------------------------------------
	public Tree(Properties props) {
		super(myTableName);

		setDependencies();
		persistentState = new Properties();
		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true) {
			String nextKey = (String) allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) {
				persistentState.setProperty(nextKey, nextValue);
			}
		}
		newTree = true;
	}

	// Constructor for existing tree
	// ---------------------------------------------------------------------
	public Tree(String Barcode) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + Barcode + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one tree
		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			// There should be EXACTLY one tree. More than that is an error
			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
						+ Barcode + " found.");
			} else {
				// copy all the retrieved data into persistent state
				Properties retrievedTreeData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedTreeData.propertyNames();
				while (allKeys.hasMoreElements() == true) {
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedTreeData.getProperty(nextKey);
					// Barcode = Integer.parseInt(retrievedTreeData.getProperty("Barcode"));

					if (nextValue != null) {
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		}
		// If no tree found for this barcode, throw an exception
		else {
			throw new InvalidPrimaryKeyException("No tree matching barcode : "
					+ Barcode + " found.");
		}
	}

	// ---------------------------------------------------------------------
	private void processNewTree(Properties props) {
		props.forEach((key, value) -> {
			persistentState.setProperty((String) key, (String) value);
		});

		update();
	}

	// ---------------------------------------------------------------------
	private void setDependencies() {
		dependencies = new Properties();

		myRegistry.setDependencies(dependencies);
	}

	// ---------------------------------------------------------------------
	public Object getState(String key) {
		switch (key) {
			case "UpdateStatusMessage":
				return updateStatusMessage;
			case "RemoveStatusMessage":
				return removeStatusMessage;
			default:
				return persistentState.getProperty(key);
		}
	}

	// ---------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("ProcessTree")) {
			processNewTree((Properties) value);
		}

		myRegistry.updateSubscribers(key, this);
	}

	public void setState(String key, String value) {
		persistentState.setProperty(key, value);
	}

	// ---------------------------------------------------------------------
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}

	public void update() {
		updateStateInDatabase();
	}

	// add to the database
	// ---------------------------------------------------------------------
	private void updateStateInDatabase() {
		try {
			if (!newTree) {
				Properties whereClause = new Properties();
				whereClause.setProperty("Barcode",
						persistentState.getProperty("Barcode"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Tree data for barcode : " + persistentState.getProperty("Barcode")
						+ " updated successfully in database!";
			} else {
				insertPersistentState(mySchema, persistentState);
				updateStatusMessage = "Tree data for new tree : " + persistentState.getProperty("Barcode")
						+ " installed successfully in database!";
				newTree = false;
			}
		} catch (SQLException ex) {
			updateStatusMessage = "Error in installing tree data in database!";
		}
	}

	// delete from database
	public int delete() {
		Boolean treeSold = persistentState.getProperty("Status").equals("Sold");
		if (treeSold) {
			removeStatusMessage = "Cannot remove tree that is already sold.";
			return 0;
		}
		try {
			Properties whereClause = new Properties();
			whereClause.setProperty("Barcode", persistentState.getProperty("Barcode"));
			return deletePersistentState(mySchema, whereClause);
		} catch (SQLException e) {
			removeStatusMessage = "Error removing tree from the database!";
			return 0;
		}
	}

	// ---------------------------------------------------------------------
	public Vector<String> getEntryListView() {
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("TreeType"));
		v.addElement(persistentState.getProperty("Notes"));
		v.addElement(persistentState.getProperty("Status"));
		v.addElement(persistentState.getProperty("DateStatusUpdated"));

		return v;
	}

	// ---------------------------------------------------------------------
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

}
