package userinterface;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Scout;

public class RegisterScoutView extends View {
	// GUI components
	protected TextField firstName, middleName, lastName, dateOfBirth, phoneNumber, email, troopID;
	protected HBox successMessage;

	protected Button backButton, submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public RegisterScoutView(IModel model)
	{
		super(model, "RegisterScoutView");

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

		Text titleText = new Text(" Register New Scout ");
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
        
        Text prompt = new Text("SCOUT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		Text firstNameLabel = new Text(" First Name: ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		firstNameLabel.setFont(myFont);
		firstNameLabel.setWrappingWidth(150);
		firstNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(firstNameLabel, 0, 1);

		firstName = new TextField();
		grid.add(firstName, 1, 1);

		Text middleNameLabel = new Text(" Middle Name: ");
		middleNameLabel.setFont(myFont);
		middleNameLabel.setWrappingWidth(150);
		middleNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(middleNameLabel, 0, 2);

		middleName = new TextField();
		grid.add(middleName, 1, 2);

		Text lastNameLabel = new Text(" Last Name: ");
		lastNameLabel.setFont(myFont);
		lastNameLabel.setWrappingWidth(150);
		lastNameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(lastNameLabel, 0, 3);

		lastName = new TextField();
		grid.add(lastName, 1, 3);

		Text dobLabel = new Text(" Date of Birth: ");
		dobLabel.setFont(myFont);
		dobLabel.setWrappingWidth(150);
		dobLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(dobLabel, 0, 4);

		dateOfBirth = new TextField();
		grid.add(dateOfBirth, 1, 4);

		Text phoneNumberLabel = new Text(" Phone Number: ");
		phoneNumberLabel.setFont(myFont);
		phoneNumberLabel.setWrappingWidth(150);
		phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(phoneNumberLabel, 0, 5);

		phoneNumber = new TextField();
		grid.add(phoneNumber, 1, 5);

		Text emailLabel = new Text(" Email: ");
		emailLabel.setFont(myFont);
		emailLabel.setWrappingWidth(150);
		emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 0, 6);

		email = new TextField();
		grid.add(email, 1, 6);

		Text troopLabel = new Text(" Troop ID: ");
		troopLabel.setFont(myFont);
		troopLabel.setWrappingWidth(150);
		troopLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(troopLabel, 0, 7);

		troopID = new TextField();
		grid.add(troopID, 1, 7);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		    	clearErrorMessage();
       		    	myModel.stateChangeRequest("CancelAction", null);   
            	  }
        	});
		doneCont.getChildren().add(backButton);
		
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	vbox.getChildren().remove(successMessage);
   		    	clearErrorMessage();
   		    	
   		    	Properties prop = new Properties();
   		    	prop.setProperty("LastName", lastName.getText());
   		    	prop.setProperty("FirstName", firstName.getText());
   		    	prop.setProperty("MiddleName", middleName.getText());
   		    	prop.setProperty("DateOfBirth", dateOfBirth.getText());
   		    	prop.setProperty("PhoneNumber", phoneNumber.getText());
   		    	prop.setProperty("Email", email.getText());
   				prop.setProperty("TroopID", troopID.getText());
   				myModel.stateChangeRequest("submit", prop);
   				
   				String error = (String)myModel.getState("error");
   				if (error.length() != 0) displayErrorMessage(error);
   				else if (vbox.getChildren().contains(successMessage))
   					vbox.getChildren().add(successMessage); 
   		     }
    	});
		doneCont.getChildren().add(submitButton);
	
		vbox.getChildren().add(grid);
		vbox.getChildren().add(doneCont);
		
		Text text = new Text("Scout added");
		text.setFont(myFont);
		text.setTextAlignment(TextAlignment.CENTER);
		
		successMessage = new HBox(1);
		successMessage.setAlignment(Pos.CENTER);
		successMessage.getChildren().add(text);

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
		firstName.setText("");
		middleName.setText("");
		lastName.setText("");
		dateOfBirth.setText("");
		phoneNumber.setText("");
		email.setText("");
		troopID.setText("");
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();

		/*if (key.equals("ServiceCharge") == true)
		{
			String val = (String)value;
			serviceCharge.setText(val);
			displayMessage("Service Charge Imposed: $ " + val);
		}*/
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