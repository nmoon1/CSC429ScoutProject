package model;

import java.util.Properties;
import javafx.scene.Scene;

import userinterface.View;
import userinterface.ViewFactory;

public class StartShiftAction extends Action {

    public StartShiftAction() throws Exception {
        super();
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
        switch(key) {
            default: return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob": 
                doYourJob();
                break;
            case "StartSession":
                startSession((Properties)value);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    public void startSession(Properties props) {
        try {
            Session session = new Session(props);
            System.out.println(session);
            session.save();
        } catch(Exception e) {
            System.out.println("Error starting session: " + e.toString());
        }
    }
}
