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
			case "RegisterScoutView": return new RegisterScoutView(model);
			case "UpdateScoutIDView": return new UpdateScoutIDView(model);
			case "UpdateScoutInfoView": return new UpdateScoutInfoView(model);
			case "UpdateScoutListView": return new UpdateScoutListView(model);
			case "RemoveScoutIDView": return new RemoveScoutIDView(model);
			case "RemoveScoutListView": return new RemoveScoutListView(model);
			case "RemoveScoutConfirmView": return new RemoveScoutConfirmView(model);
			case "AddTreeTypeActionView": return new AddTreeTypeActionView(model);
		}
		return null;
	}

}
