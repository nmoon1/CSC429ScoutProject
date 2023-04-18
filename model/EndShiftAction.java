package model;

import java.util.Properties;

import javafx.scene.Scene;

public class EndShiftAction extends Action {

    private double totalCheckSales;
    private double endCash;

    public EndShiftAction() throws Exception {
        super();
    }

    @Override
    protected Scene createView() {
        return getOrCreateScene("EndShiftActionView");
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("Cancel", "CancelAction");
        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {
        switch(key) {
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
        switch(key) {
            case "DoYourJob":
                calcEndingSales();
                doYourJob();
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void calcEndingSales() {
        // get session ID
        Session currentSession = new Session();
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
    
}
