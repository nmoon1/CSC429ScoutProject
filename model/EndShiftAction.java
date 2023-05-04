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
//                Session session = new Session();
//                session.setState("EndTime", props.getProperty("Time"));
//                session.setState("Notes", props.getProperty("Notes"));
//                session.updateStateInDatabase();
                //System.out.println(props.getProperty("Time"));
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
//        String endTime = Instant.now().atZone(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.MINUTES).toString();
//        int index = endTime.indexOf(":");
//        endTime = endTime.substring(index - 2, index + 3);

        ////////////////////////////////////////////////////////////////
        String Timecheck = props.getProperty("Time");
        String TimecheckM ,TimecheckH, TimecheckM1;


        TimecheckM = Timecheck.substring(3);
        TimecheckM1 = Timecheck.substring(2);
        TimecheckH = Timecheck.substring(0, 2);
        String notes = props.getProperty("Notes");

        System.out.println(TimecheckM);
        System.out.println(TimecheckH);
        System.out.println(TimecheckM1);

        System.out.println("cats cats1");

////////////////////////
        int endHourInt, endMinInt;
        System.out.println("cats cats2");

            endMinInt = Integer.parseInt(TimecheckM);
        System.out.println("hmmm why does it break before it can print this hmmmmm");

        System.out.println(TimecheckM);

        endHourInt = Integer.parseInt(TimecheckH);
            // check for 24 hour time
            if (endHourInt < 0 || endHourInt > 25) {
                stateChangeRequest("EndTimeH", "");
                return;
            }
            // check if start and end minute are between 0 and 59
            if (endMinInt < 0 || endMinInt > 60) {
                stateChangeRequest("EndTimeM", "");
                return;
            }

            // add code to vaildate that its between  0 - 23 and acutually numbers
            //  Pattern.matches("^\\d{2}$", text)
            // make the text boxes a reasonable size
//            int i = Integer.parseInt(Timecheck);

//        endMin.setText(minTime.substring(3));
//        endHour.setText(HourTime.substring(0,2));

//        Timecheck = Timecheck == null ? "" : Timecheck;
//        if(notes.length() > 500) {
//            stateChangeRequest("EndTimeM", "");
//            return;
//        }
//            Timecheck = Timecheck == null ? "" : Timecheck;
//            if (i > 23 || i < 0) {
//                stateChangeRequest("EndTimeH", "");
//                return;
//            }


            /////////////////////////////////
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


///
        }
    }


