package model;

import impresario.IView;
import java.util.Vector;
import java.util.Properties;

public class TransactionCollection extends EntityBase implements IView {

    private static final String myTableName = "Transaction";
    private Vector<Transaction> transactionList;

    private double totalCashSales;
    private double totalCheckSales;

    public TransactionCollection() {
        super(myTableName);
        transactionList = new Vector<Transaction>();
    }

    public void findTransactionsForSession(String sessionID) {
        String query = "SELECT * FROM " + myTableName + " WHERE (SessionID = " + sessionID + " )";
        Vector<Properties> allData = getSelectQueryResult(query);
        transactionList.clear();
        for(int i = 0; i < allData.size(); i++) {
            Properties data = allData.elementAt(i);
            Transaction t = new Transaction(data);
            transactionList.add(t);
        }
        calcTotalSales();
    }

    private void calcTotalSales() {
        for(int i = 0; i < transactionList.size(); i++) {
            Transaction t = transactionList.elementAt(i);
            String paymentMethod = (String)t.getState("PaymentMethod");
            if(paymentMethod.equals("Cash")){
                double amount = Double.parseDouble((String)t.getState("TransactionAmount"));
                totalCashSales += amount;
            } else if(paymentMethod.equals("Check")) {
                double amount = Double.parseDouble((String)t.getState("TransactionAmount"));
                totalCheckSales += amount;
            }
        }
    }

    @Override
    public Object getState(String key) {
        switch(key) {
            case "TotalCashSales":
                return totalCashSales;
            case "TotalCheckSales":
                return totalCheckSales;
            default:
                return null;
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateState(String key, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initializeSchema(String tableName) {
        if(mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
}