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

    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");
        VBox container = createFormContents();
        getChildren().add(container);
    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        String endingCash = (String)myModel.getState("EndingCash");
        String totalCheckSales = (String)myModel.getState("TotalCheckSales");
        Text cash = new Text("Ending Cash: " + endingCash);
        Text check = new Text("Total Check Sales: " + totalCheckSales);
        HBox btnContainer = new HBox(10);
        Button cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });
        Button confirm = new Button("Confirm");
        confirm.setOnAction( action -> {
            myModel.stateChangeRequest("Confirm", "");
        });
        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(confirm);
        container.getChildren().add(cash);
        container.getChildren().add(check);
        container.getChildren().add(btnContainer);
        return container;
    }

    @Override
    public void updateState(String key, Object value) {
        // TODO Auto-generated method stub
        
    }
    
}
