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
    protected TextArea Notes;
    protected MessageView statusLog;
    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");
        VBox container = createFormContents();
        getChildren().add(container);

        myModel.subscribe("ShiftEnded", this);
        myModel.subscribe("NotesError", this);


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

        Notes = new TextArea();
        Notes.setPrefSize(150, 60);
        Notes.setWrapText(true);
//        Notes.setEditable(true);
        container.getChildren().add(Notes);

        Button cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("CancelAction", "");
                                        //change to action
        });
        Button confirm = new Button("Confirm");
        confirm.setOnAction( action -> {
            myModel.stateChangeRequest("Confirm", Notes.getText());

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
        switch (key){
            case"ShfitEnded":
                statusLog.displayMessage("The Shift has ended");
            case"NotesError":
                statusLog.displayErrorMessage("Notes were entered incorrectly");
                break;
        }

    }

}