package model;

import java.util.Properties;
import javafx.scene.Scene;

import userinterface.View;
import userinterface.ViewFactory;

public class StartShiftAction extends Action {

    protected ScoutCollection allScouts = new ScoutCollection();

    public StartShiftAction() throws Exception {
        super();

        allScouts.lookupAll();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Done", "CompleteAction");
        dependencies.setProperty("Cancel", "CancelAction");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("StartShiftActionView");
        if(currentScene == null) {
            // create initial view
            View initialView = ViewFactory.createView("StartShiftActionView", this);
            currentScene = new Scene(initialView);
            myViews.put("StartShiftActionView", currentScene);
        }
        return currentScene;
    }

    public Object getState(String key) {
        if (key.equals("GetScouts"))
		{
			return allScouts;
		}
        else
            return null;
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob": 
                doYourJob();
                break;
            case "StartSession":
                startSession((Properties)value);
                break;
            case "AddShift":
                addShift((Properties)value);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void startSession(Properties props) {
        try {
            Session session = new Session(props);
            session.save();

        } catch(Exception e) {
            System.out.println("Error starting session: " + e.toString());
        }
    }

    private void addShift(Properties props) {
        try {
            Session se = new Session();
            props.setProperty("SessionID", (String)se.getState("ID"));
            
            Shift shift = new Shift(props);
            shift.save();
            
        } catch(Exception e) {
            System.out.println("Error adding shift: " + e.toString());
        }
    }
}
