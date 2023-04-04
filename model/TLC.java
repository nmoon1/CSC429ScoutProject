package model;

// TLC -> Tree Lot Coordinator is the main user agent for the application

//system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

//project imports
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;


public class TLC implements IView, IModel {
    // for impressario
    private Properties dependencies;
	private ModelRegistry myRegistry;

    // GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage myStage;

    // model components - this will be the "state"

    public TLC() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        myRegistry = new ModelRegistry("TLC");
        if(myRegistry == null) {
            new Event(Event.getLeafLevelClassName(this), "TLC",
            "Could not instantiate Registry", Event.ERROR);
        }

        setDependencies();
        createAndShowTLCView();
    }

    private void setDependencies() {
        dependencies = new Properties();
        //TODO: set props
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowTLCView() {
        Scene currentScene = (Scene)myViews.get("TLCView");
        if(currentScene == null) {
            // create initial view
            View initialView = ViewFactory.createView("TLCView", this);
            currentScene = new Scene(initialView);
            myViews.put("TLCView", currentScene);
        }

        swapToView(currentScene);
    }

    /**
     * Called by the client to get the value of some column in the database.
     * 
     * @param key Name of the column for which the client wants the value.
     * 
     * @return Value associated with the requested key or an empty string if no such key exists.
     */
    public Object getState(String key) {
        switch(key) {

            default: return "";
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "RegisterScout": 
            case "UpdateScout": 
            case "RemoveScout": 
            case "AddTree": 
            case "UpdateTree": 
            case "RemoveTree": 
            case "AddTreeType": 
            case "UpdateTreeType":
            case "StartShift":
            case "EndShift":
            case "SellTree":
                doAction(key);
                break;
            case "CompleteAction":
            case "CancelAction":         
                createAndShowTLCView();
                break;   
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    /** Register objects to recieve state updates. */
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    /** Unregister objects from recieving state updates */
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    public void swapToView(Scene newScene) {
        if (newScene == null) {
			System.out.println("TLC.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}
        myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage);
    }

    public void doAction(String actionType) {
        try {
            Action action = ActionFactory.createAction(actionType);
            action.subscribe("CompleteAction", this);
            action.subscribe("CancelAction", this);
            action.stateChangeRequest("DoYourJob", "");
        } catch(Exception e) {
            new Event(Event.getLeafLevelClassName(this), "createAction", "Unrecognized action: " + actionType + "." + e.toString(), Event.ERROR);
        }
    }
}
