package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            //case "StartShift": return new StartShiftAction();
            //case "AddTree": return new AddTreeAction();
            //case "StartShift": return new StartShiftAction();
            case "RegisterScout": return new RegisterScoutAction();
            default: return null;
        }
    }
}
