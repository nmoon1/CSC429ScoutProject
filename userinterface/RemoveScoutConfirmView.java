package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Scout;

public class RemoveScoutConfirmView extends View {
	// GUI components
	protected Text scoutID, firstName, middleName, lastName, dateOfBirth, phoneNumber, email, troopID;

	protected Button backButton, searchButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public RemoveScoutConfirmView(IModel model)
	{
		super(model, "RemoveScoutConfirmView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContent());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		//myModel.subscribe("ServiceCharge", this);
		myModel.subscribe("UpdateStatusMessage", this);
	}


	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Remove Scout ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);
		
		return container;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(15);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text prompt = new Text("CONFIRM SCOUT REMOVAL");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        
        Text scoutIDLabel = new Text(" Scout ID: ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		scoutIDLabel.setFont(myFont);
		scoutIDLabel.setWrappingWidth(150);
		scoutIDLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(scoutIDLabel, 0, 1);

		scoutID = new Text("");
		grid.add(scoutID, 1, 1);

		Text firstNameLabel = new Text(" First Name: ");
		firstNameLabel.setFont(myFont);
		firstNameLabel.setWrappingWidth(150);
		firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(firstNameLabel, 0, 2);

		firstName = new Text("");
		grid.add(firstName, 1, 2);

		Text middleNameLabel = new Text(" Middle Name: ");
		middleNameLabel.setFont(myFont);
		middleNameLabel.setWrappingWidth(150);
		middleNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(middleNameLabel, 0, 3);

		middleName = new Text("");
		grid.add(middleName, 1, 3);

		Text lastNameLabel = new Text(" Last Name: ");
		lastNameLabel.setFont(myFont);
		lastNameLabel.setWrappingWidth(150);
		lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(lastNameLabel, 0, 4);

		lastName = new Text("");
		grid.add(lastName, 1, 4);

		Text dobLabel = new Text(" Date of Birth: ");
		dobLabel.setFont(myFont);
		dobLabel.setWrappingWidth(150);
		dobLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dobLabel, 0, 5);

		dateOfBirth = new Text("");
		grid.add(dateOfBirth, 1, 5);

		Text phoneNumberLabel = new Text(" Phone Number: ");
		phoneNumberLabel.setFont(myFont);
		phoneNumberLabel.setWrappingWidth(150);
		phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(phoneNumberLabel, 0, 6);

		phoneNumber = new Text("");
		grid.add(phoneNumber, 1, 6);

		Text emailLabel = new Text(" Email: ");
		emailLabel.setFont(myFont);
		emailLabel.setWrappingWidth(150);
		emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 0, 7);

		email = new Text("");
		grid.add(email, 1, 7);

		Text troopLabel = new Text(" Troop ID: ");
		troopLabel.setFont(myFont);
		troopLabel.setWrappingWidth(150);
		troopLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(troopLabel, 0, 8);

		troopID = new Text("");
		grid.add(troopID, 1, 8);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	myModel.stateChangeRequest("BackConfirm", null);   
        	  }
    	});
		doneCont.getChildren().add(backButton);
		
		searchButton = new Button("Remove");
		searchButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	
   				myModel.stateChangeRequest("Remove", null);
   				
   				displayMessage("Scout removed");
   				Stage stage = (Stage)myModel.getState("Stage");
   				statusLog.setWrappingWidth(stage.getWidth() - 50);
   				stage.sizeToScene();
   		     }
    	});
		doneCont.getChildren().add(searchButton);
	
		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);

		return vbox;
	}


	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);
		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{
		Scout scout = (Scout)myModel.getState("Scout");
		scoutID.setText((String)scout.getState("ID"));
		firstName.setText((String)scout.getState("FirstName"));
		middleName.setText((String)scout.getState("MiddleName"));
		lastName.setText((String)scout.getState("LastName"));
		dateOfBirth.setText((String)scout.getState("DateOfBirth"));
		phoneNumber.setText((String)scout.getState("PhoneNumber"));
		email.setText((String)scout.getState("Email"));
		troopID.setText((String)scout.getState("TroopID"));
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}