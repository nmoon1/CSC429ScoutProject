package model;

import javafx.stage.Stage;
import java.util.Properties;
import javafx.scene.Scene;

import userinterface.View;
import userinterface.ViewFactory;

public class AddTreeAction extends Action {

    private String addCompleteMessage = "";

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
            case "StartSession":
                startSession((Properties)value);
                break;
            case "ProcessAddTree":
                processNewTree((Properties) value);
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

    public void processNewTree(Properties props){
        
       Tree tree = new Tree(props);
       tree.update();
       addCompleteMessage = "Tree added!";
    }

}
