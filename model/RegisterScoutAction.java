package model;

import java.util.Properties;
import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;

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
		return getOrCreateScene(viewName);
	}

	@Override
	public Object getState(String key) {
		switch (key) {
			case "Error": return errorMessage;
			case "Stage": return myStage;
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
   		    		tempScout.lookupAndStore("TroopID = '" + scoutInfo.getProperty("TroopID") + "'");
   		    		errorMessage = "Scout with troop ID \"" + scoutInfo.getProperty("TroopID") + "\" already exists";
   		    		return;
   		    	} catch (InvalidPrimaryKeyException ex) { }
   		    	
   		    	Scout scout = new Scout(scoutInfo);
   		    	scout.updateStatusDate();
   		    	scout.update();
				break;
		}
	}
}