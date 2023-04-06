// specify the package
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

//for messages
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** The class containing the Tree View for the Library application */
// ==============================================================
public class AddTreeActionView extends View {

	// GUI components
	protected TextField Barcode;
	protected String TreeType;
	protected TextArea Notes;
	protected String DateStatusUpdated;

	protected Button cancelButton;
	protected Button submitButton;

	// for showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	// ----------------------------------------------------------
	public AddTreeActionView(IModel Tree) {
		super(Tree, "AddTreeActionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a Barcode for this panel
		container.getChildren().add(createTitle());

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("UpdateStatusMessage", this);
		myModel.subscribe("TreeAdded", this);
	}

	// Create the Barcode container
	// -------------------------------------------------------------
	private Node createTitle() {
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);

		Text titleText = new Text(" Add A New Tree ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		return container;
	}

	// Create the main form content
	// -------------------------------------------------------------
	private VBox createFormContent() {
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		// barcode
		Text barcodeLabel = new Text(" Barcode : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		barcodeLabel.setFont(myFont);
		barcodeLabel.setWrappingWidth(150);
		barcodeLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodeLabel, 0, 1);

		Barcode = new TextField();
		Barcode.setEditable(true);
		grid.add(Barcode, 1, 1);

		// type
		/*
		 * Text typeLabel = new Text(" TreeType : ");
		 * typeLabel.setFont(myFont);
		 * typeLabel.setWrappingWidth(150);
		 * typeLabel.setTextAlignment(TextAlignment.RIGHT);
		 * grid.add(typeLabel, 0, 2);
		 * 
		 * TreeType = new TextField();
		 * TreeType.setEditable(true);
		 * grid.add(TreeType, 1, 2);
		 */

		// notes
		Text notesLabel = new Text(" Notes : ");
		notesLabel.setFont(myFont);
		notesLabel.setWrappingWidth(150);
		notesLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(notesLabel, 0, 2);

		Notes = new TextArea();
		Notes.setPrefSize(150,60);
		Notes.setWrapText(true);
		Notes.setEditable(true);
		grid.add(Notes, 1, 2);

		// status
		/*
		 * Text statusLabel = new Text(" Status : ");
		 * statusLabel.setFont(myFont);
		 * statusLabel.setWrappingWidth(150);
		 * statusLabel.setTextAlignment(TextAlignment.RIGHT);
		 * grid.add(statusLabel, 0, 4);
		 * 
		 * statusComboBox = new ComboBox(FXCollections.observableArrayList(Status));
		 * grid.add(statusComboBox, 1, 4);
		 */

		// date status updated
		/*
		 * Text dateStatusUpdatedLabel = new Text(" Date Status Updated : ");
		 * dateStatusUpdatedLabel.setFont(myFont);
		 * dateStatusUpdatedLabel.setWrappingWidth(150);
		 * dateStatusUpdatedLabel.setTextAlignment(TextAlignment.RIGHT);
		 * grid.add(dateStatusUpdatedLabel, 0, 4);
		 * 
		 * Text DateStatusUpdatedDate = new Text(DateStatusUpdated);
		 * dateStatusUpdatedLabel.setFont(myFont);
		 * dateStatusUpdatedLabel.setWrappingWidth(150);
		 * dateStatusUpdatedLabel.setTextAlignment(TextAlignment.RIGHT);
		 * grid.add(DateStatusUpdatedDate, 1, 4);
		 */

		// submit
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				processAction(e);
			}
		});

		cancelButton = new Button("Cancel");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();
				myModel.stateChangeRequest("Cancel", null);
			}
		});

		HBox btnContainer = new HBox(10);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(cancelButton);
		btnContainer.getChildren().add(submitButton);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(btnContainer);

		return vbox;
	}

	// -------------------------------------------------------------
	public void populateFields() {
		Barcode.setText((String) myModel.getState("Barcode"));

		Notes.setText((String) myModel.getState("Notes"));

	}

	public void processAction(Event evt) {

		if (Barcode.getText().isEmpty()) {
			displayErrorMessage("Please fill out Barcode field");
		} else {
			insertTree();
		}
	}

	private void insertTree() {

		Properties props = new Properties();
		// check that barcode is exactly 5 digits
		String barcode = Barcode.getText();
		if(barcode.length() != 5) {
			displayErrorMessage("Barcode must be EXACTLY 5 digits.");
			return;
		}
		props.setProperty("Barcode", barcode);

		// set tree type to first 2 digits of barcode
		TreeType = barcode.substring(0, 2);
		props.setProperty("TreeType", TreeType);

		String notes = Notes.getText() != null? Notes.getText(): "";
		if(notes.length() > 200) {
			displayErrorMessage("Notes must be less than 200 characters.");
			return;
		}
		props.setProperty("Notes", notes);
		// set date to today
		DateStatusUpdated = java.time.LocalDate.now().toString();
		props.setProperty("DateStatusUpdated", DateStatusUpdated);
		props.setProperty("Status", "Available");

		myModel.stateChangeRequest("ProcessAddTree", props);
	}

	// Create the status log field
	// -------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage) {
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	/**
	 * Update method
	 */
	// ---------------------------------------------------------
	public void updateState(String key, Object value) {
		switch(key) {
			case "UpdateStatusMessage":
				String status = (String)myModel.getState("StatusMessage");
				displayErrorMessage(status);
				break;
			case "TreeAdded":
				String addCompleteMessage = (String)myModel.getState("AddComplete");
				displayMessage(addCompleteMessage);
				break;
		}
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

// ---------------------------------------------------------------
// Revision History:
//
