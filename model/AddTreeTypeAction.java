package model;

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Properties;

import userinterface.View;
import userinterface.ViewFactory;

public class AddTreeTypeAction extends Action {

	private String addMessage = "";

    public AddTreeTypeAction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("ProcessAddTreeType", "AddMessage");

        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = (Scene)myViews.get("AddTreeTypeActionView");

        if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("AddTreeTypeActionView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("AddTreeTypeActionView", currentScene);
		}
        return currentScene;
    }

    public Object getState(String key) {
        if (key.equals("AddMessage"))
		{
			return addMessage;
		}
        else
            return "";
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;    
            case "ProcessAddTreeType":
                processAddTreeType((Properties)value);
                break;
        }        
        myRegistry.updateSubscribers(key, this);
    }

    private void processAddTreeType(Properties props) {
        TreeType t = new TreeType(props);
        t.update();

        if(t.getState("UpdateStatusMessage").equals("")) {
            addMessage = "Tree Type inserted successfully!";
        }
        else if (((String)t.getState("UpdateStatusMessage")).contains("Duplicate entry")){
            addMessage = "Tree Type already exists for that Barcode Prefix.";
        }
        else {
            addMessage = "Error inserting into database.";
        }
    }
}
