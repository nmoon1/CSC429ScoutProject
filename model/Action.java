package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import event.Event;

import impresario.*;

import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

abstract public class Action implements IView, IModel {
 
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected Stage myStage;
    protected Hashtable<String, Scene> myViews;

    protected Action() throws Exception {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
        myRegistry = new ModelRegistry("Action");
        if(myRegistry == null) {
            new Event(Event.getLeafLevelClassName(this), "Action", "Could not instantiate registry.", Event.ERROR);
        }
        setDependencies();
    }
    
    protected Scene getOrCreateScene(String viewName) {
		Scene scene = (Scene)myViews.get(viewName);
		
		if (scene == null) {
			// create our initial view
			View view = ViewFactory.createView(viewName, this); // USE VIEW FACTORY
			scene = new Scene(view);
			myViews.put(viewName, scene);
		}
		
		return scene;
	}

    protected abstract void setDependencies();

    protected abstract Scene createView();

    public abstract Object getState(String key);

    public abstract void stateChangeRequest(String key, Object value);

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    protected void doYourJob() {
        try {
        	Scene newScene = createView();
            swapToView(newScene);
        } catch (Exception e) {
            new Event(Event.getLeafLevelClassName(this), "doYourJob", "Could not create new scene", Event.ERROR);
        }
    }

    public void swapToView(Scene newScene) {
        if (newScene == null) {
            System.out.println("Action.swapToView(): no view to display.");
            new Event(Event.getLeafLevelClassName(this), "swapToView", "Missing view to display", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();
        WindowPosition.placeCenter(myStage);
    }
}
