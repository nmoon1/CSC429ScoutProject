package userinterface;

// system imports
import javafx.event.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javafx.stage.Stage;

import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

public class UpdateTreeEditView extends View {

    private ComboBox statusInput;
    private TextArea notesInput;
    private MessageView statusLog;
    
    public UpdateTreeEditView(IModel model) {
        super(model, "UpdateTreeEditView");

        VBox container = createFormContents();
        getChildren().add(container);
        populateFields();
        myModel.subscribe("TreeUpdateError", this);
    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        Label notesLabel = new Label("Notes: ");
        Label statusLabel = new Label("Status: ");
        ObservableList<String> options = FXCollections.observableArrayList(
            "Available",
            "Sold",
            "Damaged"
        );
        statusInput = new ComboBox<String>(options);

        notesInput = new TextArea();
        Button cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });

        Button submit = new Button("Submit");
        submit.setOnAction( action -> {
            submitChanges();
        });

        HBox btnContainer = new HBox(10);
        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(submit);

        statusLog = new MessageView("");

        container.getChildren().add(statusLabel);
        container.getChildren().add(statusInput);
        container.getChildren().add(notesLabel);
        container.getChildren().add(notesInput);
        container.getChildren().add(btnContainer);
        container.getChildren().add(statusLog);

        return container;
    }

    private void populateFields() {
        String notes = (String)myModel.getState("Notes");
        String status = (String)myModel.getState("Status");
        notesInput.setText(notes);
        statusInput.setValue(status);
    }

    private void submitChanges() {
        String notes = notesInput.getText() != null? notesInput.getText(): "";
        if(notes.length() > 200) {
            statusLog.displayErrorMessage("Notes must be less than 200 characters.");
            return;
        }
        statusLog.clearErrorMessage();
        String status = (String)statusInput.getValue();
        Properties props = new Properties();
        props.setProperty("Notes", notes);
        props.setProperty("Status", status);
        myModel.stateChangeRequest("ChangesSubmitted", props);

    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "UpdateTreeError":
                statusLog.displayErrorMessage("Error saving changes.");
                break;
        }
        
    }
}
