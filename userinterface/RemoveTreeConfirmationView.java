package userinterface;

// system imports
import javafx.event.Event;
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


public class RemoveTreeConfirmationView extends View {

    private Text label;
    
    public RemoveTreeConfirmationView(IModel model) {
        super(model, "RemoveTreeConfirmationView");

        VBox container = createConfirmationMessage();

        getChildren().add(container);
        myModel.subscribe("BarcodeSubmitted", this);
    }

    private VBox createConfirmationMessage() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setAlignment(Pos.CENTER);
        String barcode = (String)myModel.getState("Barcode");
        label = new Text("Are you sure you would like to remove tree with barcode: " + barcode + "?");
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction( action -> {
            myModel.stateChangeRequest("ConfirmRemove", "");
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });

        container.getChildren().add(label);

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancelButton);
        btnContainer.getChildren().add(confirmButton);

        container.getChildren().add(btnContainer);
        return container;
    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "BarcodeSubmitted":
                String barcode = (String)myModel.getState("Barcode");
                label.setText("Are you sure you would like to remove tree with barcode " + barcode + "?");
                break;
        }
    }
}
