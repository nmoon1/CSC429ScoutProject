package userinterface;

import impresario.IModel;
//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		switch (viewName) {
			case "TLCView": return new TLCView(model);
			case "StartShiftActionView": return new StartShiftActionView(model);
			case "AddTree": return new AddTreeActionView(model);
			case "RemoveTreeActionView": return new RemoveTreeActionView(model);
			case "RemoveTreeConfirmationView": return new RemoveTreeConfirmationView(model);
			case "RemoveTreeDoneView": return new RemoveTreeDoneView(model);
		}
		return null;
	}

}
