// system imports
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

// project imports
import event.Event;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;
import model.TLC;

public class Main extends Application {
    private Stage mainStage;
    private TLC myTLC;

    public void start(Stage primaryStage) {
        MainStageContainer.setStage(primaryStage, "Christmas Tree Sales System");
        mainStage = MainStageContainer.getInstance();

        // enable close (x) button
        mainStage.setOnCloseRequest(new EventHandler<javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent e) {
                System.exit(0);
            }
        });

        try {
            myTLC = new TLC();
        } catch(Exception e) {
            System.err.println("Could not create TLC!");
            new Event(Event.getLeafLevelClassName(this), "Main.<init>", "Unable to create TLC object", Event.ERROR);
            e.printStackTrace();
        }

        WindowPosition.placeCenter(mainStage);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}