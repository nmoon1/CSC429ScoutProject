package model;

import java.sql.SQLException;
import java.util.Properties;
import javafx.scene.Scene;
import java.util.Properties;

import event.Event;
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;

public class UpdateTreeAction extends Action {

    private Tree myTree;
    
    public UpdateTreeAction() throws Exception{
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Cancel", "CancelAction");

        myRegistry.setDependencies(dependencies);
        
    }

    public Object getState(String key) {
        switch(key) {
            default: 
                if(myTree != null) {
                    return myTree.getState(key);
                } else {
                    return null;
                }
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "BarcodeSubmitted":
                lookupTree((String)value);
                break;
            case "ChangesSubmitted":
                updateTree((Properties)value);
                break;

        }
        myRegistry.updateSubscribers(key, this);
        
    }

    private void lookupTree(String barcode) {
        try {
            myTree = new Tree(barcode);
            if(myTree.getState("Status").equals("Sold")) {
                stateChangeRequest("TreeSoldError", "");
                return;
            }
            Scene newScene = getOrCreateScene("UpdateTreeEditView");
            swapToView(newScene);
        } catch(InvalidPrimaryKeyException e) {
            stateChangeRequest("LookupTreeError", "");
        }
    }

    private void updateTree(Properties props) {
        myTree.setState("Notes", props.getProperty("Notes"));
        String oldStatus = (String)myTree.getState("Status");
        String newStatus = props.getProperty("Status");
        if(!newStatus.equals(oldStatus)){
            myTree.setState("Status", newStatus);
            myTree.setState("DateStatusUpdated", java.time.LocalDate.now().toString());
        }
        myTree.update();

        String updateStatusMessage = (String)myTree.getState("UpdateStatusMessage");
        if(updateStatusMessage.indexOf("Error") == -1) {
            // no error
            stateChangeRequest("TreeUpdated", "");
            Scene newScene = getOrCreateScene("UpdateTreeActionView");
            swapToView(newScene);
        } else {
            stateChangeRequest("TreeUpdateError", "");
        }
    }

    protected Scene createView() {
        return getOrCreateScene("UpdateTreeActionView");
    }
}
