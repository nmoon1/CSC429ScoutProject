package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class EndShiftActionView extends View {

    private TextField endDate;
    private TextField endTime;
    private TextField endCash;

    public EndShiftActionView(IModel model) {
        super(model, "EndShiftActionView");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createFormContents());

        getChildren().add(container);
    }
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.TOP_CENTER);

        Text titleText = new Text("End Shift");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }
    private VBox createFormContents() {
        VBox container = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text endDateLabel = new Text("Total Check Sales:");
        endDateLabel.setWrappingWidth(150);
        endDateLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endDateLabel, 0, 0);

        endDate = new TextField();
        endDate.setOnAction( event -> {
            confirm(event);
        });
        grid.add(endDate, 1, 0);

        Text endCashLabel = new Text("Ending Cash:");
        endCashLabel.setWrappingWidth(150);
        endCashLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endCashLabel, 0, 2);

        endCash = new TextField();
        endCash.setOnAction( event -> {
            confirm(event);
        });
        grid.add(endCash, 1, 2);

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelBtn.setTextAlignment(TextAlignment.CENTER);
        cancelBtn.setOnAction( event -> {
            myModel.stateChangeRequest("Cancel", null);
        });

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        confirmBtn.setTextAlignment(TextAlignment.CENTER);
        confirmBtn.setOnAction( event -> {
            confirm(event);
        });
        container.getChildren().add(createTitle());

        btnContainer.getChildren().add(cancelBtn);
        btnContainer.getChildren().add(confirmBtn);
        container.getChildren().add(grid);
        container.getChildren().add(btnContainer);

        Text notesLabel = new Text("Notes:");
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 3);

        TextArea notesTextArea = new TextArea();
        notesTextArea.setPrefRowCount(4);
        grid.add(notesTextArea, 1, 3);


        return container;
    }

    public void confirm(Event event) {
        String date = endDate.getText();
        String time = endTime.getText();
        String cash = endCash.getText();

        // TODO: better validation and display error message in GUI
        if(date == null) {
            System.out.println("Please enter a end date.");
            return;
        }
        if(time == null) {
            System.out.println("Please enter a end time.");
            return;
        }
        if(cash == null) {
            System.out.println("Please enter ending cash.");
        }

        Properties props = new Properties();
        props.setProperty("EndDate", date);
        props.setProperty("EndTime", time);
        props.setProperty("EndingCash", cash);

        myModel.stateChangeRequest("EndSession", props);

    }

    public void updateState(String key, Object value) {

    }
}
