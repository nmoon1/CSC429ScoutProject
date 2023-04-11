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

public class RemoveTreeActionView extends View {

    private TextField barcodeInput;
    private Text status;

    public RemoveTreeActionView(IModel model) {
        super(model, "RemoveTreeActionView");
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        Node title = createTitle();
        VBox form = createFormContents();
        container.getChildren().add(title);
        container.getChildren().add(form);
        getChildren().add(container);
        myModel.subscribe("RemoveError", this);
        myModel.subscribe("LookupTreeError", this);
    }

    private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Remove A Tree ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setAlignment(Pos.CENTER);
        Text label = new Text("Enter tree barcode: ");
        container.getChildren().add(label);
        barcodeInput = new TextField();
        barcodeInput.setOnAction( action -> {
            String barcode = barcodeInput.getText();
            submitBarcode(barcode);
        });
        container.getChildren().add(barcodeInput);

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        Button submit = new Button("Submit");
        submit.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submit.setTextAlignment(TextAlignment.CENTER);
        submit.setOnAction( action -> {
            String barcode = barcodeInput.getText();
            submitBarcode(barcode);
        });
        
        Button cancel = new Button("Cancel");
        cancel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancel.setTextAlignment(TextAlignment.CENTER);
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });

        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(submit);

        status = new Text("  ");
        status.setFill(Color.RED);
        status.setWrappingWidth(300);

        container.getChildren().add(status);
        container.getChildren().add(btnContainer);

        return container;
    }

    private void submitBarcode(String barcode) {
        myModel.stateChangeRequest("BarcodeSubmitted", barcode);
    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "RemoveError":
                status.setText((String)myModel.getState("RemoveStatusMessage"));
                break;
            case "LookupTreeError":
                status.setText("Tree not found. Please try another barcode.");
                break;
        }
    }
    
}
