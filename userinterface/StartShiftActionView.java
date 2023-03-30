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

import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;

public class StartShiftActionView extends View {

    private TextField startDate;
    private TextField startTime;
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
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text startDateLabel = new Text("Start Date:");
        startDateLabel.setWrappingWidth(150);
        startDateLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startDateLabel, 0, 0);

        startDate = new TextField();
        startDate.setOnAction( event -> {
            submit(event);
        });
        grid.add(startDate, 1, 0);

        Text startTimeLabel = new Text("Start Time:");
        startTimeLabel.setWrappingWidth(150);
        startTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startTimeLabel, 0, 1);

        startTime = new TextField();
        startTime.setOnAction( event -> {
            submit(event);
        });
        grid.add(startTime, 1, 1);

        Text startCashLabel = new Text("Starting Cash:");
        startCashLabel.setWrappingWidth(150);
        startCashLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(startCashLabel, 0, 2);

        startCash = new TextField();
        startCash.setOnAction( event -> {
            submit(event);
        });
        grid.add(startCash, 1, 2);

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelBtn.setTextAlignment(TextAlignment.CENTER);
        cancelBtn.setOnAction( event -> {
            myModel.stateChangeRequest("Cancel", null);
        });

        Button submitBtn = new Button("Submit");
        submitBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitBtn.setTextAlignment(TextAlignment.CENTER);
        submitBtn.setOnAction( event -> {
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
        String time = startTime.getText();
        String cash = startCash.getText();

        // TODO: better validation and display error message in GUI
        if(date == null) {
            System.out.println("Please enter a start date.");
            return;
        }
        if(time == null) {
            System.out.println("Please enter a start time.");
            return;
        }
        if(cash == null) {
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
