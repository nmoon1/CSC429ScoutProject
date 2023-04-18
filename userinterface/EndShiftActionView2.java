//package model;
//
//import java.util.Properties;
//import javafx.scene.Scene;
//
//import userinterface.View;
//import userinterface.ViewFactory;
//
//public class EndShiftAction extends Action {
//
//    public EndShiftAction() throws Exception {
//        super();
//    }
//
//    protected void setDependencies() {
//        dependencies = new Properties();
//        dependencies.setProperty("Done", "CompleteAction");
//        dependencies.setProperty("Cancel", "CancelAction");
//        myRegistry.setDependencies(dependencies);
//    }
//
//    protected Scene createView() {
//        Scene currentScene = myViews.get("EndShiftActionView");
//        if(currentScene == null) {
//            // create initial view
//            View initialView = ViewFactory.createView("EndShiftActionView", this);
//            currentScene = new Scene(initialView);
//            myViews.put("EndShiftActionView", currentScene);
//        }
//        return currentScene;
//    }
//
//    public Object getState(String key) {
//        switch(key) {
//            default: return null;
//        }
//    }
//
//    public void stateChangeRequest(String key, Object value) {
//        switch(key) {
//            case "DoYourJob":
//                doYourJob();
//                break;
//            case "EndSession":
//                endSession((Properties)value);
//                break;
//        }
//        myRegistry.updateSubscribers(key, this);
//    }
//
//    public void endSession(Properties props) {
//        try {
//            Session session = new Session(props);
//            System.out.println(session);
//            session.save();
//        } catch(Exception e) {
//            System.out.println("Error ending session: " + e.toString());
//        }
//    }
//}
