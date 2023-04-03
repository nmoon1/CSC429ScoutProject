package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            //case "StartShift": return new StartShiftAction();
            //case "AddTree": return new AddTreeAction();
            //case "StartShift": return new StartShiftAction();
            case "RegisterScout": return new RegisterScoutAction();
            case "UpdateScout": return new UpdateScoutAction();
            case "RemoveScout": return new RemoveScoutAction();
            default: return null;
        }
    }
}
