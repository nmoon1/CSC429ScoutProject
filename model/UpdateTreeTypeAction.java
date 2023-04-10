package model;

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Properties;

import userinterface.View;
import userinterface.ViewFactory;

public class UpdateTreeTypeAction extends Action{

    private TreeType selectedTreeType;
    
    private String selectMessage = "";
    private String updateMessage = "";

    public UpdateTreeTypeAction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("RetrieveTreeType", "SelectMessage");
        dependencies.setProperty("ProcessUpdateTreeType", "UpdateMessage");

        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = (Scene)myViews.get("SelectTreeTypeActionView");

        if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SelectTreeTypeActionView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("SelectTreeTypeActionView", currentScene);
		}
        return currentScene;
    }
    
    public Object getState(String key) {
        switch(key) {
            case "SelectedTreeType": return selectedTreeType;
            case "UpdateMessage": return updateMessage;
            case "SelectMessage": return selectMessage;
            default: return "";
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;    
            case "RetrieveTreeType":
                retrieveTreeType((String)value);
                break;
            case "ProcessUpdateTreeType":
                processUpdateTreeType((Properties)value);
                break;
        }        
        myRegistry.updateSubscribers(key, this);
    }

    private void retrieveTreeType(String barcodePrefix){
        try {
            selectedTreeType = new TreeType(barcodePrefix);
            if(myViews.get("UpdateTreeTypeActionView") != null)
		    {
			    myViews.remove("UpdateTreeTypeActionView");
		    }

            View newView = ViewFactory.createView("UpdateTreeTypeActionView", this);
		    Scene newScene = new Scene(newView);
		    myViews.put("UpdateTreeTypeActionView", newScene);
            Scene currentScene = (Scene)myViews.get("UpdateTreeTypeActionView");

            // make the view visible by installing it into the frame
		    swapToView(newScene);
        }
        catch(Exception e) {
            selectMessage = "No Tree Type has that Barcode Prefix.";
        }
    }

    private void processUpdateTreeType(Properties props) {
        TreeType t = new TreeType(props);
        t.update();
        
        if(t.getState("UpdateStatusMessage").equals("")) {
            updateMessage = "Tree Type updated successfully!";
        }
        else {
            updateMessage = "Error updating in database.";
        }
    }
}