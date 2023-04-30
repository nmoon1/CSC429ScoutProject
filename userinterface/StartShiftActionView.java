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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StartShiftActionView extends View {

    private TextField startDate;
    private TextField currentTime;
    private TextField endHour;
    private TextField endMin;
    private TextField startingCash;

    private TextField companion;
    private TextField companionHour;
    private TextField scoutStartHour;
    private TextField scoutStartMin;
    private TextField scoutEndHour;
    private TextField scoutEndMin;
    private ComboBox<String> scoutComboBox;
    ObservableList<ScoutInfo> data = FXCollections.observableArrayList();

    // warning for cancelling without adding any scouts to session
    boolean cancelFlag = false;

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

        startDate = new TextField();
        startDate.setText(LocalDate.now().toString());
        startDate.setDisable(true);
        startDate.setPrefWidth(200);
        startDate.setStyle("-fx-opacity: 1");
        grid.add(startDate, 2, 0);

        // start time
        Label startTimeLabel = new Label("Start Time:");
        grid.add(startTimeLabel, 1, 1);

        currentTime = new TextField();
        currentTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        currentTime.setDisable(true);
        currentTime.setPrefWidth(200);
        currentTime.setStyle("-fx-opacity: 1");
        grid.add(currentTime, 2, 1);

        // End Time
        Label endTimeLabel = new Label("End Time:");
        grid.add(endTimeLabel, 1, 2);

        HBox endTimeBox = new HBox(10);

        endHour = new TextField();
        endHour.setPrefWidth(30);
        Label endHourLabel = new Label(":");

        endMin = new TextField();
        endMin.setPrefWidth(30);

        endTimeBox.getChildren().addAll(endHour, endHourLabel, endMin);
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

        Vector<String> scoutList = (Vector<String>) myModel.getState("GetScouts");

        scoutComboBox = new ComboBox<>(
                FXCollections.observableArrayList(scoutList));
       // System.out.println(scoutList);
        scoutComboBox.setPrefWidth(200);
        scoutComboBox.setDisable(true);
        scoutComboBox.setPromptText("Select a Scout");
        grid.add(scoutComboBox, 2, 5);

        // Companion Start Time
        Label companionLabel = new Label("Companion:");
        grid.add(companionLabel, 1, 6);

        HBox companionBox = new HBox(10);
        companion = new TextField();
        companion.setPrefWidth(100);

        Label companionHourLabel = new Label("Hour:");
        companionHour = new TextField();
        companionHour.setPrefWidth(50);
        companionBox.getChildren().addAll(companion, companionHourLabel, companionHour);
        companionBox.setDisable(true);
        grid.add(companionBox, 2, 6);

        // Scout Start Time
        Label scoutStartLabel = new Label("Scout Start Time:");
        grid.add(scoutStartLabel, 1, 7);

        HBox scoutStartBox = new HBox(10);
        scoutStartHour = new TextField();
        scoutStartHour.setPrefWidth(30);

        Label scoutStartHourLabel = new Label(":");
        scoutStartMin = new TextField();
        scoutStartMin.setPrefWidth(30);

        scoutStartBox.setDisable(true);
        scoutStartBox.getChildren().addAll(scoutStartHour, scoutStartHourLabel, scoutStartMin);
        grid.add(scoutStartBox, 2, 7);

        // Scout End Time
        Label scoutEndLabel = new Label("Scout End Time:");
        grid.add(scoutEndLabel, 1, 8);

        HBox scoutEndBox = new HBox(10);
        scoutEndHour = new TextField();
        scoutEndHour.setPrefWidth(30);

        Label scoutEndHourLabel = new Label(":");
        scoutEndMin = new TextField();
        scoutEndMin.setPrefWidth(30);

        scoutEndBox.setDisable(true);
        scoutEndBox.getChildren().addAll(scoutEndHour, scoutEndHourLabel, scoutEndMin);
        grid.add(scoutEndBox, 2, 8);

        // Add Scout Button
        Button addScoutBtn = new Button("Add Scout");
        addScoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        addScoutBtn.setTextAlignment(TextAlignment.CENTER);
        addScoutBtn.setDisable(true);
        addScoutBtn.setOnAction(event -> {

            String scoutStartHourText = scoutStartHour.getText();
            String scoutStartMinText = scoutStartMin.getText();
            String scoutEndHourText = scoutEndHour.getText();
            String scoutEndMinText = scoutEndMin.getText();
            String companionHourText = companionHour.getText();

            // Check if any required fields are null or empty
            if (scoutComboBox.getValue() == null || companion.getText().isEmpty() || scoutStartHourText.isEmpty()
                    || scoutStartMinText.isEmpty() || scoutEndHourText.isEmpty()
                    || scoutEndMinText.isEmpty() || companionHourText.isEmpty()) {
                // Display an error message and return
                displayErrorMessage("Please fill out all Scout fields");
                return;
            }

            // check for companion name length
            if (companion.getText().length() > 30) {
                displayErrorMessage("Companion name too long.");
                return;
            }

            // validate time
            int scoutStartHourInt;
            int scoutEndHourInt;
            int scoutStartMinInt;
            int scoutEndMinInt;
            int companionHourInt;
            try {
                scoutStartHourInt = Integer.parseInt(scoutStartHourText);
                scoutEndHourInt = Integer.parseInt(scoutEndHourText);
                companionHourInt = Integer.parseInt(companionHourText);
                scoutStartMinInt = Integer.parseInt(scoutStartMinText);
                scoutEndMinInt = Integer.parseInt(scoutEndMinText);

                // must be 24 hour format
                if (scoutStartHourInt < 0 || scoutStartHourInt > 23 || scoutEndHourInt < 0 || scoutEndHourInt > 23
                        || companionHourInt < 0 || companionHourInt > 23) {
                    displayErrorMessage("Hours must be between 0 and 23.");
                    return;
                }

                // check if start and end minute are between 0 and 59
                if (scoutStartMinInt < 0 || scoutStartMinInt > 59 || scoutEndMinInt < 0 || scoutEndMinInt > 59) {
                    displayErrorMessage("Scout Start and End minute must be between 0 and 59.");
                    return;
                }

                // check if start time is less than end time
                if (scoutStartHourInt > scoutEndHourInt
                        || (scoutStartHourInt == scoutEndHourInt && scoutStartMinInt >= scoutEndMinInt)) {
                    displayErrorMessage("Scout Start time must be before End time.");
                    return;
                }

            } catch (Exception e) {
                displayErrorMessage("Time must be a number.");
                return;
            }

            // Get the selected scout from the combo box
            ScoutInfo selectedScout = new ScoutInfo(scoutComboBox.getValue());
            // Add the selected scout to the table
            data.add(selectedScout);
            // add to database
            AddScout(event);
        });
        grid.add(addScoutBtn, 1, 9);

        /*
         * ---------------------------
         * scout table
         * ---------------------------
         */
        // Create table and set placeholder text
        TableView<ScoutInfo> table = new TableView<>();
        Label placeholderLabel = new Label("No data available.");
        placeholderLabel.setPadding(new Insets(20, 20, 20, 20));
        table.setPlaceholder(placeholderLabel);

        // first column
        TableColumn<ScoutInfo, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // troop id column
        TableColumn<ScoutInfo, String> troopCol = new TableColumn<>("Troop ID");
        troopCol.setCellValueFactory(new PropertyValueFactory<>("troop"));

        // phone column
        TableColumn<ScoutInfo, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Add columns to the table
        table.getColumns().addAll(nameCol, troopCol, phoneCol);

        table.setItems(data);

        // Set column widths
        nameCol.setPrefWidth(225);
        troopCol.setPrefWidth(75);
        phoneCol.setPrefWidth(75);

        // Set the table height to show 4 rows without scrolling
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(28));

        // Create a scroll pane and add the table to it
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(71);

        // Add the scroll pane to the grid
        grid.add(scrollPane, 1, 10, 4, 2);

        // initialize submitBtn so it can be enabled by start session button
        Button submitBtn = new Button("Submit Scouts");

        /*
         * ----------------------
         * start session button
         * ----------------------
         */
        Button startSessionBtn = new Button("Start Shift");
        startSessionBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startSessionBtn.setTextAlignment(TextAlignment.CENTER);
        startSessionBtn.setOnAction(event -> {
            String endHourText = endHour.getText();
            String endMinText = endMin.getText();
            String startingCashText = startingCash.getText();

            // check if any required fields are null or empty
            if (endHourText.isEmpty() || endMinText.isEmpty()
                    || startingCashText.isEmpty()) {
                // Display an error message and return
                displayErrorMessage("Please fill out all Session fields");
                return;
            }

            // check for ent time to be integer
            int endHourInt;
            int endMinInt;
            try {
                endMinInt = Integer.parseInt(endMinText);
                endHourInt = Integer.parseInt(endHourText);

                // check for 24 hour time
                if (endHourInt < 0 || endHourInt > 23) {
                    displayErrorMessage("Session End hour must be between 0 and 23.");
                    return;
                }

                // check if start and end minute are between 0 and 59
                if (endMinInt < 0 || endMinInt > 59) {
                    displayErrorMessage("Session End minute must be between 0 and 59.");
                    return;
                }

                // check that end time is after current time
                LocalTime endTime = LocalTime.of(endHourInt, endMinInt);
                LocalTime tempTime = LocalTime.now();
                if (endTime.isBefore(tempTime)) {
                    System.out.println("before!");
                    displayErrorMessage("End time must be after start time.");
                    return;
                }

            } catch (Exception e) {
                displayErrorMessage("End time must be an integer.");
                return;
            }

            // starting cash must be a number
            double startingCashNumber;
            try {
                startingCashNumber = Double.parseDouble(startingCashText);
                if (startingCashNumber < 0) {
                    displayErrorMessage("Starting cash must be > $0.00");
                }
            } catch (Exception e) {
                displayErrorMessage("Starting Cash must be a number.");
                return;
            }

            int decimal = startingCashText.indexOf(".");
            // check for 2 decimal places max
            if (decimal != -1) {
                if (decimal < (startingCashText.length() - 3)) {
                    displayErrorMessage("Starting cash may only have 2 digits after decimal.");
                    return;
                }
            }

            // max value for cash
            if (startingCashNumber >= 1000000) {
                displayErrorMessage("Starting cash too large.");
                return;
            }

            // cannot be negative
            if (startingCashNumber < 0) {
                displayErrorMessage("Starting cash must be positive.");
                return;
            }

            // disable start session
            endHour.setDisable(true);
            endMin.setDisable(true);
            startingCash.setDisable(true);
            startSessionBtn.setDisable(true);

            // enable add scout
            addScoutBtn.setDisable(false);
            submitBtn.setDisable(false);
            scoutEndBox.setDisable(false);
            scoutStartBox.setDisable(false);
            scoutComboBox.setDisable(false);
            companionBox.setDisable(false);

            // show cancel warning
            cancelFlag = true;

            StartSession(event);
        });
        grid.add(startSessionBtn, 1, 4);

        /*
         * ---------------------------
         * start shift / back button
         * ---------------------------
         */
        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        // cancel button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelBtn.setTextAlignment(TextAlignment.CENTER);
        cancelBtn.setOnAction(event -> {
            if (cancelFlag) {
                displayErrorMessage("You must submit scouts to the started session.");
                return;
            }
            myModel.stateChangeRequest("Cancel", null);
        });

        // start shift button
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitBtn.setTextAlignment(TextAlignment.CENTER);
        submitBtn.setDisable(true);
        submitBtn.setOnAction(event -> {
            myModel.stateChangeRequest("StartShift", null);
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

    /*
     * --------------------
     * start session
     * --------------------
     */
    public void StartSession(Event event) {
        // clear error message
        clearErrorMessage();

        String endTimeH = endHour.getText();
        String endTimeM = endMin.getText();

        if (endTimeH.length() == 1) {
            endTimeH = "0" + endTimeH;
        }
        if (endTimeM.length() == 1) {
            endTimeM = "0" + endTimeM;
        }

        String cash = startingCash.getText();

        String endTime = endTimeH + ":" + endTimeM;

        String startTime = currentTime.getText();

        String date = startDate.getText();

        Properties props = new Properties();
        props.setProperty("StartDate", date);
        props.setProperty("StartTime", startTime);
        props.setProperty("EndTime", endTime);
        props.setProperty("StartingCash", cash);

        myModel.stateChangeRequest("StartSession", props);
        // display success message
        displayMessage("Session added!");
    }

    /*
     * add scount
     */
    public void AddScout(Event event) {
        // clear error messages
        clearErrorMessage();

        String selectedScout = scoutComboBox.getValue();
        String scoutID = selectedScout.substring(0, selectedScout.indexOf(" "));

        String comp = companion.getText();
        String compH = companionHour.getText();
        String scoutStartH = scoutStartHour.getText();
        String scoutStartM = scoutStartMin.getText();
        String scoutEndH = scoutEndHour.getText();
        String scoutEndM = scoutEndMin.getText();

        if (scoutStartH.length() == 1) {
            scoutStartH = "0" + scoutStartH;
        }
        if (scoutStartM.length() == 1) {
            scoutStartM = "0" + scoutStartM;
        }
        if (scoutEndH.length() == 1) {
            scoutEndH = "0" + scoutEndH;
        }
        if (scoutEndM.length() == 1) {
            scoutEndM = "0" + scoutEndM;
        }

        String ScoutStartTime = scoutStartH + ":" + scoutStartM;
        String ScoutEndTime = scoutEndH + ":" + scoutEndM;

        Properties props = new Properties();
        props.setProperty("ScoutID", scoutID);
        props.setProperty("CompanionName", comp);
        props.setProperty("CompanionHours", compH);
        props.setProperty("StartTime", ScoutStartTime);
        props.setProperty("EndTime", ScoutEndTime);

        myModel.stateChangeRequest("AddShift", props);

        // Remove the added scout from the list of scouts we can choose
        scoutComboBox.getItems().remove(scoutComboBox.getValue());

        // clear input fields
        scoutComboBox.getSelectionModel().clearSelection();
        companion.clear();
        companionHour.clear();
        scoutStartHour.clear();
        scoutStartMin.clear();
        scoutEndHour.clear();
        scoutEndMin.clear();

        // display success message
        displayMessage("Scout added!");
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
