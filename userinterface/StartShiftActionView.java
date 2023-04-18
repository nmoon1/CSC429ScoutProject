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
import java.time.LocalDate;

import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;

import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Region;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ScrollPane;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class StartShiftActionView extends View {

    private DatePicker startDatePicker;
    private TextField startHour;
    private TextField startMin;
    private TextField endHour;
    private TextField endMin;
    private TextField startingCash;

    // private ComboBox<String> scoutComboBox;
    private TextField companion;
    private TextField companionHour;
    private TextField scoutStartHour;
    private TextField scoutStartMin;
    private TextField scoutEndHour;
    private TextField scoutEndMin;
    ObservableList<Scout> data = FXCollections.observableArrayList();

    // for showing error message
    protected MessageView statusLog;

    public StartShiftActionView(IModel model) {
        super(model, "StartShiftActionView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContents());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        // populateFields();

        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("ShiftStarted", this);
    }

    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Start Shift ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContents() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(25));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        // grid.setStyle("-fx-background-color: #f5f5f5;");
        grid.setHgap(10);
        grid.setVgap(10);

        /*
         * ---------------------------
         * session
         * ---------------------------
         */
        // Start Date
        Label startDateLabel = new Label("Start Date:");
        grid.add(startDateLabel, 1, 0);

        startDatePicker = new DatePicker();
        startDatePicker.setPrefWidth(200);
        grid.add(startDatePicker, 2, 0);

        // Start Time
        Label startTimeLabel = new Label("Start Time:");
        grid.add(startTimeLabel, 1, 1);

        HBox startTimeBox = new HBox(10);

        startHour = new TextField();
        startHour.setPrefWidth(75);
        Label startHourLabel = new Label("H");

        startMin = new TextField();
        startMin.setPrefWidth(75);
        Label startMinLabel = new Label("M");

        startTimeBox.getChildren().addAll(startHour, startHourLabel, startMin, startMinLabel);
        grid.add(startTimeBox, 2, 1);

        // End Time
        Label endTimeLabel = new Label("End Time:");
        grid.add(endTimeLabel, 1, 2);

        HBox endTimeBox = new HBox(10);

        endHour = new TextField();
        endHour.setPrefWidth(75);
        Label endHourLabel = new Label("H");

        endMin = new TextField();
        endMin.setPrefWidth(75);
        Label endMinLabel = new Label("M");

        endTimeBox.getChildren().addAll(endHour, endHourLabel, endMin, endMinLabel);
        grid.add(endTimeBox, 2, 2);

        // Starting Cash
        Label startingCashLabel = new Label("Starting Cash:");
        grid.add(startingCashLabel, 1, 3);

        startingCash = new TextField();
        grid.add(startingCash, 2, 3);

        /*
         * ---------------------------
         * add scout
         * ---------------------------
         */

        // Scout Selection
        Label scoutLabel = new Label("Scout:");
        grid.add(scoutLabel, 1, 5);

        ComboBox<Scout> scoutComboBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        new Scout("Jon", "Jones", 208),
                        new Scout("Jake", "Gyllenhaal", 302),
                        new Scout("Juice", "Wrld", 999),
                        new Scout("Bob", "Marley", 305)));

        Scout selectedScout = scoutComboBox.getValue();

        if (selectedScout != null) {
            String first = selectedScout.getFirst();
            String last = selectedScout.getLast();
            int troop = selectedScout.getTroop();
        }
        scoutComboBox.setPrefWidth(200);
        scoutComboBox.setDisable(true);
        scoutComboBox.setPromptText("Select a Scout");
        grid.add(scoutComboBox, 2, 5);

        // Companion Start Time
        Label companionLabel = new Label("Companion:");
        grid.add(companionLabel, 1, 6);

        HBox companionBox = new HBox(10);
        TextField companion = new TextField();
        companion.setPrefWidth(100);

        Label companionHourLabel = new Label("Hour:");
        TextField companionHour = new TextField();
        companionHour.setPrefWidth(50);
        companionBox.getChildren().addAll(companion, companionHourLabel, companionHour);
        companionBox.setDisable(true);
        grid.add(companionBox, 2, 6);

        // Scout Start Time
        Label scoutStartLabel = new Label("Scout Start Time:");
        grid.add(scoutStartLabel, 1, 7);

        HBox scoutStartBox = new HBox(10);
        TextField scoutStartHour = new TextField();
        scoutStartHour.setPrefWidth(75);
        scoutStartHour.setOnAction(event -> AddScout(event));

        Label scoutStartHourLabel = new Label("H");
        TextField scoutStartMin = new TextField();
        scoutStartMin.setPrefWidth(75);
        scoutStartMin.setOnAction(event -> AddScout(event));

        Label scoutStartMinLabel = new Label("M");
        scoutStartBox.setDisable(true);
        scoutStartBox.getChildren().addAll(scoutStartHour, scoutStartHourLabel, scoutStartMin, scoutStartMinLabel);
        grid.add(scoutStartBox, 2, 7);

        // Scout End Time
        Label scoutEndLabel = new Label("Scout End Time:");
        grid.add(scoutEndLabel, 1, 8);

        HBox scoutEndBox = new HBox(10);
        TextField scoutEndHour = new TextField();
        scoutEndHour.setPrefWidth(75);
        scoutEndHour.setOnAction(event -> AddScout(event));

        Label scoutEndHourLabel = new Label("H");
        TextField scoutEndMin = new TextField();
        scoutEndMin.setPrefWidth(75);
        scoutEndMin.setOnAction(event -> AddScout(event));

        Label scoutEndMinLabel = new Label("M");
        scoutEndBox.setDisable(true);
        scoutEndBox.getChildren().addAll(scoutEndHour, scoutEndHourLabel, scoutEndMin, scoutEndMinLabel);
        grid.add(scoutEndBox, 2, 8);

        // Add Scout Button
        Button addScoutBtn = new Button("Add Scout");
        addScoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addScoutBtn.setTextAlignment(TextAlignment.CENTER);
        addScoutBtn.setDisable(true);
        addScoutBtn.setOnAction(event -> {
            // Get the selected scout from the combo box
            Scout selectedScouts = scoutComboBox.getValue();

            // Add the selected scout to the table
            data.add(selectedScouts);

            // Clear the selection in the combo box
            scoutComboBox.getSelectionModel().clearSelection();

            AddScout(event);
        });
        grid.add(addScoutBtn, 1, 9);

        /*
         * ---------------------------
         * scout table
         * ---------------------------
         */
        // Create table and set placeholder text
        TableView<Scout> table = new TableView<>();
        Label placeholderLabel = new Label("No data available.");
        placeholderLabel.setPadding(new Insets(20, 20, 20, 20));
        table.setPlaceholder(placeholderLabel);

        // first column
        TableColumn<Scout, String> firstCol = new TableColumn<>("First");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("First"));

        // last column
        TableColumn<Scout, Integer> lastCol = new TableColumn<>("Last");
        lastCol.setCellValueFactory(new PropertyValueFactory<>("Last"));

        // troop id column
        TableColumn<Scout, Integer> troopCol = new TableColumn<>("Troop ID");
        troopCol.setCellValueFactory(new PropertyValueFactory<>("Troop"));

        // Add columns to the table
        table.getColumns().addAll(firstCol, lastCol, troopCol);

        table.setItems(data);

        // Set column widths
        firstCol.setPrefWidth(150);
        lastCol.setPrefWidth(150);
        troopCol.setPrefWidth(75);

        // Set the table to dynamically resize to fit the number of rows
        table.setPrefHeight(Region.USE_COMPUTED_SIZE);
        table.setMaxHeight(Double.MAX_VALUE);
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(28));

        grid.add(table, 1, 10, 4, 2);

        /*
         * ----------------------
         * start session button
         * ----------------------
         */
        Button startSessionBtn = new Button("Add Session");
        startSessionBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startSessionBtn.setTextAlignment(TextAlignment.CENTER);
        startSessionBtn.setOnAction(event -> {
            // Check if any required fields are null or empty
            if (startDatePicker.getValue() == null || startHour.getText().isEmpty() || startMin.getText().isEmpty()
                    || endHour.getText().isEmpty() || endMin.getText().isEmpty() || startingCash.getText().isEmpty()) {
                // Display an error message and return
                
                return;
            }
            // disable start session
            startDatePicker.setDisable(true);
            startHour.setDisable(true);
            startMin.setDisable(true);
            endHour.setDisable(true);
            endMin.setDisable(true);
            startingCash.setDisable(true);
            startSessionBtn.setDisable(true);

            // enable add scout
            addScoutBtn.setDisable(false);
            scoutEndBox.setDisable(false);
            scoutStartBox.setDisable(false);
            scoutComboBox.setDisable(false);
            companionBox.setDisable(false);

            StartSession(event);
        });
        grid.add(startSessionBtn, 1, 4);

        /*
         * ---------------------------
         * start shift / cancel button
         * ---------------------------
         */
        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        // cancell button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelBtn.setTextAlignment(TextAlignment.CENTER);
        cancelBtn.setOnAction(event -> {

            myModel.stateChangeRequest("Cancel", null);
        });

        // start shift button
        Button submitBtn = new Button("Start Shift");
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitBtn.setTextAlignment(TextAlignment.CENTER);
        submitBtn.setOnAction(event -> {
            Finish(event);
        });

        // cancel buttom
        btnContainer.getChildren().add(cancelBtn);
        // submit button
        btnContainer.getChildren().add(submitBtn);

        container.getChildren().add(grid);
        container.getChildren().add(btnContainer);

        return container;
    }

    // Create the status log field
    // -------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    // scouts constructor
    public class Scout {
        private final String first;
        private final String last;
        private final int troop;

        public Scout(String first, String last, int troop) {
            this.first = first;
            this.last = last;
            this.troop = troop;
        }

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public int getTroop() {
            return troop;
        }

        @Override
        public String toString() {
            return first + " " + last + " (Troop " + troop + ")";
        }
    }

    /*
     * start session
     */
    public void StartSession(Event event) {
        // Get the selected year, month, and day values from the DatePicker
        LocalDate selectedDate = startDatePicker.getValue();
        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int dayOfMonth = selectedDate.getDayOfMonth();

        // Create a Calendar object and set its year, month, and day values
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Note: JavaFX months start from 1
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        // Format the Calendar object into a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());

        String startTimeH = startHour.getText();
        String startTimeM = startMin.getText();
        String endTimeH = endHour.getText();
        String endTimeM = endMin.getText();
        String cash = startingCash.getText();

        String startTime = startTimeH + ":" + startTimeM;
        String endTime = endTimeH + ":" + endTimeM;

        Properties props = new Properties();
        props.setProperty("StartDate", date);
        props.setProperty("StartTime", startTime);
        props.setProperty("EndTime", endTime);
        props.setProperty("StartingCash", cash);

        myModel.stateChangeRequest("StartSession", props);
        System.out.println(date + "\n" + startTime + "\n" + endTime + "\n" + cash);
    }

    /*
     * add scount
     */
    public void AddScout(Event event) {

        // String scout = scoutComboBox.getValue();
        String comp = companion.getText();
        String compH = companionHour.getText();
        String scoutStartH = scoutStartHour.getText();
        String scoutStartM = scoutStartMin.getText();
        String scoutEndH = scoutEndHour.getText();
        String scoutEndM = scoutEndMin.getText();

        Properties props = new Properties();
        // props.setProperty("Scout", scout);
        props.setProperty("Companion", comp);
        props.setProperty("CompanionHour", compH);
        props.setProperty("ScoutStartHour", scoutStartH);
        props.setProperty("ScoutStartMin", scoutStartM);
        props.setProperty("ScoutEndHour", scoutEndH);
        props.setProperty("ScoutEndMin", scoutEndM);

        myModel.stateChangeRequest("AddScout", props);
    }

    /*
     * finish
     */
    public void Finish(Event event) {

    }

    public void updateState(String key, Object value) {

    }

    /**
     * Display error message
     */
    // ----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Display info message
     */
    // ----------------------------------------------------------
    public void displayMessage(String message) {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    // ----------------------------------------------------------
    public void clearErrorMessage() {

        statusLog.clearErrorMessage();
    }
}
