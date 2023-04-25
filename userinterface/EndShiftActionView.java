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
    private Button  cancel;
    private Button  done;
    private Button  confirm;
    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");
        VBox container = createFormContents();
        getChildren().add(container);

        myModel.subscribe("ShiftEnded", this);
        myModel.subscribe("NotesError", this);


    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" End Shift ");
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
        String endingCash = (String)myModel.getState("EndingCash");
        String totalCheckSales = (String)myModel.getState("TotalCheckSales");
        Text cash = new Text("Ending Cash: " + endingCash);
        Text check = new Text("Total Check Sales: " + totalCheckSales);
        Text NoteLabel = new Text("Please enter your notes: ");

        HBox btnContainer = new HBox(10);
        // add a label to the notes "Please enter your notes"
        // Also switch the order of the text box and numbers


        cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("CancelAction", "");
                                        //changed to CancelAction from just Cancel
        });
        confirm = new Button("Confirm");
        confirm.setOnAction( action -> {

            myModel.stateChangeRequest("Confirm", Notes.getText());//maybe add validation?

        });

        confirm.managedProperty().bind(confirm.visibleProperty());

        done = new Button("Done");
        done.setOnAction( action -> {
            myModel.stateChangeRequest("Done", "");

        });
        done.managedProperty().bind(done.visibleProperty());
        done.setVisible(false);

        statusLog = new MessageView("");

        container.getChildren().add(statusLog);

        container.getChildren().add(createTitle());

        container.getChildren().add(cash);
        container.getChildren().add(check);
        btnContainer.getChildren().add(cancel);
        btnContainer.getChildren().add(confirm);
        btnContainer.getChildren().add(done);

        Notes = new TextArea();
        Notes.setPrefSize(150, 60);
        Notes.setWrapText(true);
//        Notes.setEditable(true);
        container.getChildren().add(Notes);

        container.getChildren().add(btnContainer);
        return container;
    }

    private void displayDone() {
        // set confirm and cancel button's visibilty to false i.e cancel.setVisible(false);
        cancel.setVisible(false);
        confirm.setVisible(false);

        // set done button visibility to true i.e done.setVisible(true);
        done.setVisible(true);
        // disable text area (notes) i.e Notes.setDisabled(true);
        Notes.setDisable(true);
    }
    @Override
    public void updateState(String key, Object value) {
        // TODO Auto-generated method stub
        switch (key){
            case"ShiftEnded":
                displayDone();
                statusLog.displayMessage("The Shift has ended");
                break;
            case"NotesError":
                statusLog.displayErrorMessage("Notes must be less then 500 characters");
                //break;
        }

    }

}