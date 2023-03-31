package model;

import java.time.LocalDate;
import java.util.Properties;
import java.util.regex.Pattern;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class RegisterScoutAction extends Action {

	private static final String viewName = "RegisterScoutView";
	private String errorMessage = "";
	
	protected RegisterScoutAction() throws Exception {
		super();
		
	}

	@Override
	protected void setDependencies() { }

	@Override
	protected Scene createView() {
		Scene currentScene = (Scene)myViews.get(viewName);
			
		if (currentScene == null) {
			// create our initial view
			View newView = ViewFactory.createView(viewName, this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put(viewName, currentScene);
		}
		
		return currentScene;
	}

	@Override
	public Object getState(String key) {
		switch (key) {
			case "Error": return errorMessage;
		}
		
		return null;
	}

	@Override
	public void stateChangeRequest(String key, Object value) {
		switch (key) {
			case "DoYourJob":
				doYourJob();
				break;
			case "CancelAction":
				myRegistry.updateSubscribers(key, this);
				break;
			case "Submit":
				Properties scoutInfo = (Properties)value;
				errorMessage = Scout.validate(scoutInfo);
				if (errorMessage != null) return;
				errorMessage = "";
   		    	
   		    	// Check if a scout with the same troop ID exists yet
   		    	Scout tempScout = new Scout();
   		    	try {
   		    		tempScout.lookupAndStore("TroopID = " + scoutInfo.getProperty("TroopID"));
   		    		errorMessage = "Scout with troop ID \"" + scoutInfo.getProperty("TroopID") + "\" already exists";
   		    		return;
   		    	} catch (InvalidPrimaryKeyException ex) { }
   		    	
   		    	new Scout(scoutInfo).update();
				break;
		}
	}
	
}
