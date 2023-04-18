package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            case "RemoveTree": return new RemoveTreeAction();
            case "RegisterScout": return new RegisterScoutAction();
            case "UpdateScout": return new UpdateScoutAction();
            case "RemoveScout": return new RemoveScoutAction();
            case "AddTreeType": return new AddTreeTypeAction();
            case "StartShift": return new StartShiftAction();
            case "AddTree": return new AddTreeAction();
            case "UpdateTreeType": return new UpdateTreeTypeAction();
            case "UpdateTree": return new UpdateTreeAction();
            case "EndShift": return new EndShiftAction();
            default: return null;
        }
    }
}
