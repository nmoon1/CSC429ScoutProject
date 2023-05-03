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

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.application.Platform;
import model.Session;
import model.Shift;

public class EndShiftActionView extends View{

    private MessageView statusLog;
    private Button confirm;
    private Button done;
    private Button cancel;
    private TextArea notes;

    private TextField endHour;
    private TextField endMin;

//    private TextField endShiftField;

    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");
        VBox container = createFormContents();
        getChildren().add(container);
        myModel.subscribe("NotesError", this);
        myModel.subscribe("ShiftEnded", this);
        myModel.subscribe("EndTime", this);
        populateFields();

    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        Text titleText = new Text("End Shift");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);

        String endingCash = (String)myModel.getState("EndingCash");
        String totalCheckSales = (String)myModel.getState("TotalCheckSales");


        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedEndingCash = currencyFormatter.format(Double.parseDouble(endingCash));
        String formattedTotalCheckSales = currencyFormatter.format(Double.parseDouble(totalCheckSales));

        Text cash = new Text("Ending Cash: " + formattedEndingCash);
        Text check = new Text("Total Check Sales: " + formattedTotalCheckSales);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        // grid.setStyle("-fx-background-color: #f5f5f5;");
        grid.setHgap(10);
        grid.setVgap(10);
        Text notesLabel = new Text("Please enter any notes about the shift: ");
        notes = new TextArea();
        ///////////////////////

        Label startTimeLabel = new Label("Start Time:");
        grid.add(startTimeLabel, 1, 1);

        HBox endTimeBox = new HBox(10);

        endHour = new TextField();
        endHour.setPrefWidth(30);
        Label endHourLabel = new Label("H");

        endMin = new TextField();
        endMin.setPrefWidth(30);
        Label endMinLabel = new Label("M");

        endTimeBox.getChildren().addAll(endHour, endHourLabel, endMin, endMinLabel);
        grid.add(endTimeBox, 2, 1);



        ////////////////
        Text endShiftLabel = new Text("Would you like to change the shift ");
//        endShiftField = new TextField();
        /////////////////////////////////

//        Session session = new Session();
//        String endTime = (String)session.getState("EndTime");
//        endShiftField.setText(endTime);
//        System.out.println(endTime);

        ///////////////////////////////////////////////


        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.CENTER);

        cancel = new Button("Cancel");
        cancel.setOnAction( action -> {
            myModel.stateChangeRequest("Cancel", "");
        });
        cancel.managedProperty().bind(cancel.visibleProperty());

        confirm = new Button("Confirm");
        confirm.setOnAction( action -> {
            Properties props = new Properties();
            props.setProperty("Time", endHour.getText() + ":" + endMin.getText());
            props.setProperty("Notes", notes.getText());
            myModel.stateChangeRequest("Confirm", props);
            // add code to vaildate that its between  0 - 23 and acutually numbers
          //  Pattern.matches("^\\d{2}$", text)
            // make the text boxes a reasonable size

//            myModel.stateChangeRequest("Confirm", notes.getText());
//            myModel.stateChangeRequest("Confirm", endShiftField.getText());
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

        container.getChildren().add(titleText);
        container.getChildren().add(cash);
        container.getChildren().add(check);
        container.getChildren().add(notesLabel);
        container.getChildren().add(notes);
        container.getChildren().add(endShiftLabel);
//        container.getChildren().add(endShiftField);
        container.getChildren().add(endHour);
        container.getChildren().add(endHourLabel);
        container.getChildren().add(endMin);
        container.getChildren().add(endMinLabel);



        container.getChildren().add(btnContainer);
        container.getChildren().add(statusLog);

        return container;
    }




        private void populateFields() {
            String minTime = (String)new Session().getState("EndTime");
            String HourTime = (String)new Session().getState("EndTime");
            endMin.setText(minTime.substring(3));
            endHour.setText(HourTime.substring(0,2));
        }

//

    private void displayEndShift() {
//        statusLog.displayMessage("Shift ended.");
        cancel.setVisible(false);
        confirm.setVisible(false);
        done.setVisible(true);
        notes.setDisable(true);
    }

    public void updateState(String key, Object value) {
        switch(key) {
            case "NotesError":
                statusLog.displayErrorMessage("Notes must be less than 500 characters.");
                break;
            case "ShiftEnded":
                displayEndShift();
                break;
            case "EndTime":
                statusLog.displayMessage("Shift Changed succesfully");
                break;
//          case"EndTimeH":
//               return currentSession.getState(String.valueOf(endTimeH));
//                myModel.getState(endtime);
//
//                break;
        }

    }

}
