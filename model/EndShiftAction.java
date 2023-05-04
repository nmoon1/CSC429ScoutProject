package model;

import java.util.Properties;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.*;

import javafx.scene.Scene;

public class EndShiftAction extends Action {

    private double totalCheckSales;
    private double endCash;
    private Session currentSession;

    public EndShiftAction() throws Exception {
        super();
        currentSession = new Session();
    }

    @Override
    protected Scene createView() {
        return getOrCreateScene("EndShiftActionView");
    }


    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Cancel", "CancelAction");
        dependencies.setProperty("Done", "CompleteAction");
        myRegistry.setDependencies(dependencies);
    }
 
    public Object getState(String key) {
        switch (key) {
            case "TotalCheckSales":
                return String.valueOf(totalCheckSales);
            case "EndingCash":
                return String.valueOf(endCash);
            default:
                return null;
        }
    }


    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob":
                calcEndingSales();
                doYourJob();
                break;
            case "Confirm":
                Properties props = (Properties) value;
                endSession(props);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void calcEndingSales() {
        // get session ID
        String sessionID = (String) currentSession.getState("ID");
        // get collection of transactions corresponding to session ID
        TransactionCollection transactions = new TransactionCollection();
        transactions.findTransactionsForSession(sessionID);
        // get starting cash
        double startCash = Double.parseDouble((String) currentSession.getState("StartingCash"));
        // get total cash sales -> add to starting cash
        double cashSales = (double) transactions.getState("TotalCashSales");
        endCash = startCash + cashSales;
        // get total check sales
        totalCheckSales = (double) transactions.getState("TotalCheckSales");
    }

    private void endSession(Properties props) {
        Properties props1 = new Properties();
        String Timecheck = props.getProperty("Time");
        String TimecheckMin = props.getProperty("Time");
        String TimecheckHour = props.getProperty("Time");
        String TimecheckH;

        int index = Timecheck.indexOf(":");
        TimecheckHour = TimecheckHour.substring(index - 2, index + 0);
        TimecheckMin = TimecheckMin.substring(index - -1, index + 3);

        TimecheckH = Timecheck.substring(0, 2);
        String notes = props.getProperty("Notes");

        int endHourInt, endMinInt;
        if (!TimecheckHour.matches("^\\d{2}$") || TimecheckHour.isEmpty()
                || !TimecheckMin.matches("^\\d{2}$") || TimecheckMin.isEmpty()
        ) {
            stateChangeRequest("EndTimeNumbersOnly", "");
            return;
        }
            // check for 24 hour time
               endHourInt = Integer.parseInt(TimecheckH);
               endMinInt = Integer.parseInt(TimecheckMin);
               if (endHourInt < 0 || endHourInt > 24) {
                stateChangeRequest("EndTimeH", "");
                return;
            }
            // check if endminute is between 0 and 59
            if (endMinInt < 0 || endMinInt > 60) {
                stateChangeRequest("EndTimeM", "");
                return;
            }
            notes = notes == null ? "" : notes;
            if (notes.length() > 500) {
                stateChangeRequest("NotesError", "");
                return;
            }
            props1.setProperty("EndTime", props.getProperty("Time"));
            props1.setProperty("EndingCash", String.valueOf(endCash));
            props1.setProperty("TotalCheckTransactionsAmount", String.valueOf(totalCheckSales));
            props1.setProperty("Notes", notes);
            currentSession.save(props1);
            stateChangeRequest("ShiftEnded", "");
        }
    }


