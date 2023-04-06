package model;

import javafx.stage.Stage;
import java.util.Properties;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;

import userinterface.View;
import userinterface.ViewFactory;

public class AddTreeAction extends Action {

    private String addCompleteMessage = "";
    private String statusMessage = "";

    public AddTreeAction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        
        dependencies.setProperty("Done", "CompleteAction");
        dependencies.setProperty("Cancel", "CancelAction");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("AddTreeActionView");
        if(currentScene == null) {
            // create initial view
            View initialView = ViewFactory.createView("AddTreeActionView", this);
            currentScene = new Scene(initialView);
            myViews.put("AddTreeActionView", currentScene);
        }
        return currentScene;
    }

    public Object getState(String key) {
        switch(key) {
            case "AddComplete":
                return addCompleteMessage;
            case "StatusMessage":
                return statusMessage;
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob": 
                doYourJob();
                break;
            case "ProcessAddTree":
                processNewTree((Properties) value);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    public void processNewTree(Properties props){
        try {
            // make tree type from tree type table, get ID
            TreeType treeType = new TreeType(props.getProperty("TreeType"));
            String id = (String)treeType.getState("ID");
            //get the tree type id to access barcode prefix
            props.setProperty("TreeType", id);
        } catch (InvalidPrimaryKeyException e) {
            // update on gui
            statusMessage = "ERROR: Invalid barcode";
            stateChangeRequest("UpdateStatusMessage", "");
            return;
        }
        Tree tree;
        try {
            String barcode = props.getProperty("Barcode");
            tree = new Tree(barcode);
            statusMessage = "Tree with barcode " + barcode + " already exists";
            stateChangeRequest("UpdateStatusMessage", "");
            return;
        } catch (InvalidPrimaryKeyException e) {
            // error means success here, we should not get a tree back
            tree = new Tree(props);
        }

        try {
            tree.update();
        } catch(Exception e) {
            statusMessage = (String)tree.getState("UpdateStatusMessage");
            stateChangeRequest("UpdateStatusMessage", "");
        }

        addCompleteMessage = "Tree added successfully";
        stateChangeRequest("TreeAdded", "");
    }

}
