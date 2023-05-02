package model;

import java.util.Properties;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.scene.Scene;

public class EndShiftAction extends Action {

    private double totalCheckSales;
    private double endCash;
    private Session currentSession;
    private String statusMessage = "";

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
        switch(key) {
            case "TotalCheckSales":
                return formatUSAmount(totalCheckSales);
            case "EndingCash":
                return formatUSAmount(endCash);
            case "EndTime":
                return currentSession.getState("EndTime");
            case "StatusMessage":
                return statusMessage;
            default:
                return null;
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch(key) {
            case "DoYourJob":
                calcEndingSales();
                doYourJob();
                break;
            case "Confirm":
                endSession((Properties)value);
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private String formatUSAmount(double amount) {
        NumberFormat USFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return USFormat.format(amount);
    }

    private void calcEndingSales() {
        // get session ID
        String sessionID = (String)currentSession.getState("ID");
        // get collection of transactions corresponding to session ID
        TransactionCollection transactions = new TransactionCollection();
        transactions.findTransactionsForSession(sessionID);
        // get starting cash
        double startCash = Double.parseDouble((String)currentSession.getState("StartingCash"));
        // get total cash sales -> add to starting cash
        double cashSales = (double)transactions.getState("TotalCashSales");
        endCash = startCash + cashSales;
        // get total check sales
        totalCheckSales = (double)transactions.getState("TotalCheckSales");
    }

    private void endSession(Properties props) {
        String notes = props.getProperty("Notes");
        String endHour = props.getProperty("EndHour");
        String endMinute = props.getProperty("EndMinute");

        // validate end hour 0-23, end minute 0-59
        if(!Pattern.matches("\\d{0,2}", endHour)) {
            statusMessage = "End hour must be between 0 and 23";
            stateChangeRequest("ConfirmError", "");
            return;
        }
        if(Integer.parseInt(endHour) > 23) {
            statusMessage = "End hour must be between 0 and 23";
            stateChangeRequest("ConfirmError", "");
            return;
        }
        if(!Pattern.matches("\\d{0,2}", endMinute)) {
            statusMessage = "End minute must be between 0 and 59";
            stateChangeRequest("ConfirmError", "");
            return;
        }
        if(Integer.parseInt(endMinute) > 59) {
            statusMessage = "End minute must be between 0 and 59";
            stateChangeRequest("ConfirmError", "");
            return;
        }
        // validate new end time is after start time
        String startTime = (String)currentSession.getState("StartTime");
        int startH = Integer.parseInt(startTime.split(":")[0]);
        int startM = Integer.parseInt(startTime.split(":")[1]);
        int endH = Integer.parseInt(endHour);
        int endM = Integer.parseInt(endMinute);
        if(startH > endH || (startH == endH && startM > endM)) {
            statusMessage = "End time must be after " + startH + ":" + startM;
            stateChangeRequest("ConfirmError", "");
            return;
        }

        // turn end time into xx:xx
        if(endHour.length() == 1) {
            endHour = "0" + endHour;
        }
        if(endMinute.length() == 1) {
            endMinute = "0" + endMinute;
        }
        String endTime = endHour + ":" + endMinute;

        
        notes = notes == null ? "" : notes;
        if(notes.length() > 500) {
            statusMessage = "Notes must be less than 500 characters.";
            stateChangeRequest("ConfirmError", "");
            return;
        }
        Properties newProps = new Properties();
        newProps.setProperty("EndTime", endTime);
        newProps.setProperty("EndingCash", String.valueOf(endCash));
        newProps.setProperty("TotalCheckTransactionsAmount", String.valueOf(totalCheckSales));
        newProps.setProperty("Notes", notes);
        currentSession.save(newProps);
        stateChangeRequest("ShiftEnded", "");
    }
    
}
