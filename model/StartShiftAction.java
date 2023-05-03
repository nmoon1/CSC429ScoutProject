package model;

import java.util.Properties;
import javafx.scene.Scene;

import userinterface.View;
import userinterface.ViewFactory;

import java.util.Vector;

public class StartShiftAction extends Action {

    private ScoutCollection allScouts = new ScoutCollection();
    private Vector<Shift> shiftList = new Vector<Shift>();

    public StartShiftAction() throws Exception {
        super();

        allScouts.lookupAll("Status <> 'Inactive'");
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Done", "CompleteAction");
        dependencies.setProperty("Cancel", "CancelAction");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("StartShiftActionView");
        if (currentScene == null) {
            // create initial view
            View initialView = ViewFactory.createView("StartShiftActionView", this);
            currentScene = new Scene(initialView);
            myViews.put("StartShiftActionView", currentScene);
        }
        return currentScene;
    }

    public Object getState(String key) {
        if (key.equals("GetScouts")) {
            Vector<Scout> scoutList = (Vector<Scout>) allScouts.getState("Scouts");
            Vector<String> scoutInfoList = new Vector<String>();
            for (int i = 0; i < scoutList.size(); i++) {
                Scout curScout = scoutList.get(i);
                // String scoutID = (String)curScout.getState("ID");
                String scoutName = curScout.getFullName();
                String scoutTroop = (String) curScout.getState("TroopID");
                String scoutPhone = (String) curScout.getState("PhoneNumber");
                String formattedScoutPhone = scoutPhone.substring(0, 3) + "-" + scoutPhone.substring(3, 6) + "-"
                        + scoutPhone.substring(6);
                scoutInfoList.add(scoutName + " (" + scoutTroop + ") " + formattedScoutPhone);
            }
            return scoutInfoList;
        } else
            return null;
    }

    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "StartSession":
                startSession((Properties) value);
                break;
            case "AddShift":
                addShift((Properties) value);
                break;
            case "StartShift":
                startShift();
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void startSession(Properties props) {
        try {
            Session session = new Session(props);
            session.save();

        } catch (Exception e) {
            System.out.println("Error starting session: " + e.toString());
        }
    }

    private void addShift(Properties props) {
        try {
            Session se = new Session();

            String firstName = props.getProperty("FirstName");
            String lastName = props.getProperty("LastName");
            String troopID = props.getProperty("TroopID");
            String phone = props.getProperty("PhoneNumber");
            String phoneTokens [] = phone.split("-");
            phone = phoneTokens[0] + phoneTokens[1] + phoneTokens[2];
            Scout scout = new Scout();
            String condition = "FirstName=\"" + firstName +"\" AND LastName=\"" + lastName +"\" AND troopID=\"" + troopID + "\" AND PhoneNumber=\"" + phone + "\"";
            scout.lookupAndStore(condition);
            String scoutID = (String)scout.getState("ID");

            Properties shiftProps = new Properties();
            shiftProps.setProperty("ScoutID", scoutID);
            shiftProps.setProperty("SessionID", (String)se.getState("ID"));
            shiftProps.setProperty("CompanionName", props.getProperty("CompanionName"));
            shiftProps.setProperty("CompanionHours", props.getProperty("CompanionHours"));
            shiftProps.setProperty("StartTime", props.getProperty("StartTime"));
            shiftProps.setProperty("EndTime", props.getProperty("EndTime"));
            

            Shift shift = new Shift(shiftProps);
            shiftList.add(shift);
            // shift.save();

        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.toString());
        }
    }

    private void startShift() {
        try {
            for (int i = 0; i < shiftList.size(); i++) {
                Shift curShift = shiftList.get(i);
                curShift.save();
            }

            stateChangeRequest("CompleteAction", null);

        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.toString());
        }
    }
}
