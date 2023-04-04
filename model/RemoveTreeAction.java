package model;

import java.sql.SQLException;
import java.util.Properties;
import javafx.scene.Scene;

import event.Event;
import userinterface.View;
import userinterface.ViewFactory;

public class RemoveTreeAction extends Action {

    private Tree myTree;
    
    public RemoveTreeAction() throws Exception {
        super();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Done", "CompleteAction");
        dependencies.setProperty("Cancel", "CancelAction");
        dependencies.setProperty("ConfirmRemove", "RemoveError");
        dependencies.setProperty("LookupTreeError", "LookupTreeError");
        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("RemoveTreeActionView");
        if(currentScene == null) {
            //create initial view
            View initialView = ViewFactory.createView("RemoveTreeActionView", this);
            currentScene = new Scene(initialView);
            myViews.put("RemoveTreeActionView", currentScene);
        }
        return currentScene;
    }

    public Object getState(String key) {
        switch(key) {
            default: 
                if(myTree != null) {
                    return myTree.getState(key);
                } else {
                    return null;
                }
        }
    }

    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "BarcodeSubmitted":
                lookupTree((String)value);
                break;
            case "ConfirmRemove":
                removeTree();
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void lookupTree(String barcode) {
        try {
            myTree = new Tree(barcode);
            createAndShowConfirmationView();
        } catch (Exception e) {
            System.out.println("Error creating tree object: " + e.toString());
            new Event(Event.getLeafLevelClassName(this), "removeTree", "Error creating tree object.", Event.ERROR);
            stateChangeRequest("LookupTreeError", "");
            return;
        } 
    }

    private void removeTree() {
        Boolean treeRemoved = myTree.delete() != 0;
        if(treeRemoved) {
            createAndShowDoneView();
        } else {
            // go back to submit barcode but show error message
            Scene intialView = createView();
            swapToView(intialView);
        }
    }

    private void createAndShowConfirmationView() {
        Scene scene = myViews.get("RemoveTreeConfirmationView");
        if(scene == null) {
            // create initially
            View view = ViewFactory.createView("RemoveTreeConfirmationView", this);
            scene = new Scene(view);
            myViews.put("RemoveTreeConfirmationView", scene);
        }
        swapToView(scene);
    }

    private void createAndShowDoneView() {
        Scene scene = myViews.get("RemoveTreeDoneView");
        if(scene == null) {
            View view = ViewFactory.createView("RemoveTreeDoneView", this);
            scene = new Scene(view);
            myViews.put("RemoveTreeDoneView", scene);
        }
        swapToView(scene);
    }


}
