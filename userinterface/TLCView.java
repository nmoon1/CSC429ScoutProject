package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TLCView extends View {

    private Button endShiftBtn;
    private Button sellTreeBtn;
    private Button startShiftBtn;

    public TLCView(IModel model) {
        super(model, "TLCView");

        VBox container = new VBox(5);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setAlignment(Pos.CENTER);

        Node title = makeTitle();
        Button registerScoutBtn = makeButton("Register Scout", "RegisterScout");
        Button updateScoutBtn = makeButton("Update Scout", "UpdateScout");
        Button removeScoutBtn = makeButton("Remove Scout", "RemoveScout");
        Button addTreeBtn = makeButton("Add Tree", "AddTree");
        Button updateTreeBtn = makeButton("Update Tree", "UpdateTree");
        Button removeTreeBtn = makeButton("Remove Tree", "RemoveTree");
        Button addTreeTypeBtn = makeButton("Add Tree Type", "AddTreeType");
        Button updateTreeTypeBtn = makeButton("Update Tree Type", "UpdateTreeType");
        startShiftBtn = makeButton("Start Shift", "StartShift");
        endShiftBtn = makeButton("End Shift", "EndShift");
        sellTreeBtn = makeButton("Sell Tree", "SellTree");
        Button doneBtn = makeDoneButton();

        container.getChildren().add(title);
        container.getChildren().add(registerScoutBtn);
        container.getChildren().add(updateScoutBtn);
        container.getChildren().add(removeScoutBtn);
        container.getChildren().add(addTreeBtn);
        container.getChildren().add(updateTreeBtn);
        container.getChildren().add(removeTreeBtn);
        container.getChildren().add(addTreeTypeBtn);
        container.getChildren().add(updateTreeTypeBtn);
        container.getChildren().add(startShiftBtn);
        container.getChildren().add(endShiftBtn);
        container.getChildren().add(sellTreeBtn);
        container.getChildren().add(doneBtn);

        getChildren().add(container);
        myModel.subscribe("DisableButtons", this);
        myModel.subscribe("EnableButtons", this);
    }

    private Node makeTitle() {
		Text title = new Text("Christmas Tree Sale System");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		title.setTextAlignment(TextAlignment.CENTER);
		title.setFill(Color.DARKGREEN);
		return title;
	}

    private Button makeButton(String label, String action) {
        Button btn = new Button(label);
        setButtonStyles(btn);
        btn.setOnAction( event -> {
            myModel.stateChangeRequest(action, "");
        });
        return btn;
    }

    private void setButtonStyles(Button btn) {
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setTextAlignment(TextAlignment.CENTER);
    }

    private Button makeDoneButton() {
		Button btn = new Button("Done");
		setButtonStyles(btn);
		btn.setOnAction( event -> {
			System.exit(0);
		});
		return btn;
	}

    public void updateState(String key, Object value) {
        switch(key) {
            case "DisableButtons":
                toggleButtonDisabled(true);
                break;
            case "EnableButtons":
                toggleButtonDisabled(false);
                break;
        }
    }

    private void toggleButtonDisabled(Boolean disabled) {
        endShiftBtn.setDisable(disabled);
        sellTreeBtn.setDisable(disabled);
        startShiftBtn.setDisable(!disabled);
    }
    
}
