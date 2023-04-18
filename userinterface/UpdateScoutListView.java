package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import model.ScoutCollection;

public class UpdateScoutListView extends View {
	// GUI components
	protected TableView<ScoutTable> tableOfScouts;

	protected Button backButton, submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public UpdateScoutListView(IModel model)
	{
		super(model, "UpdateScoutListView");

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

		Text titleText = new Text(" Update Scout ");
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
		
		GridPane titleGrid = new GridPane();
        titleGrid.setAlignment(Pos.CENTER);
       	titleGrid.setHgap(10);
        titleGrid.setVgap(10);
        titleGrid.setPadding(new Insets(25, 25, 25, 25));
        
        Text prompt = new Text("SELECT SCOUT");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        titleGrid.add(prompt, 0, 0, 2, 1);

		tableOfScouts = new TableView<ScoutTable>();
		tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	
		TableColumn scoutIdColumn = new TableColumn("ID");
		scoutIdColumn.setMinWidth(25);
		scoutIdColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("scoutID"));
		
		TableColumn firstNameColumn = new TableColumn("First Name");
		firstNameColumn.setMinWidth(50);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("firstName"));
		  
		TableColumn middleNameColumn = new TableColumn("Middle Name");
		middleNameColumn.setMinWidth(50);
		middleNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("middleName"));
		
		TableColumn lastNameColumn = new TableColumn("Last Name");
		lastNameColumn.setMinWidth(50);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("lastName"));
		
		TableColumn dobColumn = new TableColumn("Date of Birth");
		dobColumn.setMinWidth(50);
		dobColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("dateOfBirth"));
		
		TableColumn phoneColumn = new TableColumn("Phone Number");
		phoneColumn.setMinWidth(50);
		phoneColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("phoneNumber"));
		
		TableColumn emailColumn = new TableColumn("Email");
		emailColumn.setMinWidth(50);
		emailColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("email"));
		
		TableColumn troopIDColumn = new TableColumn("Troop ID");
		troopIDColumn.setMinWidth(50);
		troopIDColumn.setCellValueFactory(new PropertyValueFactory<ScoutTable, String>("troopID"));

		tableOfScouts.getColumns().addAll(scoutIdColumn, firstNameColumn, middleNameColumn,
				lastNameColumn, dobColumn, phoneColumn, emailColumn, troopIDColumn);
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(115, 150);
		scrollPane.setContent(tableOfScouts);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		
		backButton = new Button("Back");
		backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		backButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	clearErrorMessage();
   		    	myModel.stateChangeRequest("BackSearch", null);   
        	  }
    	});
		doneCont.getChildren().add(backButton);
		
		submitButton = new Button("Submit");
		submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
   		     @Override
   		     public void handle(ActionEvent e) {
   		    	 if (tableOfScouts.getSelectionModel().getSelectedIndex() == -1) return;
   		    	clearErrorMessage();
   		    	
   		    	myModel.stateChangeRequest("Select", tableOfScouts.getSelectionModel().getSelectedItem().getScoutID());
   				
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
	
		vbox.getChildren().add(titleGrid);
		vbox.getChildren().add(scrollPane);
		vbox.getChildren().add(doneCont);

		return vbox;
	}
	
	private ObservableList<ScoutTable> getTableData(ScoutCollection scoutList)
	{
		ObservableList<ScoutTable> tableData = FXCollections.observableArrayList();
		for (Scout scout : scoutList.scouts) {
			tableData.add(new ScoutTable(scout));
		}
		
		return tableData;
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
		tableOfScouts.setItems(getTableData((ScoutCollection)myModel.getState("ScoutList")));
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