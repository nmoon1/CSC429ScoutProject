package model;

import java.util.ArrayList;
import java.util.Properties;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class UpdateScoutAction extends Action {

	private static final String idViewName = "UpdateScoutIDView";
	private static final String listViewName = "UpdateScoutListView";
	private static final String infoViewName = "UpdateScoutInfoView";
	private String errorMessage = "";
	private ScoutCollection scoutCollection = null;
	private Scout selectedScout = null;
	
	protected UpdateScoutAction() throws Exception {
		super();
	}

	@Override
	protected void setDependencies() { }

	@Override
	protected Scene createView() {
		return getOrCreateScene(idViewName);
	}

	@Override
	public Object getState(String key) {
		switch (key) {
			case "Error": return errorMessage;
			case "Stage": return myStage;
			case "ScoutList": return scoutCollection;
			case "Scout": return selectedScout;
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
			case "BackSearch":
				swapToView(getOrCreateScene(idViewName));
				break;
			case "BackInfo":
				swapToView(getOrCreateScene(listViewName));
				break;
			case "Select":
				{
					errorMessage = "";
					String id = (String)value;
					for (Scout scout : scoutCollection.scouts) {
						if (scout.getState("ID").equals(id)) {
							selectedScout = scout;
							swapToView(getOrCreateScene(infoViewName));
							return;
						}
					}
					errorMessage = "No scout with ID matching \"" + id + "\"";
				}
				break;
			case "Search":
				{
					Properties scoutInfo = (Properties)value;
					ArrayList<String> keys = new ArrayList<String>();
					ArrayList<String> values = new ArrayList<String>();
					for (String propKey : new String[] { "ID", "FirstName", "MiddleName", "LastName", "DateOfBirth", "PhoneNumber", "Email", "TroopID" }) {
						String propValue = scoutInfo.getProperty(propKey); 
						if (propValue.length() != 0) {
							keys.add(propKey);
							values.add(propValue);
						}
					}
					
					if (keys.isEmpty()) {
						// No information entered
						errorMessage = "Fill at least one field";
						return;
					}
					
					// Construct SQL condition string
					String condition = "";
					boolean first = true;
					for (int i = 0; i < keys.size(); i++) {
						if (!first) condition += " AND ";
						first = false;
						String key2 = keys.get(i);
						condition += key2 + " = ";
						if (key2.equals("ID")) condition += values.get(i);
						else condition += "'" + values.get(i) + "'";
					}
					
					// Retrieve scouts from database
					scoutCollection = new ScoutCollection();
					scoutCollection.lookupAll(condition);
					
					if (scoutCollection.scouts.isEmpty()) {
						// No scouts matching given criteria
						errorMessage = "No scouts found matching search criteria \"" + condition + "\"";
						return;
					}
					
					swapToView(getOrCreateScene(listViewName));
				}
				break;
			case "Update":
				{
					Properties scoutInfo = (Properties)value;
					scoutInfo.setProperty("ID", (String)selectedScout.getState("ID"));
					errorMessage = Scout.validate(scoutInfo);
					if (errorMessage != null) return;
					errorMessage = "";
	   		    	
	   		    	// Check if a scout with the same troop ID exists yet
	   		    	Scout tempScout = new Scout();
	   		    	try {
	   		    		tempScout.lookupAndStore("TroopID = '" + scoutInfo.getProperty("TroopID") + "' AND ID <> " + scoutInfo.getProperty("ID"));
	   		    		errorMessage = "Scout with troop ID \"" + scoutInfo.getProperty("TroopID") + "\" already exists";
	   		    		return;
	   		    	} catch (InvalidPrimaryKeyException ex) { }
	   		    	
	   		    	selectedScout.setScout(scoutInfo);
	   		    	selectedScout.update();
				}
				break;
		}
	}
	
}