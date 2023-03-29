package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            case "StartShift": return new StartShiftAction();
            default: return null;
        }
    }
}
