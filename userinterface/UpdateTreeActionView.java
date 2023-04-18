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

public class UpdateTreeActionView extends View {

    private TextField barcode;

    protected MessageView statusLog;

    public UpdateTreeActionView(IModel model) {
        super(model, "UpdateTreeActionView");

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        VBox form = createFormContents();
        Node title = createTitle();

        container.getChildren().add(title);
        container.getChildren().add(form);

        getChildren().add(container);
        myModel.subscribe("LookupTreeError", this);
        myModel.subscribe("TreeUpdated", this);
    }

    private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Update A Tree ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        
        Label barcodeLabel = new Label("Please enter tree barcode: ");
        barcode = new TextField();
        barcode.setOnAction( action -> {
            submitBarcode();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });
        Button submit = new Button("submit");
        submit.setOnAction( action -> {
            submitBarcode();
        });
        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(submit);

        statusLog = new MessageView("");

        container.getChildren().add(barcodeLabel);
        container.getChildren().add(barcode);
        container.getChildren().add(btnContainer);
        container.getChildren().add(statusLog);

        return container;
    }

    private void submitBarcode(){
        String bcode = barcode.getText();
        if(!bcode.matches("^\\d{5}$") || bcode.isEmpty()) {
            statusLog.displayErrorMessage("ERROR! Barcode must be EXACTLY 5 digits.");
            return;
        } 
        myModel.stateChangeRequest("BarcodeSubmitted", bcode);
    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "LookupTreeError":
                statusLog.displayErrorMessage("ERROR! Tree with barcode + " + barcode.getText() + " does not exist!");
                // maybe put a helper method somewhwere to do this
                Stage stage = MainStageContainer.getInstance();
                stage.sizeToScene();
                break;
            case "TreeUpdated":
                displayTreeUpdated();
        }
        
    }

    private void displayTreeUpdated() {
        barcode.setText("");
        statusLog.displayMessage("Tree with barcode " + myModel.getState("Barcode") + " updated successfully.");
    }
}
