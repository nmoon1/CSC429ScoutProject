package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.util.Properties;

public class SellTreeCostView extends View {
	// GUI components
	protected TextField newCost;
	protected Text curCost, description;
	protected TextArea treeNotes;

	protected Button backButton, submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SellTreeCostView(IModel model)
	{
		super(model, "SellTreeCostView");

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
        
        Text prompt = new Text("TREE COST");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        
        Text curCostLabel = new Text(" Current Cost: ");
		Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
		Font normalFont = Font.font("Helvetica", FontWeight.NORMAL, 12);
		curCostLabel.setFont(myFont);
		curCostLabel.setWrappingWidth(150);
		curCostLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(curCostLabel, 0, 1);

		curCost = new Text();
		curCost.setFont(normalFont);
		curCost.setWrappingWidth(150);
		curCost.setTextAlignment(TextAlignment.LEFT);
		grid.add(curCost, 1, 1);
        
        Text newCostLabel = new Text(" New Cost: ");
		newCostLabel.setFont(myFont);
		newCostLabel.setWrappingWidth(150);
		newCostLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(newCostLabel, 0, 2);

		newCost = new TextField();
		grid.add(newCost, 1, 2);
        
        Text descLabel = new Text(" Description: ");
        descLabel.setFont(myFont);
        descLabel.setWrappingWidth(150);
        descLabel.setTextAlignment(TextAlignment.RIGHT);
		grid.add(descLabel, 0, 3);
        
		description = new Text("");
		description.setFont(normalFont);
		description.setWrappingWidth(150);
		description.setTextAlignment(TextAlignment.LEFT);
		grid.add(description, 1, 3);

		Text treeNotesLabel = new Text("Notes:");
		treeNotesLabel.setFont(myFont);
		treeNotesLabel.setTextAlignment(TextAlignment.CENTER);
		treeNotes = new TextArea();
		grid.add(treeNotesLabel, 0, 4, 2, 1);
		grid.add(treeNotes, 0, 5, 2, 1);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	myModel.stateChangeRequest("BackCost", null);   
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
				props.setProperty("Cost", newCost.getText());
				props.setProperty("Notes", treeNotes.getText());
   		    	myModel.stateChangeRequest("SubmitCost", props);
   				
   				String error = (String)myModel.getState("Error");
   				if (error.length() != 0) displayErrorMessage(error);
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
		String currentCost = (String)myModel.getState("Cost");
		String notes = (String)myModel.getState("Notes");
		String desc = (String)myModel.getState("Description");
		curCost.setText("$" + currentCost);
		newCost.setText(currentCost);
		treeNotes.setText(notes);
		description.setText(desc);
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