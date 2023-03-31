package model;

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Properties;

import userinterface.View;
import userinterface.ViewFactory;

public class AddTreeTypeAction extends Action {

	private String addCompleteMessage = "";

    public AddTreeTypeAction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("ProcessAddTreeType", "AddComplete");

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
        if (key.equals("AddComplete"))
		{
			return addCompleteMessage;
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
        addCompleteMessage = "Tree Type inserted successfully!";
    }
}
