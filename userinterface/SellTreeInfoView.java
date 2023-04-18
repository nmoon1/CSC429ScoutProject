package userinterface;

import java.util.Properties;

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
import javafx.stage.Stage;

public class SellTreeInfoView extends View {
	// GUI components
	protected TextField name, phoneNumber, email;
	protected ComboBox paymentMethod;

	protected Button backButton, submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SellTreeInfoView(IModel model)
	{
		super(model, "SellTreeInfoView");

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

		myModel.subscribe("UpdateStatusMessage", this);
	}


	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Sell a Tree ");
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
        
        Text prompt = new Text("CUSTOMER INFO");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        
        Text nameLabel = new Text(" Customer Name: ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		nameLabel.setFont(myFont);
		nameLabel.setWrappingWidth(150);
		nameLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(nameLabel, 0, 1);

		name = new TextField();
		grid.add(name, 1, 1);
        
        Text phoneNumberLabel = new Text(" Customer Phone Number: ");
		phoneNumberLabel.setFont(myFont);
		phoneNumberLabel.setWrappingWidth(150);
		phoneNumberLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(phoneNumberLabel, 0, 2);

		phoneNumber = new TextField();
		grid.add(phoneNumber, 1, 2);
        
        Text emailLabel = new Text(" Email: ");
        emailLabel.setFont(myFont);
        emailLabel.setWrappingWidth(150);
        emailLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(emailLabel, 0, 3);

		email = new TextField();
		grid.add(email, 1, 3);
        
        Text paymentMethodLabel = new Text(" Payment Method: ");
        paymentMethodLabel.setFont(myFont);
        paymentMethodLabel.setWrappingWidth(150);
        paymentMethodLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(paymentMethodLabel, 0, 4);

		paymentMethod = new ComboBox();
		paymentMethod.getItems().add("Cash");
		paymentMethod.getItems().add("Check");
		grid.add(paymentMethod, 1, 4);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	myModel.stateChangeRequest("BackInfo", null);   
        	  }
    	});
		doneCont.getChildren().add(backButton);
		
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	
   		    	Properties props = new Properties();
   		    	props.setProperty("Name", name.getText());
   		    	props.setProperty("PhoneNumber", phoneNumber.getText());
   		    	props.setProperty("Email", email.getText());
   		    	props.setProperty("PaymentMethod", (String)paymentMethod.getValue());
   		    	myModel.stateChangeRequest("SubmitInfo", props);
   				
   				String error = (String)myModel.getState("Error");
   				if (error.length() == 0) displayMessage("Tree sold"); 
   				else displayErrorMessage(error);
   				Stage stage = (Stage)myModel.getState("Stage");
   				statusLog.setWrappingWidth(stage.getWidth() - 50);
   				stage.sizeToScene();
   		     }
    	});
		doneCont.getChildren().add(submitButton);
		
		Button doneButton = new Button("Done");
		doneButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		doneButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	myModel.stateChangeRequest("CancelAction", null);   
        	  }
    	});
		doneCont.getChildren().add(doneButton);
	
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
		name.setText("");
		phoneNumber.setText("");
		email.setText("");
		paymentMethod.setValue(paymentMethod.getItems().get(0));
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