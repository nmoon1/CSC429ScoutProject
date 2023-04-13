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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class StartShiftActionView extends View {

    private TextField startDate;
    private TextField startHour;
    private TextField startMin;
    private TextField endHour;
    private TextField endMin;
    private ComboBox<String> scoutComboBox;
    private TextField companion;
    private TextField companionHour;
    private TextField scoutStartHour;
    private TextField scoutStartMin;
    private TextField scoutEndHour;
    private TextField scoutEndMin;
    private TextField startingCash;

    private TextField startCash;

    public StartShiftActionView(IModel model) {
        super(model, "StartShiftActionView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createFormContents());

        getChildren().add(container);
    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // start date
        // ---------------------------------------

        Text startDateLabel = new Text("Start Date:");
        startDateLabel.setWrappingWidth(150);
        startDateLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startDateLabel, 0, 0);

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setOnAction(event -> {
            submit(event);
        });
        grid.add(startDatePicker, 1, 0);

        // start time
        // ---------------------------------------

        Text startTimeLabel = new Text("Start Time:");
        startTimeLabel.setWrappingWidth(150);
        startTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startTimeLabel, 0, 1);

        startHour = new TextField();
        startHour.setPrefWidth(75); // Set the preferred width of the TextField
        startHour.setOnAction(event -> submit(event));
        grid.add(startHour, 1, 1);

        Label hourLabel = new Label("H");
        grid.add(hourLabel, 2, 1);

        startMin = new TextField();
        startMin.setPrefWidth(75); // Set the preferred width of the TextField
        startMin.setOnAction(event -> submit(event));
        grid.add(startMin, 3, 1);

        Label minLabel = new Label("M");
        grid.add(minLabel, 4, 1);

        // end time
        // ---------------------------------------

        Text endTimeLabel = new Text("End Time:");
        endTimeLabel.setWrappingWidth(150);
        endTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endTimeLabel, 0, 2);

        endHour = new TextField();
        endHour.setPrefWidth(75); // Set the preferred width of the TextField
        endHour.setOnAction(event -> submit(event));
        grid.add(endHour, 1, 2);

        Label endhourLabel = new Label("H");
        grid.add(endhourLabel, 2, 2);

        endMin = new TextField();
        endMin.setPrefWidth(75); // Set the preferred width of the TextField
        endMin.setOnAction(event -> submit(event));
        grid.add(endMin, 3, 2);

        Label endminLabel = new Label("M");
        grid.add(endminLabel, 4, 2);

        // starting cash
        // ---------------------------------------

        Text startingCashLabel = new Text("Starting Cash:");
        startingCashLabel.setWrappingWidth(150);
        startingCashLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startingCashLabel, 0, 3);

        startingCash = new TextField();
        startingCash.setPrefWidth(75); // Set the preferred width of the TextField
        startingCash.setOnAction(event -> submit(event));
        grid.add(startingCash, 1, 3);

        // add session button
        // ---------------------------------------

        Button addSessionBtn = new Button("Add Session");
        addSessionBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addSessionBtn.setTextAlignment(TextAlignment.CENTER);
        grid.add(addSessionBtn, 4, 4);

        // scout combo box
        // ---------------------------------------

        Text scoutLabel = new Text("Scout:");
        scoutLabel.setWrappingWidth(150);
        scoutLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(scoutLabel, 0, 5);

        scoutComboBox = new ComboBox<String>(FXCollections.observableArrayList("Scout 1", "Scout 2", "Scout 3"));
        scoutComboBox.setPrefWidth(150);
        scoutComboBox.setOnAction(event -> {
            submit(event);
        });
        grid.add(scoutComboBox, 1, 5);

        // companion
        // ---------------------------------------

        Text companionLabel = new Text("Companion:");
        companionLabel.setWrappingWidth(150);
        companionLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(companionLabel, 0, 6);

        companion = new TextField();
        companion.setPrefWidth(75); // Set the preferred width of the TextField
        companion.setOnAction(event -> submit(event));
        grid.add(companion, 1, 6);

        // companion hour
        Label companionHourLabel = new Label("Hour:");
        grid.add(companionHourLabel, 2, 6);

        companionHour = new TextField();
        companionHour.setPrefWidth(75); // Set the preferred width of the TextField
        companionHour.setOnAction(event -> submit(event));
        grid.add(companionHour, 3, 6);

        // scout start
        // ---------------------------------------

        Text scoutStartLabel = new Text("Scout Start:");
        scoutStartLabel.setWrappingWidth(150);
        scoutStartLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(scoutStartLabel, 0, 7);

        // start hour
        scoutStartHour = new TextField();
        scoutStartHour.setPrefWidth(75); // Set the preferred width of the TextField
        scoutStartHour.setOnAction(event -> submit(event));
        grid.add(scoutStartHour, 1, 7);

        Label scoutStartHourLabel = new Label("H");
        grid.add(scoutStartHourLabel, 2, 7);

        // start min
        scoutStartMin = new TextField();
        scoutStartMin.setPrefWidth(75); // Set the preferred width of the TextField
        scoutStartMin.setOnAction(event -> submit(event));
        grid.add(scoutStartMin, 3, 7);

        Label scoutStartMinLabel = new Label("M");
        grid.add(scoutStartMinLabel, 4, 7);

        // scout end
        // ---------------------------------------

        Text scoutEndLabel = new Text("Scout End:");
        scoutEndLabel.setWrappingWidth(150);
        scoutEndLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(scoutEndLabel, 0, 8);

        // end hour
        scoutEndHour = new TextField();
        scoutEndHour.setPrefWidth(75); // Set the preferred width of the TextField
        scoutEndHour.setOnAction(event -> submit(event));
        grid.add(scoutEndHour, 1, 8);

        Label scoutEndHourLabel = new Label("H");
        grid.add(scoutEndHourLabel, 2, 8);

        // end min
        scoutEndMin = new TextField();
        scoutEndMin.setPrefWidth(75); // Set the preferred width of the TextField
        scoutEndMin.setOnAction(event -> submit(event));
        grid.add(scoutEndMin, 3, 8);

        Label scoutEndMinLabel = new Label("M");
        grid.add(scoutEndMinLabel, 4, 8);

        // add scout button
        // ---------------------------------------

        Button addScoutBtn = new Button("Add Scout");
        addScoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addScoutBtn.setTextAlignment(TextAlignment.CENTER);
        grid.add(addScoutBtn, 4, 9);

        // scouts staffing shift table
        // ---------------------------------------

        Text scoutTableLabel = new Text("Scouts Staffing Shift:");
        scoutTableLabel.setWrappingWidth(150);
        scoutTableLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(scoutTableLabel, 0, 10);

        TableView table = new TableView();
        table.setPlaceholder(new Label("No data available."));
        grid.add(table, 1, 10, 4, 2);

        // start shift / cancel button
        // ---------------------------------------

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelBtn.setTextAlignment(TextAlignment.CENTER);
        cancelBtn.setOnAction(event -> {
            myModel.stateChangeRequest("Cancel", null);
        });

        Button submitBtn = new Button("Start Shift");
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitBtn.setTextAlignment(TextAlignment.CENTER);
        submitBtn.setOnAction(event -> {
            submit(event);
        });
        btnContainer.getChildren().add(cancelBtn);
        btnContainer.getChildren().add(submitBtn);

        container.getChildren().add(grid);
        container.getChildren().add(btnContainer);

        return container;
    }

    public void submit(Event event) {
        String date = startDate.getText();
        String time = startHour.getText();
        String cash = startCash.getText();

        // TODO: better validation and display error message in GUI
        if (date == null) {
            System.out.println("Please enter a start date.");
            return;
        }
        if (time == null) {
            System.out.println("Please enter a start time.");
            return;
        }
        if (cash == null) {
            System.out.println("Please enter starting cash.");
        }

        Properties props = new Properties();
        props.setProperty("StartDate", date);
        props.setProperty("StartTime", time);
        props.setProperty("StartingCash", cash);

        myModel.stateChangeRequest("StartSession", props);

    }

    public void updateState(String key, Object value) {

    }
}
