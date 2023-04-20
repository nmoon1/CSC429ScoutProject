package userinterface;

// system imports

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;

import java.util.Properties;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.application.Platform;

public class EndShiftActionView extends View{

    private MessageView statusLog;
    private Button confirm;
    private Button done;
    private Button cancel;
    private TextArea notes;

    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");
        VBox container = createFormContents();
        getChildren().add(container);
        myModel.subscribe("NotesError", this);
        myModel.subscribe("ShiftEnded", this);
    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        String endingCash = (String)myModel.getState("EndingCash");
        String totalCheckSales = (String)myModel.getState("TotalCheckSales");
        Text cash = new Text("Ending Cash: " + endingCash);
        Text check = new Text("Total Check Sales: " + totalCheckSales);

        Text notesLabel = new Text("Please enter any notes about the shift: ");
        notes = new TextArea();

        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);

        cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });
        cancel.managedProperty().bind(cancel.visibleProperty());

        confirm = new Button("Confirm");
        confirm.setOnAction( action -> {
            myModel.stateChangeRequest("Confirm", notes.getText());
        });
        confirm.managedProperty().bind(confirm.visibleProperty());

        done = new Button("Done");
        done.setOnAction(action -> {
            myModel.stateChangeRequest("Done", "");
        });
        done.managedProperty().bind(done.visibleProperty());
        done.setVisible(false);
        
        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(confirm);
        btnContainer.getChildren().add(done);

        statusLog = new MessageView("");

        container.getChildren().add(cash);
        container.getChildren().add(check);
        container.getChildren().add(notesLabel);
        container.getChildren().add(notes);
        container.getChildren().add(btnContainer);
        container.getChildren().add(statusLog);
        return container;
    }

    private void displayEndShift() {
        statusLog.displayMessage("Shift ended.");
        cancel.setVisible(false);
        confirm.setVisible(false);
        done.setVisible(true);
        notes.setDisable(true);
    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "NotesError":
                statusLog.displayErrorMessage("Notes msut be less than 500 characters.");
                break;
            case "ShiftEnded":
                displayEndShift();
                break;
        }
        
    }
    
}
