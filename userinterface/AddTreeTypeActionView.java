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

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.application.Platform;

/** The class containing the Tree View  for the Library application */
//==============================================================
public class AddTreeTypeActionView extends View
{

	// GUI components
	protected TextField TypeDescription;
	protected TextField Cost;
    protected TextField BarcodePrefix;

	protected Button cancelButton;
	protected Button submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public AddTreeTypeActionView(IModel addTreeType)
	{
		super(addTreeType, "AddTreeTypeActionView");

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

		myModel.subscribe("AddMessage", this);
	}


	// Create the Barcode container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		HBox container = new HBox();
		container.setAlignment(Pos.CENTER);	

		Text titleText = new Text(" Add A New TreeType ");
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
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // type description
		Text typeDescriptionLabel = new Text(" Type Description : ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		typeDescriptionLabel.setFont(myFont);
		typeDescriptionLabel.setWrappingWidth(150);
		typeDescriptionLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(typeDescriptionLabel, 0, 1);

		TypeDescription = new TextField();
		TypeDescription.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	processAction(e);    
            	     }
        	});
		TypeDescription.setEditable(true);
		grid.add(TypeDescription, 1, 1);

        // cost
		Text costLabel = new Text(" Cost : ");
		costLabel.setFont(myFont);
		costLabel.setWrappingWidth(150);
		costLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(costLabel, 0, 2);

		Cost = new TextField();
		Cost.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	processAction(e);    
            	     }
        	});
		Cost.setEditable(true);
		grid.add(Cost, 1, 2);

        // barcode prefix
        Text barcodePrefixLabel = new Text(" Barcode Prefix : ");
		barcodePrefixLabel.setFont(myFont);
		barcodePrefixLabel.setWrappingWidth(150);
		barcodePrefixLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(barcodePrefixLabel, 0, 3);

		BarcodePrefix = new TextField();
		BarcodePrefix.setEditable(true);

		// force field to be numeric only
		BarcodePrefix.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
				String newValue) {
				if (!newValue.matches("\\d*")) {

					Platform.runLater(() -> {
                    	BarcodePrefix.setText(newValue.replaceAll("[^\\d]", ""));
                	});
				}
			}
		});
		BarcodePrefix.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	processAction(e);    
            	    }
        	});
		grid.add(BarcodePrefix, 1, 3);

        //submit
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
       		    	myModel.stateChangeRequest("CancelAction", null);
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

	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}
	
	//-------------------------------------------------------------
	public void populateFields()
	{
		TypeDescription.setText("");
		Cost.setText("");
        BarcodePrefix.setText("");	
	}

	public void processAction(Event evt)
	{
		clearErrorMessage();

		String typeDescriptionEntered = TypeDescription.getText();
		String costEntered = Cost.getText();
		String barcodePrefixEntered = BarcodePrefix.getText();

		if ((typeDescriptionEntered == null) || (typeDescriptionEntered.length() == 0))
		{
			displayErrorMessage("Please enter a type description!");
			TypeDescription.requestFocus();
		}
		else if (typeDescriptionEntered.length() > 25)
		{
			displayErrorMessage("Type description must be 25 characters or less.");
			TypeDescription.requestFocus();
		}
		else if ((costEntered == null) || (costEntered.length() == 0))
		{
			displayErrorMessage("Please enter a cost!");
			Cost.requestFocus();
		}
		else if (costEntered.length() > 20)
		{
			displayErrorMessage("Cost must be 20 characters or less.");
			Cost.requestFocus();
		}
		else if ((barcodePrefixEntered == null) || (barcodePrefixEntered.length() == 0))
		{
			displayErrorMessage("Please enter a barcode prefix!");
			BarcodePrefix.requestFocus();
		}
		else if (barcodePrefixEntered.length() != 2)
		{
			displayErrorMessage("Barcode prefix must be 2 digits.");
			BarcodePrefix.requestFocus();
		}
		else
		{
			try {
				double costVal = Double.parseDouble(costEntered);
				insertTreeType();
			}
			catch(Exception e) {
				displayErrorMessage("Cost must be in double format.");
				Cost.requestFocus();
			}
		}
	}

	private void insertTreeType()
	{
		Properties props = new Properties();
		props.setProperty("TypeDescription", TypeDescription.getText());
		props.setProperty("Cost", Cost.getText());
		props.setProperty("BarcodePrefix", BarcodePrefix.getText());

		myModel.stateChangeRequest("ProcessAddTreeType", props);
	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if (key.equals("AddMessage")) {
			if(((String)value).equals("Tree Type inserted successfully!")) {
				displayMessage((String)value);
			}
			else if(((String)value).equals("Tree Type already exists for that Barcode Prefix.")){
				displayErrorMessage((String)value);
				BarcodePrefix.requestFocus();
			}
			else {
				displayErrorMessage((String)value);
			}
		}
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

//---------------------------------------------------------------
//	Revision History:
//


