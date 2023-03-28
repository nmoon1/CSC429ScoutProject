package userinterface;

import impresario.IModel;
//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		switch (viewName) {
			case "TLCView": return new TLCView(model);
		}
		return null;
	}

}
