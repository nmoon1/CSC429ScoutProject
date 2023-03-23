package model;

import impresario.IModel;
import impresario.IView;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class Tree extends EntityBase implements IModel {
    // Tree can update, remove, and add a tree
    // Tree can update and add a tree type

    private static final String myTableName = "Tree";
    protected Properties dependancies;

    private String updateStatusMessage = "";

    // empty contrsuctor
    // ---------------------------------------------------------------------
    public Tree() {
        super(myTableName);

        persistantState = new Properties();
    }

    // constructor for existing tree
    // ---------------------------------------------------------------------
    public Tree(int treeBARCODE) throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (treeBARCODE = " + treeBARCODE + ")";

        // must get a tree
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be only one tree. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
                        + treeBARCODE + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no tree found for this barcode, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No tree matching barcode : "
                    + treeBARCODE + " found.");
        }

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
    }
}
