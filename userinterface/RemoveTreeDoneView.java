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

public class RemoveTreeDoneView extends View {
    
    public RemoveTreeDoneView(IModel model) {
        super(model, "RemoveTreeDoneView");
        VBox container = createContents();
        getChildren().add(container);
    }

    private VBox createContents() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.setAlignment(Pos.CENTER);

        Text label = new Text("Tree Removed Successfully.");
        Button done = new Button("Done");
        done.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        done.setTextAlignment(TextAlignment.CENTER);
        done.setOnAction( action -> {
            myModel.stateChangeRequest("Done", "");
        });

        container.getChildren().add(label);
        container.getChildren().add(done);

        return container;
    }

    public void updateState(String key, Object value) {
        
    }
}
