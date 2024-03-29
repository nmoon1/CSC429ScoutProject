package model;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.regex.Pattern;

import javafx.scene.Scene;

public class SellTreeAction extends Action {

	private static final String barcodeViewName = "SellTreeBarcodeView";
	private static final String costViewName = "SellTreeCostView";
	private static final String infoViewName = "SellTreeInfoView";
	private static final String doneViewName = "SellTreeDoneWindow";
	private String errorMessage = "";
	private String cost = "";
	private String barcode = "";
	private String notes = "";
	private String description = "";
	private Tree tree;
	
	protected SellTreeAction() throws Exception {
		super();
	}

	@Override
	protected void setDependencies() { }

	@Override
	protected Scene createView() {
		return getOrCreateScene(barcodeViewName);
	}

	@Override
	public Object getState(String key) {
		switch (key) {
			case "Error": return errorMessage;
			case "Stage": return myStage;
			case "Cost": return cost;
			case "Notes": return notes;
			case "Description": return description;
		}
		
		return null;
	}

	@Override
	public void stateChangeRequest(String key, Object value) {
		switch (key) {
			case "DoYourJob":
				doYourJob();
				break;
			case "CancelAction":
				myRegistry.updateSubscribers(key, this);
				break;
			case "BackCost":
				swapToView(getOrCreateScene(barcodeViewName));
				break;
			case "BackInfo":
				swapToView(getOrCreateScene(costViewName));
				break;
			case "SubmitBarcode":
				{
					errorMessage = "";
					
					// Validate barcode
					barcode = (String)value;
					if (!Pattern.matches("^\\d{5}$", barcode)) {
						errorMessage = "Barcode must be 5 digits";
						return;
					}
					
					// Make sure that the tree exists and is not sold
					try {
						tree = new Tree(barcode);
						if (tree.getState("Status").equals("Sold")) {
							errorMessage = "Tree with barcode " + barcode + " has already been sold";
							return;
						}
					} catch (Exception e) {
						errorMessage = "No tree exists with barcode " + barcode;
						return;
					}
					notes = (String)tree.getState("Notes");
					
					// Get tree type cost
					String prefix = barcode.substring(0, 2); 
					try {
						TreeType treeType = new TreeType(prefix);
						cost = (String)treeType.getState("Cost");
						description = (String)treeType.getState("TypeDescription");
						swapToView(getOrCreateScene(costViewName));
					} catch (Exception e) {
						errorMessage = "No tree type with barcode prefix matching \"" + prefix + "\"";
						return;
					}
				}
				break;
			case "SubmitCost":
				errorMessage = "";

				// Validate cost is a dollar value
				Properties props = (Properties) value;
				cost = props.getProperty("Cost");
				notes = props.getProperty("Notes") == null? "" : props.getProperty("Notes");
				if (!Pattern.matches("^\\d+(\\.\\d\\d?)?$", cost)) {
					errorMessage = "Invalid cost";
					return;
				}
				if(notes.length() > 200){
					errorMessage = "Notes must be less than 200 characters.";
					return;
				}
				tree.updateState("Notes", notes);
				tree.update();
				
				swapToView(getOrCreateScene(infoViewName));
				break;
			case "SubmitInfo":
				{
					errorMessage = "";
					
					// Validate session
					Session session = new Session();
					
					Properties customerInfo = (Properties)value;
					
					String paymentMethod = customerInfo.getProperty("PaymentMethod");
					String custName = "",
						   phoneNumber = "",
						   email = "";
					if(paymentMethod.equals("Check")) {
						// Validate customer name
						custName = customerInfo.getProperty("Name");

						if (custName == null || custName.length() == 0) {
							errorMessage = "Name cannot be empty";
							return;
						}
						
						if (custName.length() > 50) {
							errorMessage = "Name is too long";
							return;
						}
						
						// Validate phone number
						phoneNumber = customerInfo.getProperty("PhoneNumber");
						if (phoneNumber.length() == 0) {
							errorMessage = "Phone number cannot be empty";
							return;
						}
						
						if (Pattern.matches("^\\d{3}\\-\\d{3}\\-\\d{4}$", phoneNumber))
							phoneNumber = phoneNumber.substring(0, 3) + phoneNumber.substring(4, 7) + phoneNumber.substring(8);
						else if (Pattern.matches("^\\(\\d{3}\\)\\d{3}\\-\\d{4}$", phoneNumber))
							phoneNumber = phoneNumber.substring(1, 4) + phoneNumber.substring(5, 8) + phoneNumber.substring(9);
						else if (!Pattern.matches("^\\d{10}$", phoneNumber)) {
							errorMessage = "Phone number must be of the form XXXXXXXXXX or (XXX)XXX-XXXX or XXX-XXX-XXXX";
							return;
						}
						
						// Validate email
						email = customerInfo.getProperty("Email");
						if (!email.contains("@")) {
							errorMessage = "Invalid email";
							return;
						}
					}
					
			    	// Time is HHMM
					Properties transactionProps = new Properties();
					transactionProps.setProperty("CustomerName", custName);
					transactionProps.setProperty("CustomerPhone", phoneNumber);
					transactionProps.setProperty("CustomerEmail", email);
					transactionProps.setProperty("PaymentMethod", customerInfo.getProperty("PaymentMethod"));
					transactionProps.setProperty("SessionID", (String)session.getState("ID"));
					transactionProps.setProperty("TransactionAmount", cost);
					transactionProps.setProperty("TransactionType", "Tree Sale");
					
					// Format current date
					LocalDateTime date = LocalDateTime.now();
					String year = date.getYear() + "";
					while (year.length() < 4) year = "0" + year;
					String month = date.getMonthValue() + "";
					if (month.length() < 2) month = "0" + month;
					String day = date.getDayOfMonth() + "";
					if (day.length() < 2) day = "0" + day;
					transactionProps.setProperty("TransactionDate", year + "-" + month + "-" + day);
					
					// Format current time
					String hour = date.getHour() + "";
					if (hour.length() < 2) hour = "0" + hour;
					String minute = date.getMinute() + "";
					if (minute.length() < 2) minute = "0" + minute;
					transactionProps.setProperty("TransactionTime", hour + minute);
					
					// Retrieve Tree
					Tree tree;
					try {
						tree = new Tree(barcode);
					} catch (Exception e) {
						errorMessage = "Could not find a tree with barcode " + barcode;
						return;
					}
					tree.setState("Status", "Sold");
					tree.update();
					
					Transaction transaction = new Transaction(transactionProps);
					transaction.updateStatusDate();
					transaction.update();
					swapToView(getOrCreateScene(doneViewName));
				}
				break;
		}
	}
}